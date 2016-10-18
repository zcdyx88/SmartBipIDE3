package com.dc.bip.ide.views.objects;

import com.dc.bip.ide.objects.BaseService;

public class BSNode extends TreeNode {
	
	private BaseService baseService;
	
	public BSNode(String nodeName) {
		super(nodeName);		
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

}
