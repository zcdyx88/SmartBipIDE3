package org.activiti.designer.property;

import org.activiti.bpmn.model.UserTask;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class PropertyTaskListenerFilter extends ActivitiPropertyFilter {

	@Override
	protected boolean accept(PictogramElement pe) {
		Object bo = getBusinessObject(pe);
		if (bo instanceof UserTask) {
			return true;
		}
		return false;
	}

}
