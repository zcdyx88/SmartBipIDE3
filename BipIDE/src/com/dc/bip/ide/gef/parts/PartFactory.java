package com.dc.bip.ide.gef.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.dc.bip.ide.gef.model.LayerModel;
import com.dc.bip.ide.gef.model.LineConnectionModel;
import com.dc.bip.ide.gef.model.Param;
import com.dc.bip.ide.gef.model.ParamContainer;
import com.dc.bip.ide.gef.model.TitleModel;

public class PartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = this.getEditPartForElement(model);
		part.setModel(model);
		return part;
	}

	private EditPart getEditPartForElement(Object model){
		if (model instanceof ParamContainer)
		{
			return new ParamContainerEditPart();
		}
		else if(model instanceof LayerModel)
		{
			return new LayerEditPart();
		}
		else if(model instanceof Param)
		{
			return new ParamEditPart();
		}else if(model instanceof TitleModel)
		{
			return new TitlePartEditPart();
		}else if(model instanceof LineConnectionModel)
		{
			return new LineConnectionEditPart();
		}
		else
		{
			System.out.println("");
			return null;
		}		    
	}
}
