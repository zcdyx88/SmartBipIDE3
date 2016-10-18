package com.dc.bip.ide.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BusiFolder;
import com.dc.bip.ide.views.objects.CompositeFolder;
import com.dc.bip.ide.views.objects.ProjectNode;
import com.dc.bip.ide.views.objects.TreeNode;
import com.dc.bip.ide.wizards.bipserver.BipSvcImpWizard;

public class BipImportAction implements IObjectActionDelegate {

	private TreeNode treeNode;

	public BipImportAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		//每一个窗口都有一个Shell对象。Shell对象代表了与用户交互的窗口框架，并处理与窗口关联的诸如移动、改变大小等常见行为
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		//获取菜单引用
		TreeView view = (TreeView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView("wizard.view1");
		BipSvcImpWizard bsw = new BipSvcImpWizard(treeNode.getProjectName());
		WizardDialog dialog = new WizardDialog(shell, bsw);
		dialog.setTitle("新建组合服务");
		dialog.open();

		//Object[] expandedElements = view.getViewer().getExpandedElements();
		
		// 展开组合服务节点
		if (view != null) {
			CompositeFolder compositeFolder = null;
			ProjectNode projectNode = null;
			for (TreeItem item : view.getViewer().getTree().getItems()) {
				if (item.getText().equalsIgnoreCase(treeNode.getProjectName())) {
					projectNode = (ProjectNode) item.getData();
					for (TreeNode tmpNode : projectNode.getChildren()) {
						if (tmpNode instanceof CompositeFolder) {
							compositeFolder = (CompositeFolder) tmpNode;
							break;
						}
					}
					break;
				}
			}
		
			view.getViewer().setExpandedElements(new TreeNode[] { projectNode, compositeFolder });
		}
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
