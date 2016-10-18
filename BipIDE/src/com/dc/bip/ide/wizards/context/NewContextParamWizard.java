package com.dc.bip.ide.wizards.context;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.views.TreeView;

//业务服务向导
public class NewContextParamWizard extends Wizard implements INewWizard {	
	private Table table;

	public NewContextParamWizard(Table table) {
		this.table = table;
	}

	@Override
	public void addPages() {
		this.addPage(new NewContextParamWizardPage());
	}

	@Override
	public boolean performFinish() {
		IWizardPage[] pages = this.getPages();
		NewContextParamWizardPage publiccontextpage = (NewContextParamWizardPage) pages[0];
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText(0,publiccontextpage.getPararmID());
		tableItem.setText(1, publiccontextpage.getPararmType());
		tableItem.setText(2, publiccontextpage.getPararmValue());
		tableItem.setText(3, publiccontextpage.getPararmDesc());
		tableItem.setData(BipConstantUtil.SessionParamKey);
		// System.out.println("*********************ip:"+page.getIP());
		// System.out.println("*********************jmxport:"+page.getJmxPort());
		// System.out.println("*********************regport:"+page.getRegPort());
//		String id = bippage.getID();
//		String ip = bippage.getIP();
//		String jmxport = bippage.getJmxPort();
//		String regport = bippage.getRegPort();
//
//		BipServer bipServer = new BipServer();
//		bipServer.setId(id);
//		bipServer.setIp(ip);
//		bipServer.setJmxport(jmxport);
//		bipServer.setRegport(regport);
//		
//		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//		// 创建文件
//		String fileFolderStr = new StringBuilder("/").append(projectName).append(BipConstantUtil.ServerPath).toString();
//		IFolder folder = root.getFolder(new Path(fileFolderStr));
//		if (folder != null && !folder.exists())
//			try {
//				HelpUtil.mkdirs(folder, true, true, null);
//			} catch (CoreException e) {
//				e.printStackTrace();
//				return false;
//			}
//		String fileStr = (new StringBuilder(fileFolderStr)).append(id + ".bipserver").toString();
//		Path serverPath = new Path(fileStr);
//		IFile serverfile = root.getFile(serverPath);
//		try {
//			serverfile.create(null, false, null);
//		} catch (CoreException e) {
//			e.printStackTrace();
//			return false;
//		}
//		HelpUtil.writeObject(bipServer, serverfile);
		TreeView view = (TreeView) page.findView("wizard.view1");
		if (null != view) {
			view.reload();
		}
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
