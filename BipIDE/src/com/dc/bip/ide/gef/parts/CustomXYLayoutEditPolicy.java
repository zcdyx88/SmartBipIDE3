package com.dc.bip.ide.gef.parts;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import com.dc.bip.ide.gef.model.ParamContainer;

public class CustomXYLayoutEditPolicy extends XYLayoutEditPolicy {
	
	public Command getCommand(Request request) {
           System.out.println(request.getType());
           return super.getCommand(request);
	}
	
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		ChangeConstranitCommand command =  new ChangeConstranitCommand();
		command.setModel((ParamContainer)child.getModel());
		command.setConstranit((Rectangle)constraint);
		return command;
	}
	
	@Override
	protected  Command getCreateCommand(CreateRequest request){
		CreateServiceCommand command = new CreateServiceCommand();
		ParamContainer serviceModel = (ParamContainer)request.getNewObject();
		Rectangle rectangle = (Rectangle)this.getConstraintFor(request);
		serviceModel.setRectangle(rectangle);
		command.setService(serviceModel);
		command.setLayer(getHost().getModel());
		return command;	
	}

}
