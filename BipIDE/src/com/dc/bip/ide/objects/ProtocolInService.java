package com.dc.bip.ide.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProtocolInService implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2035594410070119664L;
	private String protocolType;
	private String protocolName;
	private String Ip;
	private String port;
	private String url;
	private String contactProtocolType;
	private String minThreads;
	private String maxThreads;
	private String acceptQueueSize;
	private String acceptSize;
	private String menuFilePath;
	private List<PramsInfo> paramsModels;
	private String iFilePath;
	private String overTime;
	private String readMethod;
	private String writeMethod;
	private List<PramsInfo>  headMsgDef;
	private String type ;
	
	
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOverTime() {
		return overTime;
	}

	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}

	public String getReadMethod() {
		return readMethod;
	}

	public void setReadMethod(String readMethod) {
		this.readMethod = readMethod;
	}

	public String getWriteMethod() {
		return writeMethod;
	}

	public void setWriteMethod(String writeMethod) {
		this.writeMethod = writeMethod;
	}

	public List<PramsInfo> getHeadMsgDef() {
		if(null == headMsgDef){
			synchronized (PramsInfo.class) {
				if(null == headMsgDef){
					headMsgDef = new ArrayList<PramsInfo>();
				}
			}
		}
		return headMsgDef;
	}

	public void setHeadMsgDef(List<PramsInfo> headMsgDef) {
		this.headMsgDef = headMsgDef;
	}

	public String getiFilePath() {
		return iFilePath;
	}

	public void setiFilePath(String iFilePath) {
		this.iFilePath = iFilePath;
	}

	public List<PramsInfo> getParamsModels() {
		if(null == paramsModels){
			synchronized (PramsInfo.class) {
				if(null == paramsModels){
					paramsModels = new ArrayList<PramsInfo>();
				}
			}
		}
		return paramsModels;
	}

	public void setParamsModels(List<PramsInfo> reversalConditions) {
		this.paramsModels = reversalConditions;
	}
	
	public String getMenuFilePath() {
		return menuFilePath;
	}
	public void setMenuFilePath(String menuFilePath) {
		this.menuFilePath = menuFilePath;
	}
	public String getProtocolType() {
		return protocolType;
	}
	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}
	public String getProtocolName() {
		return protocolName;
	}
	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}
	public String getIp() {
		return Ip;
	}
	public void setIp(String ip) {
		Ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContactProtocolType() {
		return contactProtocolType;
	}
	public void setContactProtocolType(String contactProtocolType) {
		this.contactProtocolType = contactProtocolType;
	}

	public String getMinThreads() {
		return minThreads;
	}
	public void setMinThreads(String minThreads) {
		this.minThreads = minThreads;
	}
	public String getMaxThreads() {
		return maxThreads;
	}
	public void setMaxThreads(String maxThreads) {
		this.maxThreads = maxThreads;
	}
	public String getAcceptQueueSize() {
		return acceptQueueSize;
	}
	public void setAcceptQueueSize(String acceptQueueSize) {
		this.acceptQueueSize = acceptQueueSize;
	}
	public String getAcceptSize() {
		return acceptSize;
	}
	public void setAcceptSize(String acceptSize) {
		this.acceptSize = acceptSize;
	}
	
}
