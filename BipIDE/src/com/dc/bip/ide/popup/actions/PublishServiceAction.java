package com.dc.bip.ide.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import com.dc.bip.ide.views.objects.BSOperationNode;
import com.dc.bip.ide.views.objects.TreeNode;
import com.dc.bip.ide.wizards.PublishServiceWizard;


public class PublishServiceAction implements IObjectActionDelegate{
	
	private TreeNode treeNode;
	
	public PublishServiceAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		PublishServiceWizard pbsw = new PublishServiceWizard(treeNode);
        WizardDialog dialog = new WizardDialog(shell, pbsw);
        dialog.setTitle("发布服务");
        dialog.open(); 		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection)
		{
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if( structuredSelection.getFirstElement() instanceof  TreeNode)
			{			
				treeNode = (TreeNode)(structuredSelection.getFirstElement());
			}			
		}		
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

}
