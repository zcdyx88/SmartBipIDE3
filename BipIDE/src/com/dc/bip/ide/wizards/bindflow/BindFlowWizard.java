package com.dc.bip.ide.wizards.bindflow;

import java.lang.reflect.InvocationTargetException;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;

//业务服务向导
public class BindFlowWizard extends Wizard implements INewWizard {

	private String projectName;
	private CompositeService compositeService;
	private Table table_flow;
	public BindFlowWizard(String projectName,CompositeService compositeService) {
		this.setDialogSettings(new DialogSettings("导入工程"));
		this.projectName = projectName;
		this.compositeService = compositeService;
	}

	public BindFlowWizard(String projectName,CompositeService compositeService, Table table_flow) {
		this.setDialogSettings(new DialogSettings("导入工程"));
		this.projectName = projectName;
		this.compositeService = compositeService;
		this.table_flow = table_flow;
	}
	
	
	@Override
	public void addPages() {
		this.addPage(new BindFlowWizardPage(projectName));
		// this.addPage(new BaseSvcWizardP2());
	}

	@Override
	public boolean performFinish() {
//		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IWizardPage[] pages = this.getPages();
		BindFlowWizardPage bindpage = (BindFlowWizardPage) pages[0];
		String flowName = bindpage.getFlowName();
		compositeService.setFlowname(flowName);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		String compositeFilePath = (new StringBuilder("/")).append(projectName)
				.append(BipConstantUtil.CompositePath).append(compositeService.getBaseinfo().getServiceId()).append(".composite").toString();
		IFile compositeFile = root.getFile(new Path(compositeFilePath));
		compositeService.setProject(compositeFile.getProject());
		HelpUtil.writeObject(compositeService, compositeFile);
//		Shell shell = PlatformUI.getWorkbench().getModalDialogShellProvider().getShell();
		//给table赋值
		table_flow.removeAll();
		TableItem tableItem = new TableItem(table_flow, SWT.NONE);
		// 判断是否绑定流程 20160531
		String flownamePath = "";
		// 判断是否绑定流程
		if (flowName != null) {
			flownamePath = flowName.substring(0, flowName.indexOf("."));
		}
		tableItem.setText(0, flownamePath);
		tableItem.setText(1, flowName);
		
//		MessageDialog.openInformation(shell, "绑定流程", "绑定成功");
		//保存流程不用再刷新菜单 20160531
//		TreeView view = (TreeView) page.findView("wizard.view1");
//		if (null != view) {
//			view.reload();
//		}
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

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
