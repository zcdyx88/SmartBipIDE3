package com.dc.bip.ide.gef.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

public class LineConnectionEditPart extends AbstractConnectionEditPart {

	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new CustomConnectionEndpointEditPolicy());
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE, new CustomConnectionEditPolicy());
		
	}
	
	protected IFigure createFigure() {
		PolylineConnection connection =  new PolylineConnection();
		connection.setTargetDecoration(new PolygonDecoration());
		return connection;
	}

}
