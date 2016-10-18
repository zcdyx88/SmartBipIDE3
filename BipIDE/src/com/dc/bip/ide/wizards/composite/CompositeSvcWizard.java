package com.dc.bip.ide.wizards.composite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.PlatformUtils;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BSNode;
import com.dc.bip.ide.views.objects.BaseFolder;
import com.dc.bip.ide.views.objects.CompositeFolder;
import com.dc.bip.ide.views.objects.CompositeNode;
import com.dc.bip.ide.views.objects.ProjectNode;
import com.dc.bip.ide.views.objects.TreeNode;

public class CompositeSvcWizard extends Wizard implements INewWizard {
	private String projectName;
	private CompositeService compositeService = new CompositeService();

	public CompositeSvcWizard(String projectName) {
		this.setDialogSettings(new DialogSettings("组合服务"));
		this.setProjectName(projectName);
	}

	@Override
	public void addPages() {
		this.addPage(new CompositeSvcWizardPage());
	}

	@Override
	public boolean performFinish() {

		IWizardPage[] pages = this.getPages();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		if (pages != null) {
			CompositeSvcWizardPage pageWizard = (CompositeSvcWizardPage) pages[0];
			// 设置组合服务的基本信息 来自页面输入
			compositeService = pageWizard.getCompositeService();
			String serviceId = compositeService.getBaseinfo().getServiceId();
			try {
				String fileFolderStr = new StringBuilder("/").append(projectName).append(BipConstantUtil.CompositePath)
						.toString();
				IFolder folder = root.getFolder(new Path(fileFolderStr));

				String fileStr = (new StringBuilder(fileFolderStr)).append(serviceId + ".composite").toString();

				if (folder != null && !folder.exists())
					HelpUtil.mkdirs(folder, true, true, null);
				Path basePPath = new Path(fileStr);
				// 如果存在该.composite 文件 就删除
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
				compositeService.setProject(basefile.getProject());
				// 存储.composite文件 用与关闭editor窗口
				compositeService.setCompositePath(basefile.getLocation().toOSString());
				// 写入compositeService 对象到.composite 文件
				HelpUtil.writeObject(compositeService, basefile);

				IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				TreeView view = (TreeView) wp.findView("wizard.view1");
				if (null != view) {
					CompositeNode compNode = new CompositeNode(serviceId);
					compNode.setProjectName(compositeService.getProject().getName());
					compNode.setCompSvc(compositeService);

					CompositeFolder compositeFolder = null;
					for (TreeItem item : view.getViewer().getTree().getItems()) {
						if (item.getText().equalsIgnoreCase(projectName)) {
							ProjectNode tmpProjectNode = (ProjectNode) item.getData();
							for (TreeNode tmpNode : tmpProjectNode.getChildren()) {
								if (tmpNode instanceof CompositeFolder) {
									compositeFolder = (CompositeFolder) tmpNode;
									break;
								}
							}
							break;
						}
					}
					if (compositeFolder != null) {
						compositeFolder.addChild(compNode);
						view.getViewer().add(compositeFolder, compNode);
					}
				}

				IDE.openEditor(page, basefile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	class LongRunningOperation implements IRunnableWithProgress {
		// The total sleep time
		private static final int TOTAL_TIME = 10000;
		// The increment sleep time
		private static final int INCREMENT = 500;
		private boolean indeterminate;

		public LongRunningOperation(boolean indeterminate) {
			this.indeterminate = indeterminate;
		}

		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			monitor.beginTask("Running long running operation", indeterminate ? IProgressMonitor.UNKNOWN : TOTAL_TIME);
			// monitor.subTask("Doing first half");
			for (int total = 0; total < TOTAL_TIME && !monitor.isCanceled(); total += INCREMENT) {
				Thread.sleep(INCREMENT);
				monitor.worked(INCREMENT);
				if (total == TOTAL_TIME / 2)
					monitor.subTask("Doing second half");
			}
			monitor.done();
			if (monitor.isCanceled())
				throw new InterruptedException("The long running operation was cancelled");
		}
	}
}
