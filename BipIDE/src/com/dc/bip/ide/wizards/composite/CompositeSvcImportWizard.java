package com.dc.bip.ide.wizards.composite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.WsdlUtil;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BSOperationNode;

public class CompositeSvcImportWizard extends Wizard {
	private String projectName;
	private String serviceName;
	private final String NAME = "业务服务";

	public CompositeSvcImportWizard(String projectName) {
		this.projectName = projectName;
		setWindowTitle("组合服务");
	}

	@Override
	public void addPages() {
		this.addPage(new CompositeSvcImportWizardPage());
	}

	@Override
	public boolean performFinish() {
		IWizardPage[] pages = this.getPages();

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		if (pages != null) {

			CompositeSvcImportWizardPage page = (CompositeSvcImportWizardPage) pages[0];
			String url = page.getUrl();
			serviceName = page.getName();

			try {
				String fileFolderStr = new StringBuilder("/").append(projectName).append(BipConstantUtil.CompositePath)
						.toString();
				IFolder folder = root.getFolder(new Path(fileFolderStr));
				
				List<BSOperationNode> list = WsdlUtil.generateOperationNodes(url, projectName,
						folder.getLocation().toOSString(),fileFolderStr);
				
				for(BSOperationNode node : list){
					if(serviceName != null && !serviceName.trim().equals("")){
						BusiSvcInfo busiSvcInfo = node.getBusiSvcInfo();
						busiSvcInfo.setName( NAME + busiSvcInfo.getId());
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
					try {
						basefile.create(null, false, null);
					} catch (CoreException e) {
						e.printStackTrace();
						return false;
					}
					//
					File file = new File(basefile.getLocation().toOSString());
					FileOutputStream fileOut = new FileOutputStream(file);
					ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
					objOut.writeObject(node);
					objOut.flush();
					objOut.close();
//					fileOut.flush();
//					fileOut.close();
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
		}
		try {
			project.refreshLocal(0, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return true;
	}
	

}
