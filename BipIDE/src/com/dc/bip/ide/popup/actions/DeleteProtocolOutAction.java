package com.dc.bip.ide.popup.actions;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.ProtocolOutNode;

public class DeleteProtocolOutAction implements IObjectActionDelegate {
	public ProtocolOutNode node ;

	@Override
	public void run(IAction arg0) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		try {
			File protocolInFile = new File(node.getProtocolInService().getMenuFilePath());
			if (protocolInFile.exists()) {
				protocolInFile.getAbsoluteFile().delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		//关闭编辑器
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorReference[] refers = page.getEditorReferences();
		IEditorPart editor;
		if(node.getProtocolInService().getiFilePath() != null && !"".equals(node.getProtocolInService().getiFilePath())){
			IPath sdaPath = new Path(node.getProtocolInService().getiFilePath());
			IFile file = root.getFile(sdaPath);
			IEditorInput input = new FileEditorInput(file);
		      for (IEditorReference refer : refers) {
		    	 
			        try {	
						boolean f =  input.getName().equals(refer.getEditorInput().getName());
						if(f){
							editor = refer.getEditor(false);
							page.closeEditor(editor, false);
						}
					} catch (PartInitException e) {
						e.printStackTrace();
					}
			      }
		}
		TreeView view = (TreeView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView("wizard.view1");
	
		//取得工作台窗口
		if (null != view) {
			node.getParent().removeChild(node);
			view.getViewer().remove(node);
			//view.reload();
		}
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object first = ((IStructuredSelection) selection).getFirstElement();
			if (first instanceof ProtocolOutNode) {
				node = (ProtocolOutNode) first;
			}
		}
		
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// TODO Auto-generated method stub
		
	}

}
