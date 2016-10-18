package com.dc.bip.ide.objects;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialArray;

import org.eclipse.core.resources.IFile;
/**
 * 
 * @author pangzt
 * 组合服务的服务定义结构文件信息
 */
public class CompServiceSDAInfo  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String filepath;

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	@Override
	public String toString() {
		return "CompServiceSDAInfo [filepath=" + filepath + "]";
	}
	
}
