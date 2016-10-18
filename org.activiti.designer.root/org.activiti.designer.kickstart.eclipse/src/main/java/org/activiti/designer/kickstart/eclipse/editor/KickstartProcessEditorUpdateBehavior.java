package org.activiti.designer.kickstart.eclipse.editor;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.ui.editor.DefaultUpdateBehavior;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;

/**
 * Subclasses the default update behavior of Graphiti to all external creation of the transactional
 * editing domain. This allows us to not create one externally via internal API but rely on the
 * standard way of doing it ... well mostly.
 *
 * @author Heiko Kopp
 */
public class KickstartProcessEditorUpdateBehavior extends DefaultUpdateBehavior {

  public KickstartProcessEditorUpdateBehavior(DiagramBehavior diagramBehavior) {
    super(diagramBehavior);
  }

@Override
  public TransactionalEditingDomain getEditingDomain() {
    if (super.getEditingDomain() == null) {
      createEditingDomain(null);
    }

    return super.getEditingDomain();
  }

  @Override
  protected boolean isAdapterActive() {
    return false;
  }

}
