package com.dc.bip.ide.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
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

import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BSNode;

public class DeleteBaseSvcAction implements IObjectActionDelegate 
{
	
	private BSNode node;
	public DeleteBaseSvcAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		//关闭编辑器
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorReference[] refers = page.getEditorReferences();
		IEditorPart editor;
		IFile file = node.getBaseService().getBaseFile();
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
		
		try {			
			/*BaseService bs = HelpUtil.parseBaseSvcXml(node.getResource().getContents());
			String codeFilePath = (new StringBuilder("/")).append(node.getProjectName()).append("/src/").append(bs.getImpls().replaceAll("\\.", "/")).append(".java").toString();
			System.out.println(codeFilePath);
		    IFile codeFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(codeFilePath));	*/	    
		    node.getBaseService().getBaseFile().delete(true, null);
		    node.getBaseService().getCodeFile().delete(true, null);
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
		// TODO Auto-generated method stub
		if (selection instanceof IStructuredSelection) {
            Object first = ((IStructuredSelection) selection).getFirstElement();
            if (first instanceof BSNode) {
            	node = (BSNode) first;
            }
        }
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

}
