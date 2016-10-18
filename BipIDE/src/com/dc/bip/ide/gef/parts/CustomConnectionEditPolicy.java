package com.dc.bip.ide.gef.parts;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class CustomConnectionEditPolicy extends ConnectionEditPolicy {

	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		DelecteConnectionCommand command = new DelecteConnectionCommand();
		command.setConnection(getHost().getModel());
		return command;
	}

}
