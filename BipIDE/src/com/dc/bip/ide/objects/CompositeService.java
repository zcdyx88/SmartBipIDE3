package com.dc.bip.ide.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

import com.dc.bip.ide.common.model.ConfigFile;
import com.dc.bip.ide.context.PublicCode;
import com.dc.bip.ide.gef.model.Param;
import com.dc.bip.ide.gef.model.ParamContainer;
import com.dc.bip.ide.gef.model.ParamScope;
import com.dc.bip.ide.gef.model.TitleModel;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.views.objects.BSOperationNode;

/**
 * 
 * @author pangzt 组合服务信息
 */
public class CompositeService implements Serializable, IService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 组合服务的基本信息
	private CompServiceBaseInfo baseinfo; 
	// 绑定协议名称
	private String protocolName;
	// 数据适配
	private String packUnPackMode;
	
	// 组合服务的wsdl信息
	private CompServiceWsdlInfo wsdlinfo;
	private CompServiceSDAInfo sdafo;
	private String flowname;
	private List<PublicCode> codes;
	private ParamContainer inParams;
	private ParamContainer outParams;
	private ParamContainer sessionParams;
	private String project;
	private List<BusiSvcInfo> busiSvcNodes = new ArrayList<BusiSvcInfo>();
	private boolean isNEW;
	private String reqMsg;
	private String rspMsg;
	// .composite文件目录
	private String compositePath;
	// 文件导入从导入文件中取得
	private String serviceFolderName;
	// 配置文件列表
	private List<ConfigFile> configs = new ArrayList<ConfigFile>();
	
	public void addConfig(ConfigFile configFile){ 
		configs.add(configFile);
	}
	
	public List<ConfigFile> getConfigs(){
		return configs;
	}
	
	public void persist(BusiSvcInfo svcInfo){
		
	}

	public String getServiceFolderName() {
		return serviceFolderName;
	}

	public void setServiceFolderName(String serviceFolderName) {
		this.serviceFolderName = serviceFolderName;
	}

	public String getCompositePath() {
		return compositePath;
	}

	public void setCompositePath(String compositePath) {
		this.compositePath = compositePath;
	}

	public CompositeService() {
		this.isNEW = false; // 添加初始化非新建标志
	}

	public boolean is_NEW() {
		return isNEW;
	}

	public void setNEW(boolean isNEW) {
		this.isNEW = isNEW;
	}

	public CompServiceBaseInfo getBaseinfo() {
		return baseinfo;
	}

	public void setBaseinfo(CompServiceBaseInfo baseinfo) {
		this.baseinfo = baseinfo;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getPackUnPackMode() {
		return packUnPackMode;
	}

	public void setPackUnPackMode(String packUnPackMode) {
		this.packUnPackMode = packUnPackMode;
	}

	public CompServiceWsdlInfo getWsdlinfo() {
		return wsdlinfo;
	}

	public void setWsdlinfo(CompServiceWsdlInfo wsdlinfo) {
		this.wsdlinfo = wsdlinfo;
	}

	public CompServiceSDAInfo getSdafo() {
		return sdafo;
	}

	public void setSdafo(CompServiceSDAInfo sdafo) {
		this.sdafo = sdafo;
	}

	public String getFlowname() {
		return flowname;
	}

	public void setFlowname(String flowname) {
		this.flowname = flowname;
	}

	@Override
	public String toString() {
		return "CompsiteService [baseinfo=" + baseinfo + ", wsdlinfo=" + wsdlinfo + ", sdafo=" + sdafo + "]";
	}

	public List<PublicCode> getCodes() {
		return codes;
	}

	public void setCodes(List<PublicCode> codes) {
		this.codes = codes;
	}

	public ParamContainer getInParams() {
		return inParams;
	}

	public ParamContainer getOutParams() {
		return outParams;
	}

	public ParamContainer getSessionParams() {
		return sessionParams;
	}

	public void setInParams(ParamContainer inParams) {
		this.inParams = inParams;
	}

	public void setOutParams(ParamContainer outParams) {
		this.outParams = outParams;
	}

	public void setSessionParams(ParamContainer sessionParams) {
		this.sessionParams = sessionParams;
	}

	public String getReqMsg() {
		return reqMsg;
	}

	public void setReqMsg(String reqMsg) {
		this.reqMsg = reqMsg;
	}

	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}

	public List<BusiSvcInfo> getBusiSvcNodesFromFlow() {
		/*
		 * setBusiSvcNodes(this.mockGetChildNodes()); return
		 * this.getBusiSvcNodes();
		 */

		List<BusiSvcInfo> busiSvcList = new ArrayList<BusiSvcInfo>();
		if (null == this.getFlowname() || this.getFlowname().length() == 0) {
			System.err.println("未绑定业务流程");
			return new ArrayList<BusiSvcInfo>();
		} else if (null == getProject()) {
			System.err.println("未设置工程");
			return new ArrayList<BusiSvcInfo>();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(BipConstantUtil.FlowPath).append(this.getFlowname());
		IFile flowFile = this.getProject().getFile(sb.toString());
		SAXReader sr = new SAXReader();
		try {
			Document doc = sr.read(flowFile.getLocation().toFile());
			Element root = doc.getRootElement();
			List<String> serviceList = getBusiServiceIds(root.element("process"));

			for (String tmpStr : serviceList) {
				BusiSvcInfo tmpBusi = getBusiSvc(tmpStr);
				if (null != tmpBusi) {
					if (!exist(busiSvcList, tmpBusi)) {
						tmpBusi.refreshParams();
						busiSvcList.add(tmpBusi);
						// 添加反交易
						if (null != tmpBusi.getOppositeBusi()) {
							if (!exist(busiSvcList, tmpBusi.getOppositeBusi())) {
								tmpBusi.getOppositeBusi().refreshParams();
								busiSvcList.add(tmpBusi.getOppositeBusi());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setBusiSvcNodes(busiSvcList);
		return busiSvcList;
	}

	private boolean exist(List<BusiSvcInfo> busiList, BusiSvcInfo busi) {
		boolean existFlag = false;
		for (BusiSvcInfo tmpBS : busiList) {
			if (tmpBS.getId().equalsIgnoreCase(busi.getId())) {
				existFlag = true;
				break;
			}
		}
		return existFlag;
	}

	public List<BusiSvcInfo> getBusiSvcNodes() {
		return busiSvcNodes;
	}

	public void setBusiSvcNodes(List<BusiSvcInfo> busiSvcNodes) {
		this.busiSvcNodes = busiSvcNodes;
	}

	private BusiSvcInfo getBusiSvc(String serviceId) {
		BusiSvcInfo busiSvcInfo = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(BipConstantUtil.BusiSvcPath).append(serviceId).append(".busi");
			String pathStr = sb.toString();
			IFile busiFile = getProject().getFile(new Path(pathStr));
			if (!busiFile.exists()) {
				return null;
			}
			FileInputStream in;
			in = new FileInputStream(new File(busiFile.getLocation().toOSString()));
			ObjectInputStream objectInputStream = new ObjectInputStream(in);
			BSOperationNode node = (BSOperationNode) objectInputStream.readObject();
			busiSvcInfo = node.getBusiSvcInfo();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return busiSvcInfo;
	}

	private List<String> getBusiServiceIds(Element root) {
		List<String> list = new ArrayList<String>();

		List serviceTasks = root.elements("serviceTask");
		for (Object tmpObject : serviceTasks) {
			Element serviceTask = (Element) tmpObject;
			String extensionId = serviceTask.attribute("extensionId").getText();
			if (null != extensionId && extensionId.equals("org.activiti.designer.bip.task.BusinessTask")) {
				Element extensionElements = (Element) serviceTask.element("extensionElements");
				if (null != extensionElements) {
					if (null != extensionElements.element("serviceId")) {
						String serviceId = serviceTask.element("extensionElements").element("serviceId").getText();
						list.add(serviceId);
					}
				}
			}
		}

		List subProcesses = root.elements("subProcess");
		for (Object tmpObject2 : subProcesses) {
			Element subProcess = (Element) tmpObject2;
			list.addAll(getBusiServiceIds(subProcess));
		}

		List transactions = root.elements("transaction");
		for (Object tmpObject3 : transactions) {
			Element transaction = (Element) tmpObject3;
			list.addAll(getBusiServiceIds(transaction));
		}
		return list;
	}

	public IProject getProject() {
		if (project == null || project.length() == 0) {
			return null;
		}
		return ResourcesPlugin.getWorkspace().getRoot().getProject(project);
	}

	public void setProject(IProject project) {
		this.project = project.getName();
	}

	/*
	 * private List<BusiSvcInfo> mockGetChildNodes() { List<BusiSvcInfo>
	 * childNodes = new ArrayList<BusiSvcInfo>(); BusiSvcInfo busiSvc; Param
	 * param; for (int i = 0;i<2;i++) { busiSvc = new BusiSvcInfo();
	 * busiSvc.setId("BusiSvc"+i); ParamContainer inServiceParams = new
	 * ParamContainer("BusiSvc"+i+"_IN_Req3002101000103");
	 * inServiceParams.addChild(new TitleModel(inServiceParams.getName()));
	 * ParamContainer outServiceParams = new
	 * ParamContainer("BusiSvc"+i+"_OUT_Rsp3002100000102");
	 * outServiceParams.addChild(new TitleModel(outServiceParams.getName()));
	 * 
	 * ParamContainer container =new ParamContainer("ReqSysHead");
	 * container.addChild(new TitleModel(container.getName(),10)); param =new
	 * Param("ChannelID");
	 * param.setPath("Req3002101000103.ReqSysHead.ChannelID");
	 * param.setServiceId(busiSvc.getId());
	 * param.setScope(ParamScope.ServiceRequest); container.addChild( param);
	 * param =new Param("LegOrgID");
	 * param.setPath("Req3002101000103.ReqSysHead.LegOrgID");
	 * param.setServiceId(busiSvc.getId()); container.addChild( param);
	 * param.setScope(ParamScope.ServiceRequest); param =new Param("ReqDate");
	 * param.setPath("Req3002101000103.ReqSysHead.ReqDate");
	 * param.setServiceId(busiSvc.getId());
	 * param.setScope(ParamScope.ServiceRequest); container.addChild( param);
	 * inServiceParams.addChild(container);
	 * 
	 * container =new ParamContainer("ReqAppHead"); container.addChild(new
	 * TitleModel(container.getName(),10)); param =new Param("ChannelID");
	 * param.setPath("Req3002101000103.ReqAppHead.ChannelID");
	 * param.setServiceId(busiSvc.getId());
	 * param.setScope(ParamScope.ServiceRequest); container.addChild( param);
	 * param =new Param("LegOrgID");
	 * param.setPath("Req3002101000103.ReqAppHead.LegOrgID");
	 * param.setServiceId(busiSvc.getId());
	 * param.setScope(ParamScope.ServiceRequest); container.addChild( param);
	 * param =new Param("ReqDate");
	 * param.setPath("Req3002101000103.ReqAppHead.ReqDate");
	 * param.setServiceId(busiSvc.getId());
	 * param.setScope(ParamScope.ServiceRequest); container.addChild( param);
	 * inServiceParams.addChild(container);
	 * 
	 * container =new ParamContainer("RspSysHead"); container.addChild(new
	 * TitleModel(container.getName(),10)); param =new Param("ChannelID");
	 * param.setPath("Req3002101000103.RspSysHead.ChannelID");
	 * param.setServiceId(busiSvc.getId());
	 * param.setScope(ParamScope.ServiceResponse); container.addChild( param);
	 * param =new Param("LegOrgID");
	 * param.setPath("Req3002101000103.RspSysHead.LegOrgID");
	 * param.setServiceId(busiSvc.getId());
	 * param.setScope(ParamScope.ServiceResponse); container.addChild( param);
	 * param =new Param("ReqDate");
	 * param.setPath("Req3002101000103.RspSysHead.ReqDate");
	 * param.setServiceId(busiSvc.getId());
	 * param.setScope(ParamScope.ServiceResponse); container.addChild( param);
	 * outServiceParams.addChild(container);
	 * 
	 * container =new ParamContainer("RspAppHead"); container.addChild(new
	 * TitleModel(container.getName(),10)); param =new Param("ChannelID");
	 * param.setPath("Req3002101000103.RspAppHead.ChannelID");
	 * param.setServiceId(busiSvc.getId());
	 * param.setScope(ParamScope.ServiceResponse); container.addChild( param);
	 * param =new Param("LegOrgID");
	 * param.setPath("Req3002101000103.RspAppHead.LegOrgID");
	 * param.setServiceId(busiSvc.getId());
	 * param.setScope(ParamScope.ServiceResponse); container.addChild( param);
	 * param =new Param("ReqDate");
	 * param.setPath("Req3002101000103.RspAppHead.ReqDate");
	 * param.setServiceId(busiSvc.getId());
	 * param.setScope(ParamScope.ServiceResponse); container.addChild( param);
	 * outServiceParams.addChild(container);
	 * 
	 * busiSvc.setInParams(inServiceParams);
	 * busiSvc.setOutParams(outServiceParams); childNodes.add(busiSvc); } return
	 * childNodes; }
	 */

	private boolean checkBody(Element body) {
		List requests = body.elements();

		if (null == requests) {
			System.err.println("请求body为空");
			return false;
		} else if (requests.size() != 1) {
			System.err.println("请求body数大于1");
			return false;
		}
		return true;
	}

	/*
	 * private void setBusiParams(String sdaFile, BusiSvcInfo busi) {
	 * ParamContainer pc = null; SAXReader sr = new SAXReader(); try { Document
	 * doc = sr.read(new File(sdaFile)); Element topElement; Element requestBody
	 * = doc.getRootElement().element("request").element("Envelope").element(
	 * "Body"); if (checkBody(requestBody)) {
	 * 
	 * topElement = (Element) requestBody.elements().get(0); // 构造最上层的参数容器
	 * StringBuilder sb = new StringBuilder();
	 * sb.append("业务服务").append(busi.getId()).append("_IN"); ParamContainer
	 * topParamContainer = new ParamContainer(sb.toString());
	 * topParamContainer.addChild(new TitleModel(sb.toString()));
	 * topParamContainer.setServiceId(busi.getId());
	 * topParamContainer.setScope(ParamScope.ServiceRequest); List
	 * topElementChildren = topElement.elements(); for (Object tmpElement :
	 * topElementChildren) {
	 * topParamContainer.addChild(readParamContainer((Element) tmpElement, null,
	 * busi.getId(), ParamScope.ServiceRequest, topElement.getName())); }
	 * busi.setInParams(topParamContainer); }
	 * 
	 * Element responseBody =
	 * doc.getRootElement().element("response").element("Envelope").element(
	 * "Body"); if (checkBody(responseBody)) { topElement = (Element)
	 * responseBody.elements().get(0); StringBuilder sb = new StringBuilder();
	 * sb.append("业务服务").append(busi.getId()).append("_OUT"); ParamContainer
	 * topParamContainer = new ParamContainer(sb.toString());
	 * topParamContainer.addChild(new TitleModel(sb.toString()));
	 * topParamContainer.setServiceId(busi.getId());
	 * topParamContainer.setScope(ParamScope.ServiceResponse); List
	 * topElementChildren = topElement.elements(); for (Object tmpElement :
	 * topElementChildren) {
	 * topParamContainer.addChild(readParamContainer((Element) tmpElement, null,
	 * busi.getId(), ParamScope.ServiceResponse, topElement.getName())); }
	 * busi.setOutParams(topParamContainer); } } catch (DocumentException e) {
	 * e.printStackTrace(); } }
	 */

	private ParamContainer readParamContainer(Element root, String path, String serviceId, ParamScope type,
			String operationId) {
		ParamContainer pc = new ParamContainer(root.getQualifiedName());
		pc.setPath(path);
		pc.setServiceId(serviceId);
		pc.setScope(type);
		pc.setOperationId(operationId);

		pc.addChild(new TitleModel(root.getQualifiedName()));
		List elements = root.elements();
		for (Object tmpObject : elements) {
			Element tmpElement = (Element) tmpObject;
			if (hasChildren(tmpElement)) {
				pc.addChild(readParamContainer(tmpElement,
						packagePath(path, root.getQualifiedName()), serviceId, type,
						operationId));
			} else {
				Param param = new Param( tmpElement.getQualifiedName());
				param.setPath(packagePath(path,  root.getQualifiedName()));
				param.setServiceId(serviceId);
				param.setScope(type);
				param.setOperationId(operationId);
				pc.addChild(param);
			}
		}
		return pc;
	}

	private String packagePath(String parentPath, String name) {
		StringBuilder sb = new StringBuilder();
		if (null == parentPath || parentPath.length() == 0) {
			sb.append(name);
		} else {
			sb.append(parentPath).append("/").append(name);
		}
		return sb.toString();
	}

	private boolean hasChildren(Element element) {
		List elements = element.elements();
		if (null != elements && elements.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取SDA文件
	 * 
	 * @return
	 */
	public File getSDAFile() {
		if (null != configs) {
			for (ConfigFile configFile : getConfigs()) {
				if (BusiSvcInfoConstants.SDA_TYPE.equalsIgnoreCase(configFile.getType())) {
					return configFile.getLocalFile();
				}
			}
		}
		return null;
	}
	

	public void refreshParams() {
		try {
			SAXReader sr = new SAXReader();
			Document doc = sr.read(getSDAFile());
			Element topElement;
			Element requestBody = doc.getRootElement().element("request");
//			if (checkBody(requestBody)) {

//				topElement = (Element) requestBody.elements().get(0);
			topElement = requestBody;
				// 构造最上层的参数容器
				StringBuilder sb = new StringBuilder();
				sb.append("组合服务").append(getBaseinfo().getServiceId()).append("_IN");
				ParamContainer topParamContainer = new ParamContainer(sb.toString());
				topParamContainer.addChild(new TitleModel(sb.toString()));
				topParamContainer.setServiceId(getBaseinfo().getServiceId());
				topParamContainer.setScope(ParamScope.ServiceRequest);
				List topElementChildren = topElement.elements();
				for (Object tmpElement : topElementChildren) {
					topParamContainer.addChild(readParamContainer((Element) tmpElement, null,
							getBaseinfo().getServiceId(), ParamScope.ServiceRequest, topElement.getName()));
				}
				setInParams(topParamContainer);
//			}

			Element responseBody = doc.getRootElement().element("response");
//			if (checkBody(responseBody)) {
//				topElement = (Element) responseBody.elements().get(0);
				topElement = responseBody;
				sb = new StringBuilder();
				sb.append("组合服务").append(getBaseinfo().getServiceId()).append("_OUT");
				topParamContainer = new ParamContainer(sb.toString());
				topParamContainer.addChild(new TitleModel(sb.toString()));
				topParamContainer.setServiceId(getBaseinfo().getServiceId());
				topParamContainer.setScope(ParamScope.ServiceResponse);
				topElementChildren = topElement.elements();
				for (Object tmpElement : topElementChildren) {
					topParamContainer.addChild(readParamContainer((Element) tmpElement, null,
							getBaseinfo().getServiceId(), ParamScope.ServiceResponse, topElement.getName()));
				}
				setOutParams(topParamContainer);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getMapFiles() {
		List<String> mapFileList = new ArrayList<String>();
		FilenameFilter mapFileFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if (str.equals(".xml")) {
						return true;
					}
				}
				return false;
			}
		};
		StringBuilder sb = new StringBuilder();
		sb.append(BipConstantUtil.CompositePath).append(getBaseinfo().getServiceId()).append("/map");
		IFile mapFile = getProject().getFile(new Path(sb.toString()));
		File mapFilesContainer = new File(mapFile.getLocation().toOSString());
		if (mapFilesContainer.exists()) {
			String[] mapFiles = mapFilesContainer.list(mapFileFilter);
			for (String tmpMapFilePath : mapFiles) {
				mapFileList.add(HelpUtil.readFile(mapFile.getLocation().toOSString() + "/" + tmpMapFilePath));
			}
		}
		return mapFileList;
	}
	
	public String getFlowFile(){
		String processName = getFlowname().substring(0, getFlowname().length() -5);
		IFile processFile = getProject().getFile(new Path(BipConstantUtil.FlowPath +processName + File.separator + processName+ ".xml"));
		File localFile = new File(processFile.getLocation().toOSString());
		return HelpUtil.readFile(localFile.getAbsolutePath());
	}

}
