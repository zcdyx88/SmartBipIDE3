package com.dc.bip.ide.gef.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.dc.bip.ide.gef.model.IImageKey;
import com.dc.bip.ide.gef.model.LayerModel;
import com.dc.bip.ide.gef.model.LineConnectionModel;
import com.dc.bip.ide.gef.model.Param;
import com.dc.bip.ide.gef.model.ParamContainer;
import com.dc.bip.ide.gef.model.ParamScope;
import com.dc.bip.ide.gef.model.TitleModel;
import com.dc.bip.ide.gef.parts.PartFactory;
import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.objects.IService;
import com.dc.bip.ide.objects.param.IParam;
import com.dc.bip.ide.util.HelpUtil;

public class GefEditor extends GraphicalEditorWithFlyoutPalette implements PropertyChangeListener{
/*	private  List<ServiceMode>  toolEntryList = new ArrayList<ServiceMode>();
	private CompositeService compositeService;
	private DefaultEditDomain myEditDomain;*/
	private boolean dirty=false;
	private LayerModel layer;
	final static public  String InvaildParam = "_invaildParam_";
	/**
	 * 当前编辑器操作的组合服务
	 */
    private CompositeService compositeService;
    
    /**
     * 当前编辑器操作的业务服务
     */
	private BusiSvcInfo busiSvcInfo;
	
	/**
	 * 当前编辑器操作的参数属于request(true),还是response(false)
	 */
	private boolean inOrOut;
	
	public GefEditor()
	{
		this.setEditDomain(new DefaultEditDomain(this));		
	}
	
	@Override
	protected void initializeGraphicalViewer() {
		layer.setEditListener(this);
		this.getGraphicalViewer().setContents(layer);
	}
	
