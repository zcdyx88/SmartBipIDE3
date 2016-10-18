package com.dc.bip.ide.objects;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dc.bip.ide.common.model.ConfigFile;
import com.dc.bip.ide.gef.model.Param;
import com.dc.bip.ide.gef.model.ParamContainer;
import com.dc.bip.ide.gef.model.ParamScope;
import com.dc.bip.ide.gef.model.TitleModel;
import com.dc.bip.ide.util.PlatformUtils;

public class BusiSvcInfo implements Serializable, IService {
	private static final long serialVersionUID = 6582564855746179742L;

	private String id; // serviceId;
	private String name = "";
	private String projectName;
	private String protocolType = "";// 协议类型
	private String wsdlUrl = "";// wsdl地址
	private String nameSpace = "";// targetNamespace
	private String bindingName = "";
	private String operationName = "";
	private String actionName;
	private String version = "";
	private String endpoint = "";
	private String localUri = "";
	private String decscription = "";
	// 绑定协议
	private String protocolName;
	// 绑定拆组包模式
	private String packUnPackMode;
	private int timeOut = 1000;// 超时时间
	private List<String> localUris = new ArrayList<String>();
	private BusiSvcInfo oppositeBusi;
	private List<ReversalCondition> reversalConditions;
	private String sdaFilePath;// sda文件
	private String reqMsg;// 请求报文
	private String resMsg;// 响应报文
	private ParamContainer inParams;
	private ParamContainer outParams;
	private String fileFolderStr;
	private String busiPath;
	// 配置文件列表
	private List<ConfigFile> configs = new ArrayList<ConfigFile>();

	public void addConfig(ConfigFile configFile) {
		configs.add(configFile);
	}

