package com.dc.bip.ide.gef.parts;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class CustomComponentEditPolicy extends ComponentEditPolicy {

	protected Command getDeleteCommand(GroupRequest request) {
		 DeleteServiceCommand command = new DeleteServiceCommand();
		 command.setLayerModel(getHost().getParent().getModel());
		 command.setServiceModel(getHost().getModel());
		 return command;
	}
	
}
