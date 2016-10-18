package com.dc.bip.ide.views.objects;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	
	private String projectName;
	private String nodeName;
	private TreeNode parent;
	private List<TreeNode> children = new ArrayList<TreeNode>();
	
	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public TreeNode(String nodeName ) {
		this.nodeName = nodeName;
	}

	public TreeNode(){
		
	}
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName; 
		for ( TreeNode node:children )
		{
			node.setProjectName(projectName);
		}		
	}
	
	public void addChild( TreeNode treeNode)
	{
		children.add(treeNode);
		treeNode.setParent(this);
		treeNode.setProjectName(this.projectName);
	}
	
	public void removeChild(TreeNode treeNode)
	{
		if(children.contains(treeNode))
		{
			children.remove(treeNode);
		}		
	}
	
}
