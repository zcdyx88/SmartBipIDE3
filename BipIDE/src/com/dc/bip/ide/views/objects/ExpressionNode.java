package com.dc.bip.ide.views.objects;

import java.io.Serializable;

import org.eclipse.core.resources.IFile;

import com.dc.bip.ide.common.model.Params;
import com.dc.bip.ide.objects.ExpressionService;
import com.dc.bip.ide.objects.ProtocolInService;

public class ExpressionNode extends TreeNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ExpressionService expressionService;
	private Params params;
	public Params getParams() {
		return params;
	}
	public void setParams(Params params) {
		this.params = params;
	}
	private IFile  resource;
	
	public IFile getResource() {
		return resource;
	}
	public void setResource(IFile resource) {
		this.resource = resource;
	}
	

	public ExpressionService getExpressionService() {
		return expressionService;
	}
	public void setExpressionService(ExpressionService expressionService) {
		this.expressionService = expressionService;
	}
	public ExpressionNode(String name) {		
		super(name);	
	}
	

}
