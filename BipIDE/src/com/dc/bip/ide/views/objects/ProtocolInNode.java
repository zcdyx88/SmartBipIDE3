package com.dc.bip.ide.views.objects;

import java.io.Serializable;

import org.eclipse.core.resources.IFile;

import com.dc.bip.ide.objects.ProtocolInService;

public class ProtocolInNode extends TreeNode implements Serializable{
	private static final long serialVersionUID = 1L;
	private ProtocolInService protocolInService;
	private IFile  resource;
	
	public IFile getResource() {
		return resource;
	}
	public void setResource(IFile resource) {
		this.resource = resource;
	}
	
	public ProtocolInService getProtocolInService() {
		return protocolInService;
	}

	public void setProtocolInService(ProtocolInService protocolInService) {
		this.protocolInService = protocolInService;
	}
	
	public ProtocolInNode(String name) {		
		super(name);	
	}
	

}
