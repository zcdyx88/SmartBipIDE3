package org.activiti.designer.command;

import org.activiti.bpmn.model.UserTask;
import org.eclipse.graphiti.features.IFeatureProvider;

public class UserTaskModelUpdater extends BpmnProcessModelUpdater {

  public UserTaskModelUpdater(IFeatureProvider featureProvider) {
    super(featureProvider);
  }
  
  @Override
  public boolean canControlShapeFor(Object businessObject) {
    if (businessObject instanceof UserTask) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  protected UserTask cloneBusinessObject(Object businessObject) {
    return ((UserTask) businessObject).clone();
  }

  @Override
  protected void performUpdates(Object valueObject, Object targetObject) {
    ((UserTask) targetObject).setValues(((UserTask) valueObject));
  }
  
  @Override
  public BpmnProcessModelUpdater createUpdater(IFeatureProvider featureProvider) {
    return new UserTaskModelUpdater(featureProvider);
  }
}
