package com.dc.bip.ide.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.views.objects.TreeNode;
import com.dc.bip.ide.wizards.basesvc.BaseSvcW;
import com.dc.bip.ide.wizards.bipserver.NewBipServerWizard;

public class NewBipServerAction implements IObjectActionDelegate {

	private TreeNode treeNode;
	
	/**
	 * Constructor for Action1.
	 */
	public NewBipServerAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			NewBipServerWizard bsw = new NewBipServerWizard(treeNode.getProjectName());
	        WizardDialog dialog = new WizardDialog(shell, bsw);
	        dialog.setTitle("新增BIP服务端配置");
	        dialog.open();    
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {		
		if (selection instanceof IStructuredSelection) {
            Object first = ((IStructuredSelection) selection).getFirstElement();
            if (first instanceof TreeNode) {
            	treeNode = (TreeNode) first;
            }
        }
	}

}
