package com.dc.bip.ide.objects;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;

import com.dc.bip.ide.gef.model.ParamContainer;
import com.dc.bip.ide.objects.param.IParam;

public class BaseService  implements IService {
	private String name = "";
	private String  nickName="";
	private String objectType="";
	private String location="";
	private String category="";
	private Long lastModified ;
	private String description ="";
	private String impls ="";
	private String contributionId ="";
	private List<ConfigParam> paramList  = new ArrayList<ConfigParam>();
	private IFile baseFile;
	private IFile codeFile;
	
	public IFile getBaseFile() {
		return baseFile;
	}

	public IFile getCodeFile() {
		return codeFile;
	}

	public void setBaseFile(IFile baseFile) {
		this.baseFile = baseFile;
	}

	public void setCodeFile(IFile codeFile) {
		this.codeFile = codeFile;
	}

	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);  
	
	
      public void addPropertyChangeListener(PropertyChangeListener listener) {  
         listeners.addPropertyChangeListener(listener);  
     }  
      
     public void removePropertyChangeListener(PropertyChangeListener listener){  
    	 listeners.addPropertyChangeListener(listener);  
     } 

	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickName;
	}

	public BaseService(String name) {
		super();
		this.name = name;
	}

	public BaseService() {
		
	}

	public String getObjectType() {
		return objectType;
	}

	public String getLocation() {
		return location;
	}

	public String getCategory() {
		return category;
	}

	public Long getLastModified() {
		return lastModified;
	}

	public String getDescription() {
		return description;
	}

	public String getImpls() {
		return impls;
	}

	public String getContributionId() {
		return contributionId;
	}

	public List<ConfigParam> getParamList() {
		return paramList;
	}

	public void setName(String name) {
		listeners.firePropertyChange("name", this.name, this.name = name);
	}

	public void setNickName(String nickName) {
		listeners.firePropertyChange("nickName", this.nickName, this.nickName = nickName);
	}

	public void setObjectType(String objectType) {
		listeners.firePropertyChange("objectType", this.objectType, this.objectType = objectType);
	}

	public void setLocation(String location) {
		listeners.firePropertyChange("location", this.location, this.location = location);
	}

	public void setCategory(String category) {
		listeners.firePropertyChange("category", this.category, this.category = category);
	}

	public void setLastModified(Long lastModified) {
		listeners.firePropertyChange("lastModified", this.lastModified, this.lastModified = lastModified);
	}

	public void setDescription(String description) {
		listeners.firePropertyChange("description", this.description, this.description = description);
	}

	public void setImpls(String impls) {
		listeners.firePropertyChange("impls", this.impls, this.impls = impls);
	}

	public void setContributionId(String contributionId) {
		listeners.firePropertyChange("contributionId", this.contributionId, this.contributionId = contributionId);
	}

	public void setParamList(List<ConfigParam> paramList) {
		listeners.firePropertyChange("paramList", this.paramList, this.paramList = paramList);
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("name=").append(name).append(",impls=").append(impls).append(",description=").append(description)
		.append(",nickName=").append(nickName);
		return sb.toString();
	}

	@Override
	public ParamContainer getInParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParamContainer getOutParams() {
		// TODO Auto-generated method stub
		return null;
	}


	
}
