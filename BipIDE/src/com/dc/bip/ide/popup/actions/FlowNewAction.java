package com.dc.bip.ide.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.views.objects.TreeNode;
import com.dc.bip.ide.wizards.FlowNewWizard;
import com.dc.bip.ide.wizards.busi.BusiSvcImpWizard;

public class FlowNewAction implements IObjectActionDelegate{

	private TreeNode treeNode;

	public FlowNewAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		FlowNewWizard newFlow = new FlowNewWizard(treeNode.getProjectName());
        WizardDialog dialog = new WizardDialog(shell, newFlow);
        dialog.setTitle("新建流程");
        dialog.open();  		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
            Object first = ((IStructuredSelection) selection).getFirstElement();
            if (first instanceof TreeNode) {
            	treeNode = (TreeNode) first;
            }
        }		
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

}
