package org.activiti.designer.property;

import org.activiti.bpmn.model.ScriptTask;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class PropertyScriptTaskFilter extends ActivitiPropertyFilter {
	
	@Override
	protected boolean accept(PictogramElement pe) {
		Object bo = getBusinessObject(pe);
		if (bo != null && bo instanceof ScriptTask) {
			return true;
		}
		return false;
	}

}
