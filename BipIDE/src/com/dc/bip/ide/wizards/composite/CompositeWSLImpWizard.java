package com.dc.bip.ide.wizards.composite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.WsdlUtil;
import com.dc.bip.ide.views.TreeView;

public class CompositeWSLImpWizard extends Wizard {
	private String projectName;
	private String serviceName;

	public CompositeWSLImpWizard(String projectName) {
		this.projectName = projectName;
		setWindowTitle("业务服务WSL导入");
	}

	@Override
	public void addPages() {
		this.addPage(new CompositeWSLImpWizardPage());
	}

	@Override
	/***
	 * 保存操作
	 */
	public boolean performFinish() {
		IWizardPage[] pages = this.getPages();

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		if (pages != null) {

			CompositeWSLImpWizardPage page = (CompositeWSLImpWizardPage) pages[0];
			String url = page.getUrl();
			serviceName = page.getName();
/*
			try {
				String fileFolderStr = new StringBuilder("/").append(projectName).append(BipConstantUtil.CompositePath)
						.toString();
				IFolder folder = root.getFolder(new Path(fileFolderStr));
//				new CompositeService().getWsdlinfo();
				List<CompServiceWsdlInfo> list = WsdlUtil.generateOperationNodes(url, projectName, folder.getLocation().toOSString());
				
				for(BSOperationNode node : list){
					if(serviceName != null && !serviceName.trim().equals("")){
						BusiSvcInfo busiSvcInfo = node.getBusiSvcInfo();
						busiSvcInfo.setName(serviceName);
						node.setBusiSvcInfo(busiSvcInfo);
					}
					
					String fileStr = (new StringBuilder(fileFolderStr)).append(node.getNodeName() + ".busi").toString();

					if (folder != null && !folder.exists())
						HelpUtil.mkdirs(folder, true, true, null);
					Path basePPath = new Path(fileStr);
					IResource baseResource = root.findMember(basePPath);
					if (baseResource != null && baseResource.exists()) {
						baseResource.delete(true, null);;
					}

					final IFile basefile = root.getFile(basePPath);
					File file = new File(basefile.getLocation().toOSString());
					FileOutputStream fileOut = new FileOutputStream(file);
					ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
					objOut.writeObject(node);
					objOut.flush();
					objOut.close();
					fileOut.close();
				}
				

				IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				TreeView view = (TreeView) wp.findView("wizard.view1");
				if (null != view) {
					view.getViewer().getTree().forceFocus();
					view.reload();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// IDE.openEditor(page, basefile);
			
			 */
		}
		
		return true;
	}
	

}
