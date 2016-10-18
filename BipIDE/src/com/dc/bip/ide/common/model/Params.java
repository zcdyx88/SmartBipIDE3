package com.dc.bip.ide.common.model;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dc.bip.ide.util.XMLUtils;

public class Params implements Persistable<Params>{
	private Map<String, String> params;
	private Map<String, Params> childParams;
	private String path;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Params() {
		// TODO Auto-generated constructor stub
		params = new HashMap<String, String>();
		this.childParams = new HashMap<String, Params>();
	}
	
	public void remove(String id){
		params.remove(id);
	}
	
	public Params(String path) {
		super();
		params = new HashMap<String, String>();
		this.childParams = new HashMap<String, Params>();
		this.path = path;
	}
	
	public Params getChild(String childId){
		return childParams.get(childId);
	}
	
	public void setChild(String childName,Params childParams){
		this.childParams.put(childName, childParams);
	}

	public String get(String key){
		return params.get(key);
	}
	
	public void set(String key,String value){
		params.put(key, value);
	}
	
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Map<String, Params> getChildParams() {
		return childParams;
	}

	public void setChildParams(Map<String, Params> childParams) {
		this.childParams = childParams;
	}
	@Override
	public Params unPersist() {
		Document document = XMLUtils.getDocFromFile(new File(path));
		Element element = document.getRootElement();
		List<Element> childElements = element.elements();
		for(Element childElement : childElements){
			unMarshallParams(childElement,this);
		}
		return this;
	}
	
	private void unMarshallParams(Element childElement,Params param){
		List<Element> subChildElements = childElement.elements();
		if(null != subChildElements && subChildElements.size() > 0){
			Params childParams = new Params();
			for(Element subChildElement : subChildElements){
				unMarshallParams(subChildElement,childParams);
			}
			param.getChildParams().put(childElement.getName(), childParams);
		}else{
			param.set(childElement.getName(), childElement.getText());
		}
	}

	@Override
	public void persist() {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("params");
		processParam(this, root);
		XMLUtils.saveDocument(doc, new File(path));
	}
	
	public void processParam(Params p, Element e){
		Map<String, String> params = p.getParams();
		for(Map.Entry<String, String> entry : params.entrySet()){
			Element paramElement = e.addElement(entry.getKey());
			paramElement.addText(entry.getValue());
		}
		Map<String, Params> childParams = p.getChildParams();
		for(Map.Entry<String, Params> entry : childParams.entrySet()){
			processParam(entry.getValue(), e.addElement(entry.getKey()));
		}
	}
}
