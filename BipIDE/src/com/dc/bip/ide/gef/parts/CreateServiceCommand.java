package com.dc.bip.ide.gef.parts;

import org.eclipse.gef.commands.Command;

import com.dc.bip.ide.gef.model.LayerModel;
import com.dc.bip.ide.gef.model.ParamContainer;

public class CreateServiceCommand extends Command {
	
	private LayerModel layer;
	private ParamContainer service;
	
	public void execute() {
		layer.addChild(service);
	}
	
	public void setLayer(Object layer) {
		this.layer =(LayerModel) layer;
	}
	
	public void setService( Object service) {
		this.service = (ParamContainer)service;
	}
	
	public void undo(){
		layer.removeChild(service);
	}

	public boolean canExecute() {
		 if(layer.getChildren().contains(service))
		 {
			 return false;
		 }else
		 {
			 return true;
		 }
		
	}
}
