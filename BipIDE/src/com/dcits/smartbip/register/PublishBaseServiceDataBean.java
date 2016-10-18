package com.dcits.smartbip.register;

import java.io.Serializable;

public class PublishBaseServiceDataBean  implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	 String code;
	 String id;
	 String location;
	 String desc;
	
	public PublishBaseServiceDataBean(String name, String code, String id, String location,String desc) {
		super();
		this.name = name;
		this.code = code;
		this.id = id;
		this.location = location;
		this.desc = desc;
	}	
	
}
