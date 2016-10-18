package com.dc.bip.ide.views.objects;

import java.io.Serializable;

import com.dc.bip.ide.objects.ProtocolInService;

public class ProtocolNode extends TreeNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProtocolInService proService;
	
	public ProtocolInService getProService() {
		return proService;
	}

	public void setProService(ProtocolInService proService) {
		this.proService = proService;
	}

	public ProtocolNode(String name) {		
		super(name);	
	}
}
