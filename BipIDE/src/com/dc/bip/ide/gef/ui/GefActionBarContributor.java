package com.dc.bip.ide.gef.ui;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.actions.ActionFactory;

public class GefActionBarContributor extends ActionBarContributor {

	@Override
	protected void buildActions() {
		this.addRetargetAction(new UndoRetargetAction());
		this.addRetargetAction(new RedoRetargetAction());
		this.addRetargetAction(new DeleteRetargetAction());
	}

	@Override
	protected void declareGlobalActionKeys() {
		
	}
	
    public void contributeToToolBar(IToolBarManager toolBarManager) {
    	toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
    	toolBarManager.add(getAction(ActionFactory.REDO.getId()));
    	toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
    	
    }


}
