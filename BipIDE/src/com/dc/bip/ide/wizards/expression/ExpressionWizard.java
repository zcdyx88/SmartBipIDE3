package com.dc.bip.ide.wizards.expression;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.dc.bip.ide.common.model.ExpressionConstants;
import com.dc.bip.ide.common.model.Params;
import com.dc.bip.ide.objects.ExpressionService;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.PlatformUtils;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.ExpressionFolder;
import com.dc.bip.ide.views.objects.ExpressionNode;
import com.dc.bip.ide.views.objects.ProjectNode;
import com.dc.bip.ide.views.objects.TreeNode;

public class ExpressionWizard extends Wizard implements INewWizard {
	private String projectName;
	private ExpressionService expressionService = new ExpressionService();
	private Params  params = new Params();
	public ExpressionWizard(String projectName) {
		this.setDialogSettings(new DialogSettings("新增表达式"));
		this.projectName = projectName;
	}


	@Override
	public void createPageControls(Composite pageContainer) {
	    //super.createPageControls(pageContainer);
	}
	
	@Override
	public void addPages() {
		this.addPage(new ExpressionWizardPage());

	}

	@Override
	public boolean performFinish() {
		IWizardPage[] pages = this.getPages();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		ExpressionWizardPage pageWizard = (ExpressionWizardPage)pages[0];
//		expressionService = pageWizard.getExpressionService();
		// 设置组合服务的基本信息 来自页面输入
		String serviceId = expressionService.getExpressionID();
		try {
			Params params = pageWizard.getParams();
			String fileFolderStr = new StringBuilder("/").append(projectName).append(BipConstantUtil.ExpressionPath)
					.toString();
			IFolder folder = root.getFolder(new Path(fileFolderStr));
			serviceId = params.get(ExpressionConstants.ID);
			String fileStr = (new StringBuilder(fileFolderStr)).append(serviceId + BipConstantUtil.ExpressionServiceExtension).toString();
			if (folder != null && !folder.exists())
				HelpUtil.mkdirs(folder, true, true, null);
			Path basePPath = new Path(fileStr);
			IResource baseResource = root.findMember(basePPath);
			if (baseResource != null && baseResource.exists()) {
				boolean confirm = PlatformUtils.showConfirm("提示", "存在同名文件，是否替换？");
				if (confirm == true){
					baseResource.delete(true, null);
				}else{
					return false;
				}
			}
			final IFile basefile = root.getFile(basePPath);
			ExpressionNode node= new  ExpressionNode(projectName);
			
			params.setPath(basefile.getLocation().toString());
			params.set("nodeName", serviceId);
			params.set("projectName", projectName);
			node.setParams(params);
			params.persist();
			IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			TreeView view = (TreeView) wp.findView("wizard.view1");
			if (null != view) {
				node.setResource(basefile);
				ExpressionFolder expressionFolder = null;
				for (TreeItem item : view.getViewer().getTree().getItems()) {
					if (item.getText().equalsIgnoreCase(projectName)) {
						ProjectNode tmpProjectNode = (ProjectNode) item.getData();
						for (TreeNode tmpNode : tmpProjectNode.getChildren()) {
							if (tmpNode instanceof ExpressionFolder) {
								expressionFolder = (ExpressionFolder) tmpNode;
							}
						}
						break;
					}
				}
				if (expressionFolder != null) {
					expressionFolder.addChild(node);
					view.getViewer().add(expressionFolder, node);
				}
			}
			IDE.openEditor(page, basefile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}



	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

}
