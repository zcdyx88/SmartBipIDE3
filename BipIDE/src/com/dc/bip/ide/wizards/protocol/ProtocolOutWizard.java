package com.dc.bip.ide.wizards.protocol;

import java.io.Serializable;
import java.util.List;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.dc.bip.ide.objects.ProtocolInService;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.PlatformUtils;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.ProjectNode;
import com.dc.bip.ide.views.objects.ProtocolFolder;
import com.dc.bip.ide.views.objects.ProtocolInNode;
import com.dc.bip.ide.views.objects.ProtocolOutFolder;
import com.dc.bip.ide.views.objects.ProtocolOutNode;
import com.dc.bip.ide.views.objects.TreeNode;

public class ProtocolOutWizard extends Wizard implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectName;
	 private ProtocolInService protocolInService = new ProtocolInService();
	 private AWizardPage pageWizard = null;
	  public ProtocolOutWizard(String projectName) {  
	        this.setDialogSettings(new DialogSettings("新增接入协议"));  
	        this.projectName = projectName;
	  }  

	@Override
	public void addPages() {
		this.addPage(new ProtocolOutWizardPage());
		this.addPage(new ProtocolOutDefWizardPage());
	}
	
	@Override 
	public boolean canFinish(){
		if(this.getContainer().getCurrentPage() instanceof ProtocolOutWizardPage){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void createPageControls(Composite pageContainer) {
	    //super.createPageControls(pageContainer);
	}
	

	@Override
	public boolean performFinish() {
		IWizardPage[] pages = this.getPages();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		pageWizard = (ProtocolOutDefWizardPage)pages[1];
		protocolInService = pageWizard.getProtocolInService();
		// 设置组合服务的基本信息 来自页面输入
		String serviceId = protocolInService.getProtocolName();
		try {
			String fileFolderStr = new StringBuilder("/").append(projectName).append(BipConstantUtil.ProtocolPath)
					.toString();
			IFolder folder = root.getFolder(new Path(fileFolderStr));

			String fileStr = (new StringBuilder(fileFolderStr)).append(serviceId + ".protolout").toString();
			
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
			ProtocolOutNode node= new  ProtocolOutNode(projectName);
			node.setNodeName(serviceId);
			node.setProjectName(projectName);
			node.setProtocolInService(protocolInService);
			protocolInService.setiFilePath(fileStr);
			final IFile basefile = root.getFile(basePPath);
			
			protocolInService.setMenuFilePath(basefile.getLocation().toOSString());
			HelpUtil.writeObject2(node, basefile);

			IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			TreeView view = (TreeView) wp.findView("wizard.view1");
			if (null != view) {
				node.setResource(basefile);
				ProtocolOutFolder outfolder = null;
				for (TreeItem item : view.getViewer().getTree().getItems()) {
					if (item.getText().equalsIgnoreCase(projectName)) {
						ProjectNode tmpProjectNode = (ProjectNode) item.getData();
						for (TreeNode tmpNode : tmpProjectNode.getChildren()) {
							if (tmpNode instanceof ProtocolFolder) {
								List tmpNodes = ((ProtocolFolder) tmpNode).getChildren();
								for (int i=0;i< tmpNodes.size();i++) {
									if(tmpNodes.get(i) instanceof ProtocolOutFolder){
										outfolder = (ProtocolOutFolder) tmpNodes.get(i);
										break;
									}
									
								}
							}
						}
						break;
					}
				}
				if (outfolder != null) {
					outfolder.addChild(node);
					view.getViewer().add(outfolder, node);
				}
			}

			IDE.openEditor(page, basefile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}

