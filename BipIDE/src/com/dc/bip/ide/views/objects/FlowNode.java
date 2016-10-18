package com.dc.bip.ide.views.objects;

import org.eclipse.core.resources.IFile;

public class FlowNode extends TreeNode {
	
	private IFile resource;	
	
	public FlowNode(String nodeName) {
		super(nodeName);	
		
	}
	public IFile getResource() {
		return resource;
	}
	public void setResource(IFile resource) {
		this.resource = resource;
	}
}