	public List<ConfigFile> getConfigs() {
		return configs;
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

	public void persist(BusiSvcInfo svcInfo) {

	}

	public String getPackUnPackMode() {
		return packUnPackMode;
	}

	public void setPackUnPackMode(String packUnPackMode) {
		this.packUnPackMode = packUnPackMode;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getBusiPath() {
		return busiPath;
	}

	public void setBusiPath(String busiPath) {
		this.busiPath = busiPath;
	}

	public BusiSvcInfo() {

	}

	public List<ReversalCondition> getReversalConditions() {
		if (null == reversalConditions) {
			synchronized (BusiSvcInfo.class) {
				if (null == reversalConditions) {
					reversalConditions = new ArrayList<ReversalCondition>();
				}
			}
		}
		return reversalConditions;
	}

	public void setReversalConditions(List<ReversalCondition> reversalConditions) {
		this.reversalConditions = reversalConditions;
	}

	public String getDecscription() {
		return decscription;
	}

	public void setDecscription(String decscription) {
		this.decscription = decscription;
	}

	public BusiSvcInfo getOppositeBusi() {
		return oppositeBusi;
	}

	public String getId() {
		return id;
	}

	public void setOppositeBusi(BusiSvcInfo oppositeBusi) {
		this.oppositeBusi = oppositeBusi;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getWsdlUrl() {
		return wsdlUrl;
	}

	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getBindingName() {
		return bindingName;
	}

	public void setBindingName(String bindingName) {
		this.bindingName = bindingName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public List<String> getLocalUris() {
		return localUris;
	}

	public void setLocalUris(List<String> localUris) {
		this.localUris = localUris;
	}

	public String getSdaFilePath() {
		return sdaFilePath;
	}

	public void setSdaFilePath(String sdaFilePath) {
		this.sdaFilePath = sdaFilePath;
	}

	public String getReqMsg() {
		return reqMsg;
	}

	public void setReqMsg(String reqMsg) {
		this.reqMsg = reqMsg;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	public String getLocalUri() {
		return localUri;
	}

	public void setLocalUri(String localUri) {
		this.localUri = localUri;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ParamContainer getInParams() {
		return inParams;
	}

	public ParamContainer getOutParams() {
		return outParams;
	}

	public void setInParams(ParamContainer inParams) {
		this.inParams = inParams;
	}

	public void setOutParams(ParamContainer outParams) {
		this.outParams = outParams;
	}

	public String getFileFolderStr() {
		return fileFolderStr;
	}

	public void setFileFolderStr(String fileFolderStr) {
		this.fileFolderStr = fileFolderStr;
	}

	public void refreshParams() {
		ParamContainer pc = null;
		SAXReader sr = new SAXReader();
		// 获取SDA文件
		File sdaFile = getSDAFile();
		if (null != sdaFile) {
			try {
				Document doc = sr.read(sdaFile);
				Element topElement;
				Element requestBody = doc.getRootElement().element("request");
//				if (checkBody(requestBody)) {
//					topElement = (Element) requestBody.elements().get(0);
					topElement = requestBody;
					// 构造最上层的参数容器
					StringBuilder sb = new StringBuilder();
					sb.append("业务服务").append(getId()).append("_IN");
					ParamContainer topParamContainer = new ParamContainer(sb.toString());
					topParamContainer.addChild(new TitleModel(sb.toString()));
					topParamContainer.setServiceId(getId());
					topParamContainer.setScope(ParamScope.ServiceRequest);
					List topElementChildren = topElement.elements();
					for (Object tmpElement : topElementChildren) {
						topParamContainer.addChild(readParamContainer((Element) tmpElement, null, getId(),
								ParamScope.ServiceRequest, topElement.getName()));
					}
					setInParams(topParamContainer);
//				}

				Element responseBody = doc.getRootElement().element("response");
//				if (checkBody(responseBody)) {
//					topElement = (Element) responseBody.elements().get(0);
					topElement = responseBody;
					sb = new StringBuilder();
					sb.append("业务服务").append(getId()).append("_OUT");
					ParamContainer outParamContainer = new ParamContainer(sb.toString());
					outParamContainer.addChild(new TitleModel(sb.toString()));
					outParamContainer.setServiceId(getId());
					outParamContainer.setScope(ParamScope.ServiceResponse);
					topElementChildren = topElement.elements();
					for (Object tmpElement : topElementChildren) {
						outParamContainer.addChild(readParamContainer((Element) tmpElement, null, getId(),
								ParamScope.ServiceResponse, topElement.getName()));
					}
					setOutParams(outParamContainer);
//				}
			} catch (Exception e) {
				PlatformUtils.showInfo("数据节点解析出错", "服务[" + this.getId() + "]的服务定义文件解析失败！");
				e.printStackTrace();
			}

		}
	}

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

	private ParamContainer readParamContainer(Element root, String path, String serviceId, ParamScope type,
			String operationId) {
		String name = (root.getNamespacePrefix() == null || "".equalsIgnoreCase(root.getNamespacePrefix())) ? root.getName() :(root.getNamespacePrefix() + ":" + root.getName()); 
		ParamContainer pc = new ParamContainer(root.getNamespacePrefix() + ":" + root.getName());
		pc.setPath(path);
		pc.setServiceId(serviceId);
		pc.setScope(type);
		pc.setOperationId(operationId);
		pc.addChild(new TitleModel(name));
		List elements = root.elements();
		for (Object tmpObject : elements) {
			Element tmpElement = (Element) tmpObject;
			if (hasChildren(tmpElement)) {
				pc.addChild(readParamContainer(tmpElement,
						packagePath(path, name), serviceId, type,
						operationId));
			} else {
				String tmpName = (tmpElement.getNamespacePrefix() == null || "".equalsIgnoreCase(tmpElement.getNamespacePrefix())) ? tmpElement.getName() :(tmpElement.getNamespacePrefix() + ":" + tmpElement.getName()); 
				Param param = new Param(tmpName);
				param.setPath(packagePath(path, name));
				param.setServiceId(serviceId);
				param.setScope(type);
				param.setOperationId(operationId);
				pc.addChild(param);
			}
		}
		return pc;
	}

	private boolean hasChildren(Element element) {
		List elements = element.elements();
		if (null != elements && elements.size() > 0) {
			return true;
		}
		return false;
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
}
