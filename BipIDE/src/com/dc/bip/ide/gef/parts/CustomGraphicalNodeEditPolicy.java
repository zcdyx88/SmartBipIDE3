package com.dc.bip.ide.gef.parts;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

public class CustomGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		CreateConnectionCommand command = (CreateConnectionCommand)request.getStartCommand();
		command.setTarget(this.getHost().getModel());
		return command;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		CreateConnectionCommand command = new CreateConnectionCommand();
		command.setConnection(request.getNewObject());
		command.setSource(this.getHost().getModel());
		request.setStartCommand(command);
		return command;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		ReConnectTargetCommand command = new ReConnectTargetCommand();
		command.setConnection(request.getConnectionEditPart().getModel());
		command.setTarget(getHost().getModel());
		return command;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		ReConnectSourceCommand command = new ReConnectSourceCommand();
		command.setConnection(request.getConnectionEditPart().getModel());
		command.setSource(getHost().getModel());
		return command;
	}

}
