package com.dc.bip.ide.objects;

import java.io.Serializable;
/**
 * 
 * @author pangzt
 * 组合服务基本信息
 */
public class CompServiceBaseInfo   implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//服务ID
	private String serviceId;
	//服务名称、从页面中取得
	private String serviceName;
	//服务描述
	private String descrition;
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getDescrition() {
		return descrition;
	}
	public void setDescrition(String descrition) {
		this.descrition = descrition;
	}
	@Override
	public String toString() {
		return "CompServiceBaseInfo [serviceId=" + serviceId + ", serviceName=" + serviceName + ", descrition="
				+ descrition + "]";
	}
	
}
