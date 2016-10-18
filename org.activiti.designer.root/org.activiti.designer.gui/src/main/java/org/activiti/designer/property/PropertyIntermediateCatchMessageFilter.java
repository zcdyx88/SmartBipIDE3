package org.activiti.designer.property;

import org.activiti.bpmn.model.EventDefinition;
import org.activiti.bpmn.model.IntermediateCatchEvent;
import org.activiti.bpmn.model.MessageEventDefinition;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class PropertyIntermediateCatchMessageFilter extends ActivitiPropertyFilter {
	
	@Override
	protected boolean accept(PictogramElement pe) {
		Object bo = getBusinessObject(pe);
		if (bo instanceof IntermediateCatchEvent) {
		  if(((IntermediateCatchEvent) bo).getEventDefinitions() != null) {
		    for(EventDefinition eventDefinition : ((IntermediateCatchEvent) bo).getEventDefinitions()) {
		      if(eventDefinition instanceof MessageEventDefinition) {
		        return true;
		      }
		    }
		  }
		}
		return false;
	}

}
