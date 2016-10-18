package com.dc.bip.ide.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.reficio.ws.builder.SoapBuilder;
import org.reficio.ws.builder.core.Wsdl;

import com.dc.bip.ide.views.objects.BSBindingNode;

public class BusiSvc implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String url;
	private String localPath;
	
	
	public BusiSvc(){
		
	}
	
	public BusiSvc(String url){
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getName(){
		Wsdl wsdl = getWsdl();
		String serviceId = "";
		String[] strs = url.split("/");
		String str = strs[strs.length - 1];
		if (StringUtils.isNotEmpty(str)) {
			if (str.contains(".")) {
				serviceId = str.substring(0, str.indexOf("."));
			}
		}
		return serviceId;
	}
	
	public Wsdl getWsdl(){
		try{
			Wsdl wsdl = Wsdl.parse(url);
			return wsdl;
		}catch(Exception e){
			e.printStackTrace();
			Wsdl wsdl = Wsdl.parse(localPath);//从本地读取
			return wsdl;
		}
	}
	
	public Definition getWsdlDef(){
		try{
			WSDLFactory factory = WSDLFactory.newInstance();
			WSDLReader reader = factory.newWSDLReader();
			reader.setFeature("javax.wsdl.verbose", true);
			reader.setFeature("javax.wsdl.importDocuments", true);
			Definition def = reader.readWSDL(url);
			if(null == def){
				def = reader.readWSDL(localPath);
			}
			return def;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	//获取wsdl所有banding节点
	public List<BSBindingNode> getBindingNodes(){
		List<BSBindingNode> list = new ArrayList<BSBindingNode>();
		List<QName> bandings = getWsdl().getBindings();
		for(QName qname : bandings){
			SoapBuilder soapBuilder = getWsdl().getBuilder(qname);
			BSBindingNode bnode = new BSBindingNode(soapBuilder);
			list.add(bnode);
			
		}
		return list;
		
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	
	public String getFileName(){
		String[] strs = url.split("\\/");
		String str = strs[strs.length - 1];
		return str;
	}
}
