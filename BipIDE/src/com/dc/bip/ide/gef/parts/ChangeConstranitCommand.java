package com.dc.bip.ide.gef.parts;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.dc.bip.ide.gef.model.ParamContainer;

public class ChangeConstranitCommand extends Command {

	
	private ParamContainer model;
	private Rectangle constranit;
	private Rectangle oldConstranit;
	
	public void execute() {
		model.setRectangle(constranit);
	}

	public ParamContainer getModel() {
		return model;
	}

	public Rectangle getConstranit() {
		return constranit;
	}

	public void setModel(ParamContainer model) {
		this.model = model;
		oldConstranit = model.getRectangle();
	}

	public void setConstranit(Rectangle constranit) {
		this.constranit = constranit;
	}
	
	public void undo() {
		model.setRectangle(oldConstranit);
	}
	

}
