package com.dc.bip.ide.gef.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.graphics.Color;

import com.dc.bip.ide.gef.model.ParamContainer;

public class ParamContainerEditPart extends AbstractEditPartWithListener {

	@Override
	protected IFigure createFigure() {
		Figure service = new Figure();
//		service.setBorder(new LineBorder(ColorConstants.black,1));
		service.setBorder(new CompoundBorder(new LineBorder(),new MarginBorder(3)));
	    Color classColor = new Color(null,255,255,206);
	    service.setBackgroundColor(classColor);
	    //service.setOpaque(true);
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		layout.setStretchMinorAxis(true);
		//设置子图形的间距
		layout.setSpacing(7);
		//设置布局管理器
		service.setLayoutManager(layout);
		//增加空的LabeL便于移动
/*	    Label label = new Label();
	    ParamContainer container = (ParamContainer)getModel();
	    if(null ==container){
		    label.setText(((ParamContainer)getModel()).getTitle());
		    service.add(label);
	    }else
	    {
		    label.setText(((ParamContainer)getModel()).getName());
		    service.add(label);
	    }*/
		
		
	/*  Label label = new Label();
		Font classFont = new Font(null, "Arial", 12, SWT.BOLD);
		label.setText(model.getText());
		label.setFont(classFont);
		label.setBorder(new CompoundBorder(new LineBorder(),new MarginBorder(3)));
		label.setBackgroundColor(ColorConstants.yellow);
		label.setOpaque(true);
		service.add(label,0);*/
		
		return service;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new CustomComponentEditPolicy());
	}

	protected void refreshVisuals() {
		GraphicalEditPart part = (GraphicalEditPart)this.getParent();
		part.setLayoutConstraint(this, this.getFigure(), ((ParamContainer)this.getModel()).getRectangle());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(ParamContainer.p_constraint)){
			this.refreshVisuals();
		}
	}
	
	@Override
	public List getModelChildren() {
		ParamContainer model = (ParamContainer)this.getModel();
		return model.getChildren();
	}

}
