package org.activiti.designer.features;

import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.TerminateEventDefinition;
import org.activiti.designer.PluginImage;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public class CreateTerminateEndEventFeature extends AbstractCreateFastBPMNFeature {

  public static final String FEATURE_ID_KEY = "terminateendevent";

  public CreateTerminateEndEventFeature(IFeatureProvider fp) {
    // set name and description of the creation feature
    super(fp, "TerminateEndEvent", "Add terminate end event");
  }

  public Object[] create(ICreateContext context) {
    EndEvent endEvent = new EndEvent();
    TerminateEventDefinition eventDef = new TerminateEventDefinition();
    endEvent.getEventDefinitions().add(eventDef);
    addObjectToContainer(context, endEvent, "TerminateEndEvent");

    // return newly created business object(s)
    return new Object[] { endEvent };
  }

  @Override
  public String getCreateImageId() {
    return PluginImage.IMG_EVENT_TERMINATE.getImageKey();
  }

  @Override
  protected String getFeatureIdKey() {
    return FEATURE_ID_KEY;
  }
}
