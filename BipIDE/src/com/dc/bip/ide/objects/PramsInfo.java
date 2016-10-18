package com.dc.bip.ide.objects;

import java.io.Serializable;

public class PramsInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String paramKey;
	private String paramValue;
	
	public PramsInfo(String key, String value) {
		super();
		this.paramKey = key;
		this.paramValue = value;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	


}
