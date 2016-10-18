package org.activiti.designer.property;

import org.activiti.bpmn.model.SequenceFlow;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class PropertySequenceFlowFilter extends ActivitiPropertyFilter {
	
	@Override
	protected boolean accept(PictogramElement pe) {
		Object bo = getBusinessObject(pe);
		if (bo instanceof SequenceFlow) {
			return true;
		}
		return false;
	}

}
