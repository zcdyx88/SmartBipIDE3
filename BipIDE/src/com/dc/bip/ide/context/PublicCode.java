package com.dc.bip.ide.context;

import java.io.Serializable;

public class PublicCode implements Serializable{
	private static final long serialVersionUID = 1387208666451248216L;
	private String id;
	private String name;
	private String value;
	private String desc;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "PublicContext [id=" + id + ", name=" + name + ", value=" + value + ", desc=" + desc + "]";
	}
	
}
