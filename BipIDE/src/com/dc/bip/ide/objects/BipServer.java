package com.dc.bip.ide.objects;

import java.io.Serializable;

public class BipServer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3746593196299930204L;
	private String id;
	private String ip;
	private String jmxport;
	private String regport;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getJmxport() {
		return jmxport;
	}
	public void setJmxport(String jmxport) {
		this.jmxport = jmxport;
	}
	public String getRegport() {
		return regport;
	}
	public void setRegport(String regport) {
		this.regport = regport;
	}
	
}