	protected void configureGraphicalViewer() {
		this.getGraphicalViewer().setEditPartFactory(new PartFactory());
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			HelpUtil.writeObject(layer, ((FileEditorInput) getEditorInput()).getFile());
			createMapXML();
			setDirty(false);
			firePropertyChange(PROP_DIRTY);
		} catch (Exception e) {
			e.printStackTrace();
		} 	
	}

	/**
	 * 使用默认的画图接口初始化后，在调用该自定义函数重新增加工具
	 * @return
	 */
	private PaletteRoot getBipPaletteRoot() {
		
		System.out.println("invoke getPaletteRoot");
		PaletteRoot paletteRoot = new PaletteRoot();
		PaletteGroup toolGroup = new PaletteGroup("工具");
		//单选
		ToolEntry tool = new SelectionToolEntry();		
		paletteRoot.setDefaultEntry(tool);
		toolGroup.add(tool);
		
		//选择框
		tool = new MarqueeToolEntry();
		toolGroup.add(tool);
		
		//映射连接线
		ImageDescriptor  descriptor = AbstractUIPlugin.imageDescriptorFromPlugin("plguinId", IImageKey.lineConnection);
		ConnectionCreationToolEntry connectionToolEntry = new ConnectionCreationToolEntry(
				"新建映射", "新建参数间的映射", new SimpleFactory(LineConnectionModel.class), descriptor, descriptor);
		toolGroup.add(connectionToolEntry);
		
		PaletteDrawer serviceGroup = new PaletteDrawer("服务");
		
		ServiceFactory serviceFactory;
		
		//增加组合服务的IN&&OUT		
		serviceFactory = new ServiceFactory(ParamContainer.class,compositeService.getInParams());
		 descriptor = AbstractUIPlugin.imageDescriptorFromPlugin("plguinId", IImageKey.service);
		 CreationToolEntry serviceToolEntry;
		 if(null != compositeService.getInParams())
		 {
	            serviceToolEntry = new CreationToolEntry(
	    		  compositeService.getInParams().getName() , "使用"+compositeService.getInParams().getName()+"参数进行映射", serviceFactory, descriptor, descriptor);
	      serviceGroup.add(serviceToolEntry);
		 }
	      if(null != compositeService.getOutParams())
	      {
		 serviceFactory = new ServiceFactory(ParamContainer.class,compositeService.getOutParams());
		 descriptor = AbstractUIPlugin.imageDescriptorFromPlugin("plguinId", IImageKey.service);
	      serviceToolEntry = new CreationToolEntry(
	    		  compositeService.getOutParams().getName(), "使用"+compositeService.getOutParams().getName()+"参数进行映射", serviceFactory, descriptor, descriptor);
	      serviceGroup.add(serviceToolEntry);
	      }
		
		//增加业务服务节点
		for(ParamContainer tmpContainer : getToolEntryList())
		{
			 serviceFactory = new ServiceFactory(ParamContainer.class,tmpContainer);
			 descriptor = AbstractUIPlugin.imageDescriptorFromPlugin("plguinId", IImageKey.service);
		      serviceToolEntry = new CreationToolEntry(
		    		  tmpContainer.getName(), "使用"+  tmpContainer.getName()+"参数进行映射", serviceFactory, descriptor, descriptor);
		      serviceGroup.add(serviceToolEntry);
		}
		
		//增加全局上下文参数
		List<Param> globalParams = HelpUtil.readGlobalParams(compositeService);
		if(globalParams.size() > 0)
		{
			ParamContainer globalContainer = new ParamContainer("全局上下文参数");
			globalContainer.addChild(new TitleModel("全局上下文参数"));
			globalContainer.setScope(ParamScope.Global);		
			for(Param global : globalParams)
			{
				globalContainer.addChild(global);
			}
			 serviceFactory = new ServiceFactory(ParamContainer.class,globalContainer);
			 descriptor = AbstractUIPlugin.imageDescriptorFromPlugin("plguinId", IImageKey.service);
		     serviceToolEntry = new CreationToolEntry(
		    		  globalContainer.getName(), "使用"+ globalContainer.getName()+"参数进行映射", serviceFactory, descriptor, descriptor);
		      serviceGroup.add(serviceToolEntry);
		}
		
	      
		//增加session的上下文参数		
		List<Param> sessionParams = HelpUtil.readSessionParams(compositeService);
		if(sessionParams.size()>0)
		{
			ParamContainer sessionContainer = new ParamContainer("session上下文参数");
			sessionContainer.addChild(new TitleModel("session上下文参数"));
			sessionContainer.setScope(ParamScope.Session);		
			for(Param session :  sessionParams )
			{
				sessionContainer.addChild(session);
			}
			 serviceFactory = new ServiceFactory(ParamContainer.class,sessionContainer);
			 descriptor = AbstractUIPlugin.imageDescriptorFromPlugin("plguinId", IImageKey.service);
		      serviceToolEntry = new CreationToolEntry(
		    		  sessionContainer.getName(), "使用"+ sessionContainer.getName()+"参数进行映射", serviceFactory, descriptor, descriptor);
		      serviceGroup.add(serviceToolEntry);
		}
		
		
		
		paletteRoot.add(toolGroup);
		paletteRoot.add(serviceGroup);
		return paletteRoot;
	}	

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);			
		IFile mapFile = ((FileEditorInput) getEditorInput()).getFile();
		String mapFileName = mapFile.getName();
		String[] tmpIds = mapFileName.substring(0, mapFileName.lastIndexOf(".")).split("_");
			if(null !=tmpIds && tmpIds.length>1)
			{   
				String path = mapFile.getFullPath().toString();
				String parentPath1 = path.substring(0,path.lastIndexOf("/"));
				String parentPath2 = parentPath1.substring(0,parentPath1.lastIndexOf("/"));
				String parentPath = parentPath2.substring(0,parentPath2.lastIndexOf("/")+1);	
				StringBuilder sb =new StringBuilder();
				sb.append(parentPath).append(tmpIds[0]).append(".composite");
				IFile compsiteFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(sb.toString()));
				compositeService = HelpUtil.readObject(compsiteFile);
				
				if (null ==  compositeService )
				{
					throw new PartInitException("组合服务文件不存在");
				}
