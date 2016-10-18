package org.activiti.designer.features;

import org.activiti.bpmn.model.SubProcess;
import org.activiti.designer.PluginImage;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public class CreateEmbeddedSubProcessFeature extends AbstractCreateBPMNFeature {

  public static final String FEATURE_ID_KEY = "subprocess";

  public CreateEmbeddedSubProcessFeature(IFeatureProvider fp) {
    super(fp, "并行", "并发执行的子流程");
  }

  @Override
  public Object[] create(ICreateContext context) {
    SubProcess newSubProcess = new SubProcess();
    addObjectToContainer(context, newSubProcess, "并行");

    // return newly created business object(s)
    return new Object[] { newSubProcess };
  }

  @Override
  public String getCreateImageId() {
    return PluginImage.IMG_SUBPROCESS_COLLAPSED.getImageKey();
  }

  @Override
  protected String getFeatureIdKey() {
    return FEATURE_ID_KEY;
  }
}
