package org.activiti.designer.kickstart.process.command;

import java.util.HashMap;

import org.activiti.workflow.simple.definition.WorkflowDefinition;
import org.eclipse.graphiti.features.IFeatureProvider;

/**
 * Model updater used for the general workflow-definition properties, this does NOT include the actual steps.
 * All modifications done to the steps-list the "newObject" are not reflected in the actual object.
 * 
 * @author Frederik Heremans
 */
public class WorkflowDefinitionModelUpdater extends KickstartProcessModelUpdater<WorkflowDefinition> {

  public WorkflowDefinitionModelUpdater(WorkflowDefinition businessObject, IFeatureProvider featureProvider) {
    // Pass NO pictogram-element to super-class, as the definition updates have no effect on the shapes
    super(businessObject, null, featureProvider);
  }

  @Override
  protected WorkflowDefinition cloneBusinessObject(WorkflowDefinition businessObject) {
    // Only clone the actual name, id an description. Steps are not needed to be included in the model-updater
    WorkflowDefinition clonedDefinition = new WorkflowDefinition();
    clonedDefinition.setDescription(businessObject.getDescription());
    clonedDefinition.setId(businessObject.getId());
    clonedDefinition.setKey(businessObject.getKey());
    clonedDefinition.setName(businessObject.getName());
    clonedDefinition.setParameters(new HashMap<String, Object>(businessObject.getParameters()));
    
    return clonedDefinition;
  }

  @Override
  protected void performUpdates(WorkflowDefinition valueObject, WorkflowDefinition targetObject) {
    targetObject.setName(valueObject.getName());
    targetObject.setId(valueObject.getId());
    targetObject.setKey(valueObject.getKey());
    targetObject.setDescription(valueObject.getDescription());
    targetObject.setParameters(valueObject.getParameters());
  }
}
