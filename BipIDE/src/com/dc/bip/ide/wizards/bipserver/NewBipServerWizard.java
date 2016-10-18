package com.dc.bip.ide.wizards.bipserver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.dc.bip.ide.objects.BipServer;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.PlatformUtils;
import com.dc.bip.ide.util.SoapWsUtil;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BSNode;
import com.dc.bip.ide.views.objects.BaseFolder;
import com.dc.bip.ide.views.objects.BipServerFolder;
import com.dc.bip.ide.views.objects.BipServerNode;
import com.dc.bip.ide.views.objects.ProjectNode;
import com.dc.bip.ide.views.objects.TreeNode;

//业务服务向导
public class NewBipServerWizard extends Wizard implements INewWizard {

	private String projectName;

	public NewBipServerWizard(String projectName) {
		this.setDialogSettings(new DialogSettings("导入工程"));
		this.projectName = projectName;
	}

	@Override
	public void addPages() {
		this.addPage(new NewBipServerWizardPage());
		// this.addPage(new BaseSvcWizardP2());
	}

	@Override
	public boolean performFinish() {
		IWizardPage[] pages = this.getPages();
		NewBipServerWizardPage bippage = (NewBipServerWizardPage) pages[0];

		// System.out.println("*********************ip:"+page.getIP());
		// System.out.println("*********************jmxport:"+page.getJmxPort());
		// System.out.println("*********************regport:"+page.getRegPort());
		String id = bippage.getID();
		String ip = bippage.getIP();
		String jmxport = bippage.getJmxPort();
		String regport = bippage.getRegPort();

		BipServer bipServer = new BipServer();
		bipServer.setId(id);
		bipServer.setIp(ip);
		bipServer.setJmxport(jmxport);
		bipServer.setRegport(regport);
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		// 创建文件
		String fileFolderStr = new StringBuilder("/").append(projectName).append(BipConstantUtil.ServerPath).toString();
		IFolder folder = root.getFolder(new Path(fileFolderStr));
		if (folder != null && !folder.exists())
			try {
				HelpUtil.mkdirs(folder, true, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
				return false;
			}
		String fileStr = (new StringBuilder(fileFolderStr)).append(id + ".bipserver").toString();
		Path serverPath = new Path(fileStr);
		IFile serverfile = root.getFile(serverPath);
		if (serverfile != null && serverfile.exists()) {
			boolean confirm = PlatformUtils.showConfirm("提示", "存在同名文件，请先手动删除。是否关闭创建页面？");
			if (confirm == true){
				return true;
			}else{
				return false;
			}
		}
		try {
			serverfile.create(null, false, null);
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
		HelpUtil.writeObject(bipServer, serverfile);
		TreeView view = (TreeView) page.findView("wizard.view1");
		if (null != view) {
			BipServerNode bn = new BipServerNode(bipServer.getId());			
			bn.setProjectName(projectName);
			BipServerFolder serverFolder  = null;
			for(TreeItem item: view.getViewer().getTree().getItems())
			{
				if(item.getText().equalsIgnoreCase(projectName))
				{
					ProjectNode tmpProjectNode = (ProjectNode)item.getData();
					for( TreeNode tmpNode : tmpProjectNode.getChildren())
					{
						if(tmpNode instanceof BipServerFolder)
						{ 
							serverFolder = (BipServerFolder)tmpNode;
							break;
						}
					}
					break;
				}							
			}
			if(serverFolder != null)
			{
				serverFolder.addChild(bn);	
				view.getViewer().add(serverFolder, bn);
			}						
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
