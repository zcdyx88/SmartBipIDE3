package com.dc.bip.ide.popup.actions;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
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
import com.dc.bip.ide.views.objects.BSOperationNode;

public class DeleteBusiSvcAction implements IObjectActionDelegate {
	private BSOperationNode node;
	private String resFileStr;
	private StringBuffer sbf = new StringBuffer();
	private final String NAME = ".busi";

	@Override
	public void run(IAction action) {

		// 关闭窗口
		IWorkspaceRoot wRoot = ResourcesPlugin.getWorkspace().getRoot();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor;
		IEditorReference[] refers = page.getEditorReferences();
		String filePathStr = node.getPersistPath();
		if (null != filePathStr && !"".equals(filePathStr)) {
			Path filePath = new Path(filePathStr);
			IFile file = wRoot.getFile(filePath);
			IEditorInput input = new FileEditorInput(file);
			for (IEditorReference refer : refers) {
				try {
					boolean f = input.getName().equals(refer.getEditorInput().getName());
					if (f) {
						editor = refer.getEditor(false);
						page.closeEditor(editor, false);
					}
					System.out.println(f);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		}
		node.dispose();
		
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object first = ((IStructuredSelection) selection).getFirstElement();
			if (first instanceof BSOperationNode) {
				node = (BSOperationNode) first;
			}
		}

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
