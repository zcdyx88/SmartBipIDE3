package com.dc.bip.ide.wizards;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.reficio.ws.builder.core.Wsdl;

import com.dc.bip.ide.objects.BusiSvc;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.PlatformUtils;
import com.dc.bip.ide.util.WsdlUtil;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BSNode;
import com.dc.bip.ide.views.objects.BSOperationNode;
import com.dc.bip.ide.views.objects.BaseFolder;
import com.dc.bip.ide.views.objects.FlowFolder;
import com.dc.bip.ide.views.objects.FlowNode;
import com.dc.bip.ide.views.objects.ProjectNode;
import com.dc.bip.ide.views.objects.TreeNode;

public class FlowNewWizard extends Wizard {
	private String projectName;

	public FlowNewWizard(String projectName) {
		this.projectName = projectName;
		setWindowTitle("新建流程");
	}

	@Override
	public void addPages() {
		this.addPage(new FlowNewWizardPage());
	}

	@Override
	/**
	 * 点击导入按钮触发
	 */
	public boolean performFinish() {
		IWizardPage[] pages = this.getPages();

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		if (pages != null) {

			FlowNewWizardPage page = (FlowNewWizardPage) pages[0];
			String flowName = page.getFlowName();

			try {
				String fileFolderStr = new StringBuilder("/").append(projectName).append("/dev/services/flow/")
						.toString();
				IFolder folder = root.getFolder(new Path(fileFolderStr));
				
				String fileStr = (new StringBuilder(fileFolderStr)).append(flowName + ".bpmn").toString();

				
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
				InputStream inputStreamJava = new ByteArrayInputStream(("").getBytes());
				basefile.create(inputStreamJava, false, null);	

				IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				TreeView view = (TreeView) wp.findView("wizard.view1");
				if (null != view) {
					FlowNode bn = new FlowNode(flowName);
					
					bn.setResource(basefile);
					FlowFolder flowFolder  = null;
					for(TreeItem item: view.getViewer().getTree().getItems())
					{
						if(item.getText().equalsIgnoreCase(projectName))
						{
							ProjectNode tmpProjectNode = (ProjectNode)item.getData();
							for( TreeNode tmpNode : tmpProjectNode.getChildren())
							{
								if(tmpNode instanceof FlowFolder)
								{ 
									flowFolder = (FlowFolder)tmpNode;
									break;
								}
							}
							break;
						}							
					}
					if(flowFolder != null)
					{
						flowFolder.addChild(bn);	
						view.getViewer().add(flowFolder, bn);
					}						
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}

			// IDE.openEditor(page, basefile);
		}
		return true;
	}
	

}
