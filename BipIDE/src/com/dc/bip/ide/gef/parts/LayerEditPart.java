package com.dc.bip.ide.gef.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.dc.bip.ide.gef.model.LayerModel;

public class LayerEditPart extends AbstractEditPartWithListener {

	@Override
	protected IFigure createFigure() {
		Layer layer =new Layer();
//		Label layer = new Label();
		layer.setLayoutManager(new XYLayout());
		
	/*	layer.setBorder(new CompoundBorder(new LineBorder(),new MarginBorder(3)));
		layer.setBackgroundColor(ColorConstants.red);
		layer.setOpaque(true);*/
		return layer;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		this.installEditPolicy("XYLayoutEditPolicy", new CustomXYLayoutEditPolicy());
	}
	
	@Override
	public List getModelChildren() {
		LayerModel model = (LayerModel)this.getModel();
		return model.getChildren();
	}
	
	protected void refreshVisuals() {
		GraphicalEditPart part = (GraphicalEditPart)this.getParent();
//		part.setLayoutConstraint(this, this.getFigure(), ((LayerModel)this.getModel()).getRectangle());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(LayerModel.P_Add_Child) || evt.getPropertyName().equals(LayerModel.P_Delete_Child))
		{
			this.refreshChildren();
		}
	}

}
