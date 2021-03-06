package org.activiti.designer.kickstart.form.features;

import org.activiti.designer.kickstart.form.diagram.KickstartFormFeatureProvider;
import org.activiti.designer.kickstart.form.diagram.shape.BusinessObjectShapeController;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * Base feature to extend for adding a new form-component to the diagram.
 * 
 * @author Frederik Heremans
 */
public class AddFormComponentFeature extends AbstractAddShapeFeature {

  public AddFormComponentFeature(KickstartFormFeatureProvider fp) {
    super(fp);
  }

  @Override
  public boolean canAdd(IAddContext context) {
    return getKickstartFormFeatureProvider().hasShapeController(context.getNewObject());
  }
  
  @Override
  public PictogramElement add(IAddContext context) {
    final ContainerShape parent = context.getTargetContainer();
    
    // Get the controller, capable of creating a new shape for the business-object
    BusinessObjectShapeController shapeController = getKickstartFormFeatureProvider()
        .getShapeController(context.getNewObject());
   
    // Request a new shape from the controller
    final ContainerShape containerShape = shapeController.createShape(context.getNewObject(), 
        parent, context.getWidth(), context.getHeight());
        
    // Create link between shape and business object
    link(containerShape, context.getNewObject());
    
    // Since new shape has already been added to the parent, trigger a move to have it
    // positioned based on the layout implementation, taking into account X and Y from add-context
    getKickstartFormFeatureProvider().getFormLayouter()
      .moveShape(parent, parent, containerShape, context.getX(), context.getY());
    
    return containerShape;
  }

  protected KickstartFormFeatureProvider getKickstartFormFeatureProvider() {
    return (KickstartFormFeatureProvider) getFeatureProvider();
  }
}
