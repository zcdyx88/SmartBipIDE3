package com.dc.bip.ide.common.model;

import java.io.File;
import java.io.Serializable;

import com.dc.bip.ide.util.FileUtils;

public class ConfigFile implements Serializable{
	
	private String name;
	private String path;
	private String type;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean delete(){
		File file = FileUtils.getLocalResource(path);
		if(null != file){
			return file.delete();
		}
		return false;
	}
	
	/**
	 * 返回该配置的本地文件
	 * @return
	 */
	public File getLocalFile(){
		if(null != this.path){
			return FileUtils.getLocalResource(path);
		}
		return null;
	}

}
