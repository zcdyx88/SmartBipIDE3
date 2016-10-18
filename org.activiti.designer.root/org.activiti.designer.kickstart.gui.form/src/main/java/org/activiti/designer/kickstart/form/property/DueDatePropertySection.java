package org.activiti.designer.kickstart.form.property;

import org.activiti.workflow.simple.definition.form.ReferencePropertyDefinition;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author Frederik Heremans
 */
public class DueDatePropertySection extends AbstractKickstartFormComponentSection {

  protected Button editableControl;

  @Override
  public void createFormControls(TabbedPropertySheetPage aTabbedPropertySheetPage) {
    createFullWidthLabel("This field represent the due-date of the task. If used on a start-form, this allows selecting the default due-date "
        + "for all tasks that are part of the workflow. If added to a Human Step form, it allows displaying or editing the task's " +
        "duedate. If the due date should be displayed only, uncheck the 'Editable' property.");
    createSeparator();
    
    editableControl = createCheckboxControl("Editable");
  }

  @Override
  protected Object getModelValueForControl(Control control, Object businessObject) {
    ReferencePropertyDefinition propDef = (ReferencePropertyDefinition) businessObject;
    if (control == editableControl) {
      return propDef.isWritable();
    }
    return null;
  }

  @Override
  protected void storeValueInModel(Control control, Object businessObject) {
    ReferencePropertyDefinition propDef = (ReferencePropertyDefinition) businessObject;
    if (control == editableControl) {
      propDef.setWritable(editableControl.getSelection());
    }
  }
}
