package com.dc.bip.ide.views.objects;

import java.io.Serializable;

import org.eclipse.core.resources.IFile;

import com.dc.bip.ide.objects.ProtocolInService;

public class ProtocolOutNode extends TreeNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4972246134759215922L;
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
	public ProtocolOutNode(String name) {		
		super(name);	
	}
}
