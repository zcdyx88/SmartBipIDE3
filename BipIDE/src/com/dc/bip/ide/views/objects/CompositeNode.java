package com.dc.bip.ide.views.objects;

import java.io.Serializable;

import com.dc.bip.ide.objects.CompositeService;
/**
 * 组合服务文件节点
 * @author zhongwei
 *
 */
public class CompositeNode extends TreeNode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CompositeNode(String name) {		
		super(name);	
	}
	private CompositeService compSvc;
	public CompositeService getCompSvc() {
		return compSvc;
	}
	public void setCompSvc(CompositeService compSvc) {
		this.compSvc = compSvc;
	}
	
}
