package com.dc.bip.ide.views.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;

import com.dc.bip.ide.objects.BusiSvc;

public class BusiNode extends TreeNode{
	private IFile resource;

	private BusiSvc bs;

	public BusiNode(String nodeName) {
		super(nodeName);
	}
	
	public IFile getResource() {
		return resource;
	}
	public void setResource(IFile resource) {
		this.resource = resource;
	}
	
	//获取children
	public void initChildren(){
		try{
			File file = resource.getLocation().toFile();
			FileInputStream in = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(in);
			this.bs= (BusiSvc)objectInputStream.readObject();
			List<BSBindingNode> children = bs.getBindingNodes();
			super.getChildren().addAll(children);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public BusiSvc getBusiSvc(){
		return bs;
	}


}
