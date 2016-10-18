package com.dc.bip.ide.popup.actions;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
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
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import com.dc.bip.ide.common.model.ConfigFile;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.CompositeNode;

public class DeleteCompSvcAction implements IObjectActionDelegate {

	private CompositeNode node;

	public DeleteCompSvcAction() {
	}

	@Override
	public void run(IAction action) {
		String projectName = node.getProjectName();
		String serviceId = node.getNodeName();
		deleteLocalFile(projectName, serviceId, "wsdl");
		deleteLocalFile(projectName, serviceId, "xsd");
		deleteLocalFile(projectName, serviceId, "map");
		deleteLocalFile(projectName, serviceId, "code");
		deleteLocalFile(projectName, serviceId, "composite");
		deleteLocalFolder( projectName,  serviceId);
		//关闭编辑器
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorReference[] refers = page.getEditorReferences();
		IEditorPart editor;
		if(node.getCompSvc().getCompositePath() != null && !"".equals(node.getCompSvc().getCompositePath())){
			IPath sdaPath = new Path(node.getCompSvc().getCompositePath());
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
		/*
		 * add by wanming start
		 * 刚刚新增的组合服务，文件明明已经创建，为什么IFile调用exists()结果是false?而使用File调用exists()的结果是true
		 * */
		List<ConfigFile> configFiles = node.getCompSvc().getConfigs();
		for (ConfigFile configFile : configFiles){
			configFile.delete();
		}
		File file = new File(node.getCompSvc().getCompositePath());
		System.out.println("资源[" + file + "]是否存在" + file.exists());
		System.out.println(file.getAbsoluteFile().delete());
		/*add end*/
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object first = ((IStructuredSelection) selection).getFirstElement();
			if (first instanceof CompositeNode) {
				node = (CompositeNode) first;
			}
		}
	}

	/***
	 * 删除资源文件
	 * 
	 * @param projectName
	 *            工程名
	 * @param servcieId
	 *            服务码
	 * @param fileType
	 *            后缀
	 */
	public void deleteLocalFile(String projectName, String servcieId , String fileType) {
		String prefix = ".";
		IFile compositefile = null;
		IPath destPath;
		
		try {
			String destFilePath = (new StringBuilder("/").append(projectName).append(BipConstantUtil.CompositePath)
					.append(servcieId).append(prefix + fileType).toString());
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			destPath = new Path(destFilePath);
			compositefile = root.getFile(destPath);
			System.out.println("资源[" + compositefile.getFullPath() + "]是否存在" + compositefile.exists());
			if (compositefile.exists()) {
				compositefile.delete(true, null);
			}
		} catch (CoreException e) {
			System.err.println("删除本地资源出错[" + compositefile.getFullPath() + "]");
			e.printStackTrace();
		} 
	}
	
	public void deleteLocalFolder(String projectName, String servcieId) {
		IFolder compositeFolder = null;
		IPath destPath;
		
		try {
			String destFilePath = new StringBuilder("/").append(projectName).append(BipConstantUtil.CompositePath)
					.append(servcieId).toString();
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			destPath = new Path(destFilePath);
			compositeFolder = root.getFolder(destPath);
			if (compositeFolder.exists()) {
				compositeFolder.delete(true, null);
			}
		} catch (CoreException e) {
			System.err.println("删除本地资源出错[" + compositeFolder.getFullPath() + "]");
			e.printStackTrace();
		} 
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
