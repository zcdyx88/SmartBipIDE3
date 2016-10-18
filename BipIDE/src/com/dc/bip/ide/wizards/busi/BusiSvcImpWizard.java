package com.dc.bip.ide.wizards.busi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
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
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.reficio.ws.builder.core.Wsdl;

import com.dc.bip.ide.objects.BusiSvc;
import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.WsdlUtil;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BSNode;
import com.dc.bip.ide.views.objects.BSOperationNode;
import com.dc.bip.ide.views.objects.BaseFolder;
import com.dc.bip.ide.views.objects.BusiFolder;
import com.dc.bip.ide.views.objects.ProjectNode;
import com.dc.bip.ide.views.objects.TreeNode;

public class BusiSvcImpWizard extends Wizard {
	private String projectName;
	private String serviceName;
	private final String NAME = "业务服务";

	public BusiSvcImpWizard(String projectName) {
		this.projectName = projectName;
		setWindowTitle("业务服务新增");
	}

	@Override
	public void addPages() {
		this.addPage(new BusiSvcImpWizardPage());
	}

	@Override
	public boolean performFinish() {
		IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		TreeView view = (TreeView) wp.findView("wizard.view1");
		IWizardPage[] pages = this.getPages();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		if (pages != null) {
			BusiSvcImpWizardPage page = (BusiSvcImpWizardPage) pages[0];
			// 导入文件目录
			String url = page.getUrl();
			// 104
			serviceName = page.getName();
			FileOutputStream fileOut = null;
			ObjectOutputStream objOut = null;
			try {
				// 104
				String fileFolderStr = new StringBuilder("/").append(projectName).append("/dev/services/busi/")
						.toString();
				IFolder folder = root.getFolder(new Path(fileFolderStr));
				List<BSOperationNode> list = WsdlUtil.generateOperationNodes(url, projectName,
						folder.getLocation().toOSString(), fileFolderStr);
				for (BSOperationNode node : list) {
					BusiSvcInfo busiSvcInfo = node.getBusiSvcInfo();
					if (serviceName != null && !serviceName.trim().equals("")) {
						busiSvcInfo.setName(NAME + busiSvcInfo.getId());
						node.setBusiSvcInfo(busiSvcInfo);
					}
					String fileStr = (new StringBuilder(fileFolderStr)).append(node.getNodeName() + ".busi").toString();
					if (folder != null && !folder.exists())
						HelpUtil.mkdirs(folder, true, true, null);
					Path basePPath = new Path(fileStr);
					IResource baseResource = root.findMember(basePPath);
					if (baseResource != null && baseResource.exists()) {
						baseResource.delete(true, null);
					}

					final IFile basefile = root.getFile(basePPath);
					try {
						basefile.create(null, false, null);
					} catch (CoreException e) {
						e.printStackTrace();
						return false;
					}
					busiSvcInfo.setBusiPath(basefile.getLocation().toOSString());
					File file = new File(basefile.getLocation().toOSString());
					fileOut = new FileOutputStream(file);
					objOut = new ObjectOutputStream(fileOut);
					objOut.writeObject(node);
					objOut.flush();

					// 必须重新寻找，要不然busiFolder不是同一个对象，业务节点加入后无法正常展示
					if (view != null) {
						BusiFolder busiFolder = null;
						for (TreeItem item : view.getViewer().getTree().getItems()) {
							if (item.getText().equalsIgnoreCase(projectName)) {
								ProjectNode tmpProjectNode = (ProjectNode) item.getData();
								for (TreeNode tmpNode : tmpProjectNode.getChildren()) {
									if (tmpNode instanceof BusiFolder) {
										busiFolder = (BusiFolder) tmpNode;
										break;
									}
								}
								break;
							}
						}
						if (busiFolder != null) {
//							node.setResource(basefile);
							busiFolder.addChild(node);
							view.getViewer().add(busiFolder, node);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != objOut) {
					try {
						objOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
				if (null != fileOut) {
					try {
						fileOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}
		return true;
	}
}
