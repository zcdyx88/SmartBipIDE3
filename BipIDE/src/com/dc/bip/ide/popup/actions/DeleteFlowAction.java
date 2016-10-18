package com.dc.bip.ide.popup.actions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BSNode;
import com.dc.bip.ide.views.objects.FlowNode;

public class DeleteFlowAction implements IObjectActionDelegate {

	private FlowNode node;
	
	public DeleteFlowAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		try {			
		    node.getResource().delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		TreeView view = (TreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("wizard.view1");
		if(null != view)
		{
			node.getParent().removeChild(node);
			view.getViewer().remove(node);
//			view.reload();
		}		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
            Object first = ((IStructuredSelection) selection).getFirstElement();
            if (first instanceof FlowNode) {
            	node = (FlowNode) first;
            }
        }		
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

}
