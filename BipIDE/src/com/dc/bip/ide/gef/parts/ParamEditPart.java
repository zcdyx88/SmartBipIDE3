package com.dc.bip.ide.gef.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.swt.graphics.Color;

import com.dc.bip.ide.gef.model.AbstractConnectionNode;
import com.dc.bip.ide.gef.model.Param;

public class ParamEditPart extends AbstractEditPartWithListener implements NodeEditPart{

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
		Param model = (Param)getModel();
		Label label = new Label();
		label.setText(model.getName());
//		label.setBorder(new CompoundBorder(new MarginBorder(3),new LineBorder()));
		//label.setBorder(new LineBorder());
	   /* Color classColor = new Color(null,255,255,206);	    
	    label.setBackgroundColor(classColor);*/
	    
	    label.setOpaque(true);
		label.setBackgroundColor(ColorConstants.menuBackgroundSelected);
		//label.setOpaque(true);
		return label;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new CustomGraphicalNodeEditPolicy());
		
	}
	
	protected void refreshVisuals() {
		GraphicalEditPart part = (GraphicalEditPart)this.getParent();
		setLayoutConstraint(this, this.getFigure(), ((Param)this.getModel()).getRectangle());
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
		return ((Param)getModel()).getSourceConnection();
	}

	public List getModelTargetConnections(){
		return ((Param)getModel()).getTargetConnection();
	}

}
