package com.dc.bip.ide.wizards.bipserver;

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
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.objects.CompServiceBaseInfo;
import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.WsdlUtil;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.CompositeFolder;
import com.dc.bip.ide.views.objects.CompositeNode;
import com.dc.bip.ide.views.objects.ProjectNode;
import com.dc.bip.ide.views.objects.TreeNode;

public class BipSvcImpWizard extends Wizard {
	private String projectName;
	private String serviceName;
	private final String NAME = "组合服务";

	public BipSvcImpWizard(String projectName) {
		this.projectName = projectName;
		setWindowTitle("组合服务新增");
	}

	@Override
	public void addPages() {
		this.addPage(new BipSvcImpWizardPage());
	}

	@Override
	public boolean performFinish() {
		IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		//获取BIP业务视图
		TreeView view = (TreeView) wp.findView("wizard.view1");
		IWizardPage[] pages = this.getPages();
		//R
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		//P/test
		IProject project = root.getProject(projectName);
		if (pages != null) {
			BipSvcImpWizardPage page = (BipSvcImpWizardPage) pages[0];
			//导入文件目录
			String url = page.getUrl();
			//组合服务名称
			serviceName = page.getName();
			try {
				//新建dev/services/composite/文件夹
				String fileFolderStr = new StringBuilder("/").append(projectName).append("/dev/services/composite/")
						.toString();
				IFolder folder = root.getFolder(new Path(fileFolderStr));
				List<CompositeNode> list = WsdlUtil.generateCompositeNodes(url, projectName,
						folder.getLocation().toOSString(), fileFolderStr);
				for (CompositeNode node : list) {
					CompositeService compositeService = node.getCompSvc();
					if (serviceName != null && !serviceName.trim().equals("")) {
						CompServiceBaseInfo compServiceBaseInfo= compositeService.getBaseinfo();
						compServiceBaseInfo.setServiceName(serviceName);
						compositeService.setProject(project);
						compositeService.setBaseinfo(compServiceBaseInfo);
						node.setCompSvc(compositeService);
					}

					String fileStr = (new StringBuilder(fileFolderStr)).append(node.getNodeName() + ".composite").toString();

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
					compositeService.setCompositePath(basefile.getLocation().toOSString());
					File file = new File(basefile.getLocation().toOSString());
					FileOutputStream fileOut = new FileOutputStream(file);
					ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
					objOut.writeObject(node.getCompSvc());
					objOut.flush();
					objOut.close();
					fileOut.close();
					
                   // 必须重新寻找，要不然busiFolder不是同一个对象，业务节点加入后无法正常展示
					if (view != null) {
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
							compositeFolder.addChild(node);
							view.getViewer().add(compositeFolder, node);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
