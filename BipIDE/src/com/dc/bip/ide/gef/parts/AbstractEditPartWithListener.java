package com.dc.bip.ide.gef.parts;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.dc.bip.ide.gef.model.AbstractModel;

public abstract class AbstractEditPartWithListener extends AbstractGraphicalEditPart
		implements PropertyChangeListener {

	public void activate() {		
		AbstractModel sm = (AbstractModel)getModel();
		sm.addPropertyChangeListener(this);
		super.activate();
	}
	
	public void deactivate() {
		AbstractModel sm = (AbstractModel)getModel();
		sm.removePropertyChangeListener(this);
		super.deactivate();
	}
}
