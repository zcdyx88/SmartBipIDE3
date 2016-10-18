package com.dc.bip.ide.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;

public class ExpressionService  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2035594410070119664L;
	
	private String expressionID;
	
	private String expressionName;
	
	private String description ="";
	
	private String impls ="";

	private List<PramsInfo> paramsModels;
	
	private String localFilePath;
	
	private String menuFilePath;

	private String name = "";
	private String  nickName="";
	private String objectType="";
	private String location="";
	private String category="";
	private Long lastModified ;
	private String contributionId ="";
	private List<ConfigParam> paramList  = new ArrayList<ConfigParam>();
	private IFile baseFile;
	private IFile codeFile;
	public String getExpressionID() {
		return expressionID;
	}
	public void setExpressionID(String expressionID) {
		this.expressionID = expressionID;
	}
	public String getExpressionName() {
		return expressionName;
	}
	public void setExpressionName(String expressionName) {
		this.expressionName = expressionName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImpls() {
		return impls;
	}
	public void setImpls(String impls) {
		this.impls = impls;
	}
	public List<PramsInfo> getParamsModels() {
		if(paramsModels == null){
			paramsModels = new ArrayList<PramsInfo>();
		}
		return paramsModels;
	}
	public void setParamsModels(List<PramsInfo> paramsModels) {
		this.paramsModels = paramsModels;
	}
	public String getLocalFilePath() {
		return localFilePath;
	}
	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}
	public String getMenuFilePath() {
		return menuFilePath;
	}
	public void setMenuFilePath(String menuFilePath) {
		this.menuFilePath = menuFilePath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Long getLastModified() {
		return lastModified;
	}
	public void setLastModified(Long lastModified) {
		this.lastModified = lastModified;
	}
	public String getContributionId() {
		return contributionId;
	}
	public void setContributionId(String contributionId) {
		this.contributionId = contributionId;
	}
	public List<ConfigParam> getParamList() {
		return paramList;
	}
	public void setParamList(List<ConfigParam> paramList) {
		this.paramList = paramList;
	}
	public IFile getBaseFile() {
		return baseFile;
	}
	public void setBaseFile(IFile baseFile) {
		this.baseFile = baseFile;
	}
	public IFile getCodeFile() {
		return codeFile;
	}
	public void setCodeFile(IFile codeFile) {
		this.codeFile = codeFile;
	}
	
	
	
	

	
}
