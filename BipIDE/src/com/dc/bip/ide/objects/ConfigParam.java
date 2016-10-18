package com.dc.bip.ide.objects;

public class ConfigParam {
	private String name;
	private String value;
	private String remark;
	
	public ConfigParam(String name, String value, String remark) {
		super();
		this.name = name;
		this.value = value;
		this.remark = remark;
	}
	
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	public String getRemark() {
		return remark;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}	
}
