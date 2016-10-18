package org.activiti.designer.property;

import org.activiti.bpmn.model.TextAnnotation;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class PropertyTextAnnotationFilter extends ActivitiPropertyFilter {

	@Override
	protected boolean accept(PictogramElement element) {
		return getBusinessObject(element) instanceof TextAnnotation;
	}
}
