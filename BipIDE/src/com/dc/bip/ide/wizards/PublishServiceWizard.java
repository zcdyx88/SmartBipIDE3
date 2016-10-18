package com.dc.bip.ide.wizards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.gef.model.Param;
import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.objects.ProtocolInService;
import com.dc.bip.ide.objects.ReversalCondition;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.PlatformUtils;
import com.dc.bip.ide.views.objects.BSNode;
import com.dc.bip.ide.views.objects.BSOperationNode;
import com.dc.bip.ide.views.objects.CompositeNode;
import com.dc.bip.ide.views.objects.ProtocolInNode;
import com.dc.bip.ide.views.objects.ProtocolOutNode;
import com.dc.bip.ide.views.objects.TreeNode;
import com.dcits.smartbip.register.PublishServiceProxy;

public class PublishServiceWizard extends Wizard implements INewWizard {

	private TreeNode treeNode;
	private final String pageName = "publishbusiSvcPage1";

	public PublishServiceWizard(TreeNode treeNode) {
		super();
		this.treeNode = treeNode;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addPages() {
		this.addPage(new PublishServiceWizardPage(pageName, treeNode.getProjectName()));
	}

	@Override
	public boolean performFinish() {
		String ip = ((PublishServiceWizardPage) this.getPage(pageName)).getIP();
		String jmxPort = ((PublishServiceWizardPage) this.getPage(pageName)).getJmxPort();
		String regPort = ((PublishServiceWizardPage) this.getPage(pageName)).getRegPort();
		if(checkServerInfo(ip, jmxPort, regPort)){
			try {
				if (treeNode instanceof BSNode) {
					publishBaseSvc(ip, Integer.parseInt(jmxPort), Integer.parseInt(regPort),
							((BSNode) treeNode).getBaseService());
					PlatformUtils.showInfo("组合服务发布", "基础服务发布成功!");
				} else if (treeNode instanceof BSOperationNode) {
					publishBusiSvc(ip, Integer.parseInt(jmxPort), Integer.parseInt(regPort),
							((BSOperationNode) treeNode).getBusiSvcInfo());
					PlatformUtils.showInfo("业务服务发布", "业务服务发布成功!");
				} else if (treeNode instanceof CompositeNode) {
					publishCompositeService(ip, Integer.parseInt(jmxPort), Integer.parseInt(regPort),
							((CompositeNode) treeNode).getCompSvc());
					PlatformUtils.showInfo("组合服务发布", "组合服务发布成功!");
				}else if (treeNode instanceof ProtocolInNode) {
					publishProtocolInService(ip, Integer.parseInt(jmxPort), Integer.parseInt(regPort),
							((ProtocolInNode) treeNode).getProtocolInService());
					PlatformUtils.showInfo("输入协议管理服务发布", "输入协议管理服务发布成功!");
				}
				else if (treeNode instanceof ProtocolOutNode) {
					publishProtocolOutService(ip, Integer.parseInt(jmxPort), Integer.parseInt(regPort),
							((ProtocolOutNode) treeNode).getProtocolInService());
					PlatformUtils.showInfo("输出协议管理服务发布", "输出协议管理服务发布成功!");
				}else {
					PlatformUtils.showInfo("组合服务发布", "发布不处理该类型!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				PlatformUtils.showInfo("组合服务发布", "服务发布失败!");
			}
		}else{
			PlatformUtils.showInfo("组合服务发布", "服务器配置异常!");
		}
		return true;
	}
	
	private boolean checkServerInfo(String ip, String jmxPort, String regPort){
		return null != ip && null != jmxPort && null != regPort && StringUtils.isNumeric(jmxPort) && StringUtils.isNumeric(regPort);
	}



	private void publishBaseSvc(String ip, int regPort, int rmtPort, BaseService baseService) {
		PublishServiceProxy proxy = new PublishServiceProxy();
		String codeContentStr = HelpUtil.readFile(baseService.getCodeFile().getLocation().toOSString());
		proxy.pubBaseService(ip, rmtPort, regPort, baseService.getName(), codeContentStr, baseService.getName(),
				baseService.getLocation(), baseService.getDescription());
	}
	
	private void publishProtocolInService(String ip, int regPort, int rmtPort, ProtocolInService protocolService) {
		PublishServiceProxy proxy = new PublishServiceProxy();
		protocolService.setType("client");
		proxy.pubProtocolService(ip, rmtPort, regPort,protocolService);
	}
	
	private void publishProtocolOutService(String ip, int regPort, int rmtPort, ProtocolInService protocolService) {
		PublishServiceProxy proxy = new PublishServiceProxy();
		protocolService.setType("server");
		proxy.pubProtocolService(ip, rmtPort, regPort,protocolService);
	}

	private void publishBusiSvc(String ip, int regPort, int rmtPort, BusiSvcInfo busiSvcInfo) {
		PublishServiceProxy proxy = new PublishServiceProxy();
		List<String> busiSvcFiles = busiSvcInfo.getLocalUris();
		String wsdlFile = null;
		String schemaFile = null;
		List<String> meataDataFileList = new ArrayList<String>();
		for (String fileName : busiSvcFiles) {
			if (null == wsdlFile) {
				if (fileName.endsWith(".wsdl")) {
					wsdlFile =HelpUtil.readFile(fileName);
					continue;
				}
			}
			if (null == schemaFile) {
				if (fileName.endsWith(busiSvcInfo.getId().trim() + ".xsd")) {
					schemaFile = HelpUtil.readFile(fileName);
					continue;
				}
			}

			if (fileName.endsWith(".xsd")) {
				meataDataFileList.add(HelpUtil.readFile(fileName));
			}
		}
		busiSvcInfo.getId();
		busiSvcInfo.getProtocolType();
		busiSvcInfo.getOppositeBusi();
		List<ReversalCondition> reversalConditions = busiSvcInfo.getReversalConditions();
		proxy.pubBuzzService(ip, rmtPort, regPort,  busiSvcInfo, wsdlFile, schemaFile, meataDataFileList);
	}

	private void publishCompositeService(String ip, int regPort, int rmtPort, CompositeService compSvc) {
		PublishServiceProxy proxy = new PublishServiceProxy();
		Map<String, String> flowFiles = null;
		String bpmnName = compSvc.getFlowname();
		if (StringUtils.isNotEmpty(bpmnName)) {
			File[] flowXmlFiles = HelpUtil.getFlowFiles(treeNode.getProjectName(), bpmnName);
			if (flowXmlFiles != null) {
				flowFiles = new HashMap<String, String>();
				for (File file : flowXmlFiles) {
					flowFiles.put(file.getName(), HelpUtil.readFile(file.getPath()));
				}
			}
		}
		proxy.pubCompositeService(ip, rmtPort, regPort, compSvc.getBaseinfo().getServiceId(),
				compSvc.getBaseinfo().getServiceName(), compSvc.getBaseinfo().getDescrition(), compSvc.getFlowFile());		
	  proxy.pubParamMapFile(ip, rmtPort, regPort, compSvc.getMapFiles(),getParamFile(compSvc));
	  
	}
	
	private String getParamFile(CompositeService compSvc)
	{
		List<Param> seesionParams = HelpUtil.readSessionParams(compSvc);
		Element root = DocumentHelper.createElement("params");  
        Document document = DocumentHelper.createDocument(root); 
        //增加session变量
        Element session = root.addElement("session");
        session.addAttribute("id", compSvc.getBaseinfo().getServiceId());
        for(Param tmpParam : seesionParams)
        {
        	Element param = session.addElement("param");
        	param.addAttribute("id",tmpParam.getName());
        	param.addAttribute("type",tmpParam.getType());
        	param.addAttribute("value",tmpParam.getValue());
        	param.addAttribute("desc",tmpParam.getDesc());
        }
        
      //增加global变量
        List<Param> globalParams = HelpUtil.readGlobalParams(compSvc);
        Element global = root.addElement("global");
        for(Param tmpParam : globalParams)
        {
        	Element param = global.addElement("param");
        	param.addAttribute("id",tmpParam.getName());
        	param.addAttribute("type",tmpParam.getType());
        	param.addAttribute("value",tmpParam.getValue());
        	param.addAttribute("desc",tmpParam.getDesc());
        }
        
    	try {
		    OutputFormat format = new OutputFormat("    ",true);  
	        format.setEncoding("UTF-8");//设置编码格式  
	        XMLWriter xmlWriter;

	        StringBuilder sb = new StringBuilder();
	        sb.append(BipConstantUtil.CompositePath).append(compSvc.getBaseinfo().getServiceId()).append("/param.xml");
	        IFile resource = compSvc.getProject().getFile(new Path(sb.toString()));
	        File xmlFile = resource.getLocation().toFile();
	        if(!xmlFile.exists())
	        {
	        	xmlFile.getParentFile().mkdirs();
	        	xmlFile.createNewFile();
	        }
			xmlWriter = new XMLWriter(new FileOutputStream(xmlFile),format);
	        xmlWriter.write(document);  
	        xmlWriter.close();
	        
	        return HelpUtil.readFile(xmlFile.getAbsolutePath());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}        
    	 return null;
	}
}
