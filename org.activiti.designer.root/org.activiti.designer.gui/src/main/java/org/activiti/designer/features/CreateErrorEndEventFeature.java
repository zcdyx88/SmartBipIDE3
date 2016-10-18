package org.activiti.designer.features;

import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ErrorEventDefinition;
import org.activiti.designer.PluginImage;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public class CreateErrorEndEventFeature extends AbstractCreateFastBPMNFeature {

  public static final String FEATURE_ID_KEY = "errorendevent";

  public CreateErrorEndEventFeature(IFeatureProvider fp) {
    // set name and description of the creation feature
    super(fp, "ErrorEndEvent", "Add error end event");
  }

  public Object[] create(ICreateContext context) {
    EndEvent endEvent = new EndEvent();
    ErrorEventDefinition eventDef = new ErrorEventDefinition();
    endEvent.getEventDefinitions().add(eventDef);
    addObjectToContainer(context, endEvent, "ErrorEnd");

    // return newly created business object(s)
    return new Object[] { endEvent };
  }

  @Override
  public String getCreateImageId() {
    return PluginImage.IMG_EVENT_ERROR.getImageKey();
  }

  @Override
  protected String getFeatureIdKey() {
    return FEATURE_ID_KEY;
  }
}
