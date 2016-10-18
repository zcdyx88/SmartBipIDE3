package com.dc.bip.ide.views.objects;

import java.util.List;

import javax.wsdl.Definition;

import org.reficio.ws.builder.SoapBuilder;
import org.reficio.ws.builder.SoapOperation;

public class BSBindingNode  extends TreeNode{
	
	SoapBuilder soapBuilder;
	Definition def;
	
	public BSBindingNode(SoapBuilder soapBuilder){
		super(soapBuilder.getBindingName().getLocalPart());
		this.soapBuilder = soapBuilder;
		initChildren();
		
	}
	public BSBindingNode(String nodeName) {
		super(nodeName);
		// TODO Auto-generated constructor stub
	}
	
	public void initChildren(){
		List<SoapOperation> sos = soapBuilder.getOperations();
		for(SoapOperation so : sos){
			BSOperationNode opNode = new BSOperationNode(so.getOperationName());
			this.getChildren().add(opNode);
		}
	}
	public SoapBuilder getSoapBuilder() {
		return soapBuilder;
	}
	public void setSoapBuilder(SoapBuilder soapBuilder) {
		this.soapBuilder = soapBuilder;
	}
	
	

}