//				compositeService = new CompositeService();
	/*			CompServiceBaseInfo basicInfo = new CompServiceBaseInfo();
				basicInfo.setServiceId("test3");
				compositeService.setBaseinfo(basicInfo);*/
				compositeService.getBusiSvcNodesFromFlow();
				compositeService.refreshParams();
				
				//选择组合服务的IN或者OUT参数容器中的参数进行映射
				if( 2 == tmpIds.length)
				{
					//选择了组合服务的IN参数容器
					if(tmpIds[1].equalsIgnoreCase("IN"))
					{
						busiSvcInfo = null;
						inOrOut = true;
					}
					//选择了组合服务的OUT参数容器
					else if(tmpIds[1].equalsIgnoreCase("OUT"))
					{
						busiSvcInfo = null;
						inOrOut = false;
					}
				}
				//选择组合服务包含的某个业务服务的IN或者OUT参数容器中的参数进行映射
				else if (3 == tmpIds.length)
				{
					boolean validBusiSvc = false;
					for(BusiSvcInfo tmpObj  : compositeService.getBusiSvcNodes())
					{
						if(tmpObj.getId().equalsIgnoreCase(tmpIds[1]))
						{
							validBusiSvc = true;
							//选择了业务服务的IN参数容器
							if(tmpIds[2].equalsIgnoreCase("IN"))
							{
								busiSvcInfo = tmpObj;
								inOrOut = true;
							}
							//选择了业务服务的OUT参数容器
							else if(tmpIds[2].equalsIgnoreCase("OUT"))
							{
								busiSvcInfo = tmpObj;
								inOrOut = false;
							}
							break;
						}
					}
				 if(!validBusiSvc)
				 {
					 throw new PartInitException("未找到对应的业务服务！");
				 }
				}
			}		
			
			layer = HelpUtil.readContextObject(mapFile);
			if( null ==layer)
			{
			     layer = new LayerModel();			
			}
			else if(layer.getChildren().size()==0)
			{
				ParamContainer tmpContainer;
				if(null !=compositeService && busiSvcInfo == null)
				{
					if(inOrOut)
					{
						if(null != compositeService.getInParams())
						{
							tmpContainer = compositeService.getInParams();						 
							 tmpContainer.setRectangle(new Rectangle(0,0,-1,-1));
							layer.addChild(tmpContainer);	
						}
					}
					else
					{
						if(null != compositeService.getOutParams())
						{
							 tmpContainer = compositeService.getOutParams();
							 tmpContainer.setRectangle(new Rectangle(0,0,-1,-1));
							layer.addChild(tmpContainer);	
						}
					}					
				}
				else if( null !=compositeService && busiSvcInfo != null )
				{
					if(inOrOut)
					{
						if(null != busiSvcInfo.getInParams())
						{
							 tmpContainer = busiSvcInfo.getInParams();
							 tmpContainer.setRectangle(new Rectangle(0,0,-1,-1));
							layer.addChild(busiSvcInfo.getInParams());	
						}
					}
					else
					{
						if(null != busiSvcInfo.getInParams())
						{
							 tmpContainer = busiSvcInfo.getOutParams();
							 tmpContainer.setRectangle(new Rectangle(0,0,-1,-1));
							layer.addChild(tmpContainer);	
						}
					}		
				}
			}
		
		
		//使用默认的画图接口初始化后，再调用该自定义函数重新增加工具
		getEditDomain().setPaletteRoot(getBipPaletteRoot());
	}
	
	
	private List<ParamContainer> getToolEntryList()
	{
		List<ParamContainer> paramContainerList = new ArrayList<ParamContainer>();

		for(IService tmpService :compositeService.getBusiSvcNodes())
		{
			paramContainerList.add(tmpService.getInParams());
			paramContainerList.add(tmpService.getOutParams());
		}
		return paramContainerList;
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		return null;
	}
	
	@Override
	public boolean isDirty()
	{
		return this.dirty;
	}
	
	private void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		setDirty(true);
		firePropertyChange(PROP_DIRTY);
	}
	
	private void createMapXML()
	{
		Element root = DocumentHelper.createElement("mapping");  
        Document document = DocumentHelper.createDocument(root); 
        List<Param> paramList = new ArrayList<Param>();
        Element operation = null;
        Element otherOperation = null;
        if(null !=compositeService && busiSvcInfo == null)
		{
            root.addAttribute("id",compositeService.getBaseinfo().getServiceId());            
			if(inOrOut)
			{
				operation = root.addElement("request");	
				otherOperation = root.addElement("response");	
			}
			else
			{
				operation = root.addElement("response");
				otherOperation = root.addElement("request");	
			}			
		}
		else if( null !=compositeService && busiSvcInfo != null )
		{
			StringBuilder sb =new StringBuilder();
			sb.append("M" + compositeService.getBaseinfo().getServiceId()).append("_").append(busiSvcInfo.getId());
			root.addAttribute("id",sb.toString());
			if(inOrOut)
			{
				 operation = root.addElement("request");	
				 otherOperation = root.addElement("response");	
			}
			else
			{
				operation = root.addElement("response");
				otherOperation = root.addElement("request");	
			}	
		}       
        
        if(null != operation)
        {
         for(Object tmpObject  : layer.getChildren())
         {
        	 if( tmpObject instanceof ParamContainer)
        	 {
        		 ParamContainer container = (ParamContainer)tmpObject;
        		 if(checkNeedMap(container,inOrOut))
        		 {
        			 for(Object tmpChildObject : container.getChildren())
        			 {
        				 if(tmpChildObject instanceof ParamContainer)
        				 {
        					 ParamContainer childContainer = (ParamContainer)tmpChildObject;
        					 addMapItemNew(operation,childContainer);
        				 }
        			 }        			
        			 break;
        		 }        		 
        	 }
         }
        }
        
        //寻找另一半映射文件。画图是IN与OUT分开，生成XML时要合在一起
        String mapFileName =  ((FileEditorInput) getEditorInput()).getFile().getFullPath().toString();
        String otherMapFileName =null;
        if(mapFileName.endsWith("_IN.map"))
        {
        	otherMapFileName= mapFileName.replace("_IN.map", "_OUT.map");
        }
        else if(mapFileName.endsWith("_OUT.map"))
        {
        	otherMapFileName= mapFileName.replace("_OUT.map", "_IN.map");
        }
        if(otherMapFileName != null && otherOperation != null)
        {
        	 List<Param> otherParamList = new ArrayList<Param>();
        	IFile otherMapFile =ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(otherMapFileName));
            LayerModel  otherLayerModel = HelpUtil.readContextObject(otherMapFile);
            for(Object tmpObject  : otherLayerModel.getChildren())
            {
            	ParamContainer container = (ParamContainer)tmpObject;
	           	 if( tmpObject instanceof ParamContainer)
	           	 {
	           		 for(Object tmpChildObject : container.getChildren())
        			 {
        				 if(tmpChildObject instanceof ParamContainer)
        				 {
        					 ParamContainer childContainer = (ParamContainer)tmpChildObject;
        					 addMapItemNew(otherOperation,childContainer);
        				 }
        			 }        	
        			 break;
	           	 }
            }
        }
        
		try {
		    OutputFormat format = new OutputFormat("    ",true);  
	        format.setEncoding("UTF-8");//设置编码格式  
	        XMLWriter xmlWriter;
	        String mapFilePath =  ((FileEditorInput) getEditorInput()).getFile().getLocation().toOSString();
	        String parentPath = mapFilePath.substring(0, mapFilePath.lastIndexOf("_"));
	        StringBuilder sb = new StringBuilder();
	        sb.append(parentPath).append(".xml");
	        File xmlFile = new File(sb.toString());
	        if(!xmlFile.exists())
	        {
	        	xmlFile.getParentFile().mkdirs();
	        	xmlFile.createNewFile();
	        }
			xmlWriter = new XMLWriter(new FileOutputStream(sb.toString()),format);
	        xmlWriter.write(document);  
	        xmlWriter.close(); 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}        

	}
	
	private String getRelativePath(IParam param ,Element parentElement,Boolean sourceOrNot)
	{
		StringBuilder relativePath = new StringBuilder();
		String parentPath= null;
		if(parentElement.getName().equalsIgnoreCase("item"))
		{			
			if(sourceOrNot)
			{
			    parentPath = parentElement.attributeValue("src");
			}
	       else
	       {
	    	   parentPath = parentElement.attributeValue("dest");
		   }
			int operationIndex =-1;
			if(( operationIndex = parentPath.indexOf("."))>-1)
			{
				parentPath = parentPath.substring(operationIndex+1);
			}
		    int index =-1;
		   if( (index = param.getPath().indexOf(parentPath))>-1)
		   {
			   String newPath = param.getPath().substring(index+parentPath.length());
			   if(newPath!= null && newPath.length() >0)
			   {
				   relativePath.append(newPath).append("/").append(param.getName());
			   }else
			   {
				   relativePath.append(param.getName());
			   }			   
		   }
		   else
		   {
			   relativePath.append(GefEditor.InvaildParam);
		   }		
		}
		else
		{
			if(param.getPath() == null || param.getPath().length() == 0)
			{
				relativePath.append(param.getOperationId()).append(".").append(param.getName());
			
			}
			else				
			{
				if("request".equalsIgnoreCase((String)param.getOperationId())){
					
					relativePath.append("Req").append(param.getServiceId()).append(".").append(param.getPath()).append("/").append(param.getName());
				}else if("response".equalsIgnoreCase((String) param.getOperationId())){
					relativePath.append("Rsp").append(param.getServiceId()).append(".").append(param.getPath()).append("/").append(param.getName());
				}
			}
		}
		return relativePath.toString();
	}
	
	/**
	 * 当前处理的情况如下：
	 * （1）一个容器与另一个容器只有一条连接
	 * （2）
	 * @param element
	 * @param paramCotainer
	 */
	private void addMapItemNew(Element element,ParamContainer paramCotainer)
	{
		Element containerElement = null;
		List sourceConnections = paramCotainer.getConnectionSource();
		List targetConnections = paramCotainer.getConnectionTarget();
		
		 if(sourceConnections.size() ==1 )
		 {
				ParamContainer sourceParamContainer = (ParamContainer)sourceConnections.get(0);
				String sourcePath = getRelativePath(sourceParamContainer,element,true);
				String targetPath = getRelativePath(paramCotainer,element,false);
				if(!sourcePath.equalsIgnoreCase(GefEditor.InvaildParam) && !targetPath.equalsIgnoreCase(GefEditor.InvaildParam))
				{
					containerElement = element.addElement("item");
					containerElement.addAttribute("src",sourcePath);
					containerElement.addAttribute("dest",targetPath);	
				}										
		 }
		 else if(targetConnections.size() ==1)
		 {
			 ParamContainer targetParamContainer = (ParamContainer)targetConnections.get(0);
				String sourcePath = getRelativePath(paramCotainer,element,true);						
				String targetPath = getRelativePath(targetParamContainer,element,false);
				if(!sourcePath.equalsIgnoreCase(GefEditor.InvaildParam) && !targetPath.equalsIgnoreCase(GefEditor.InvaildParam))
				{
					containerElement = element.addElement("item");
					containerElement.addAttribute("src",sourcePath);
					containerElement.addAttribute("dest",targetPath);	
				}			
		 }
		 else if (targetConnections.size() >1 || sourceConnections.size()>1)
		 {
			 System.out.println(paramCotainer.getName() +"容器存在多个连接，暂时视为非法，暂不处理！！");
			 return;
		 }
		 
		 if(containerElement == null)
		 {
			 containerElement = element;
		 }
			for(Object tmpObject : paramCotainer.getChildren())
			{
				if(tmpObject instanceof Param)
				{					
					Param tmpParam =(Param)tmpObject;					
					for(Object tmpCont : tmpParam.getSourceConnection())
					{
						LineConnectionModel connection = (LineConnectionModel)tmpCont;
						String sourcePath = getRelativePath(tmpParam,containerElement,true);
						String targetPath = getRelativePath((Param)connection.getTarget(),containerElement,false);
						if(!sourcePath.equalsIgnoreCase(GefEditor.InvaildParam) && !targetPath.equalsIgnoreCase(GefEditor.InvaildParam))
						{
							Element paramElement = containerElement.addElement("item");
							paramElement.addAttribute("src",sourcePath);
							paramElement.addAttribute("dest",targetPath);	
						}				
					}
					for(Object tmpCont : tmpParam.getTargetConnection())
					{
						LineConnectionModel connection = (LineConnectionModel)tmpCont;
						String sourcePath = getRelativePath((Param)connection.getSource(),containerElement,true);								
						String targetPath = getRelativePath(tmpParam,containerElement,false);
						if(!sourcePath.equalsIgnoreCase(GefEditor.InvaildParam) && !targetPath.equalsIgnoreCase(GefEditor.InvaildParam))
						{
							Element paramElement = containerElement.addElement("item");
							paramElement.addAttribute("src",sourcePath);
							paramElement.addAttribute("dest",targetPath);	
						}				
					}
				}
				else if (tmpObject instanceof ParamContainer)
				{
					addMapItemNew(containerElement,(ParamContainer)tmpObject);
				}
			}	
	}	
	
/**
 * 当前的容器是否为该编辑器编辑的容器
 * @param parm
 * @return
 */
  private boolean checkNeedMap(ParamContainer parm,boolean inOrOut2)
  {
	    if(null == parm.getServiceId())
		{
			return false;
		}
	  	if(null !=compositeService && busiSvcInfo == null)
		{
            if(!parm.getServiceId().equalsIgnoreCase(compositeService.getBaseinfo().getServiceId())){
            	return false;
            } 
		}
		else if( null !=compositeService && busiSvcInfo != null )
		{
            if(!parm.getServiceId().equalsIgnoreCase(busiSvcInfo.getId())){
            	return false;
            }          
		}

	  	if(inOrOut2)
	  	{
	  	   if(parm.getScope()==ParamScope.ServiceResponse)
	  	   {
           	return false;
           }     
	  	}
	  	else
	  	{
	  	   if(parm.getScope()==ParamScope.ServiceRequest)
	  	   {
	           	return false;
	        }     
	  	}
	  return true;
  }
}
