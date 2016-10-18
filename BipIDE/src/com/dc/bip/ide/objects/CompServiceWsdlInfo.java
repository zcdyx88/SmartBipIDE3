package com.dc.bip.ide.objects;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
/**
 * 
 * @author pangzt
 * 组合服务的wsdl文件信息
 */
public class CompServiceWsdlInfo  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//协议类型
	private String protocoltype;
	//url
	private String url;
	//命名空间
	private String namespace;
	//绑定
	private String bind;
	//结束节点
	private String endpoint;
	//操作
	private String operation;
	//文件路径
	private String filepath;
	private String actionName;
	
	
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getProtocoltype() {
		return protocoltype;
	}
	public void setProtocoltype(String protocoltype) {
		this.protocoltype = protocoltype;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getBind() {
		return bind;
	}
	public void setBind(String bind) {
		this.bind = bind;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	@Override
	public String toString() {
		return "CompServiceWsdlInfo [protocoltype=" + protocoltype + ", url=" + url + ", namespace=" + namespace
				+ ", bind=" + bind + ", endpoint=" + endpoint + ", operation=" + operation + ", filepath=" + filepath
				+ "]";
	}
	
}
