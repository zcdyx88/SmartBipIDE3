package com.dcits.smartbip.register;


import java.io.Serializable;
import java.util.List;

public class PublishParamMapFileDataBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<String> mapFiles;
	String paramFile;
	
	public PublishParamMapFileDataBean( List<String> mapFiles,String paramFile)
	{
		this.mapFiles = mapFiles;
		this.paramFile = paramFile;
	}

}
