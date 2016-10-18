package com.dc.bip.ide.gef.parts;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

import com.dc.bip.ide.gef.model.AbstractConnectionNode;
import com.dc.bip.ide.gef.model.Param;
import com.dc.bip.ide.gef.model.TitleModel;

public class TitlePartEditPart extends AbstractEditPartWithListener implements NodeEditPart{

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equalsIgnoreCase(AbstractConnectionNode.P_SOURCE_CONNECTION))
		{
			this.refreshSourceConnections();
		}	
		else	if(evt.getPropertyName().equalsIgnoreCase(AbstractConnectionNode.P_TARGET_CONNECTION))
		{
			this.refreshTargetConnections();
		}			
	}

	@Override
	protected IFigure createFigure() {
		TitleModel model = (TitleModel)this.getModel();
		Label title = new Label();
		title.setFont(new Font(null, "Arial", model.getFontSize(), SWT.BOLD));
		title.setText(((TitleModel)this.getModel()).getName());
		title.setLabelAlignment(PositionConstants.LEFT);
		return title;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new CustomGraphicalNodeEditPolicy());
	}
	

	
	protected void refreshVisuals() {
		//GraphicalEditPart part = (GraphicalEditPart)this.getParent();
		//part.setLayoutConstraint(this, this.getFigure(), ((TitleModel)this.getModel()).getRectangle());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(this.getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(this.getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ChopboxAnchor(this.getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ChopboxAnchor(this.getFigure());
	}
	
	public List getModelSourceConnections(){
		return ((TitleModel)getModel()).getSourceConnection();
	}

	public List getModelTargetConnections(){
		return ((TitleModel)getModel()).getTargetConnection();
	}

}
