package com.dc.bip.ide.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BSNode;
import com.dc.bip.ide.views.objects.BipServerNode;
import com.dc.bip.ide.views.objects.CompositeNode;

public class DeleteBipServerAction implements IObjectActionDelegate 
{
	
	private BipServerNode node;
	public DeleteBipServerAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		
		try {						
			String projectName = node.getProjectName();
			String id = node.getNodeName();
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	        String compositeFilePath = (new StringBuilder("/")).append(projectName).append(BipConstantUtil.ServerPath).append(id).append(".bipserver").toString();
	        IPath compositePath = new Path(compositeFilePath);
	        IFile compositefile = root.getFile(compositePath);
	        compositefile.delete(true, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		TreeView view = (TreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("wizard.view1");
		if(null != view)
		{
			node.getParent().removeChild(node);
			view.getViewer().remove(node);
//		    view.reload();
		}		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		if (selection instanceof IStructuredSelection) {
            Object first = ((IStructuredSelection) selection).getFirstElement();
            if (first instanceof BipServerNode) {
            	node = (BipServerNode) first;
            }
        }
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

}
