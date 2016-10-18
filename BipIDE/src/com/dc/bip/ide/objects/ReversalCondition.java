package com.dc.bip.ide.objects;

import java.io.Serializable;

public class ReversalCondition implements Serializable{
	
	private static final long serialVersionUID = -6307853179904903408L;
	
	private String retCode;
	private String retValue;
	private String desc;
	
	public ReversalCondition(String retCode, String retValue, String desc) {
		super();
		this.retCode = retCode;
		this.retValue = retValue;
		this.desc = desc;
	}
	
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetValue() {
		return retValue;
	}
	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}



}
