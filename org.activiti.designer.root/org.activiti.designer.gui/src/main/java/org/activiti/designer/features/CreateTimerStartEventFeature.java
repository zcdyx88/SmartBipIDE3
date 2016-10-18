package org.activiti.designer.features;

import org.activiti.bpmn.model.EventSubProcess;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.TimerEventDefinition;
import org.activiti.designer.PluginImage;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public class CreateTimerStartEventFeature extends AbstractCreateBPMNFeature {

  public static final String FEATURE_ID_KEY = "timerstartevent";

  public CreateTimerStartEventFeature(IFeatureProvider fp) {
    // set name and description of the creation feature
    super(fp, "TimerStartEvent", "Add timer start event");
  }

  public boolean canCreate(ICreateContext context) {
    Object parentObject = getBusinessObjectForPictogramElement(context.getTargetContainer());
    if (parentObject instanceof EventSubProcess)
      return false;
    return super.canCreate(context);
  }

  public Object[] create(ICreateContext context) {
    StartEvent startEvent = new StartEvent();
    TimerEventDefinition timerEvent = new TimerEventDefinition();
    startEvent.getEventDefinitions().add(timerEvent);
    addObjectToContainer(context, startEvent, "Timer start");

    // return newly created business object(s)
    return new Object[] { startEvent };
  }

  @Override
  public String getCreateImageId() {
    return PluginImage.IMG_EVENT_TIMER.getImageKey();
  }

  @Override
  protected String getFeatureIdKey() {
    return FEATURE_ID_KEY;
  }
}
