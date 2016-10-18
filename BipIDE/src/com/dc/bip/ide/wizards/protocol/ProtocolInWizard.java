package com.dc.bip.ide.wizards.protocol;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
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
import com.dc.bip.ide.views.objects.ProtocolInFolder;
import com.dc.bip.ide.views.objects.ProtocolInNode;
import com.dc.bip.ide.views.objects.TreeNode;

public class ProtocolInWizard extends Wizard implements INewWizard {
	private String projectName;
	private ProtocolInService protocolInService = new ProtocolInService();
	private AWizardPage pageWizard = null;
	private ProtocolInWizardPage p1 = new ProtocolInWizardPage();
	private ProtocolInDefWizardPage p2 = new ProtocolInDefWizardPage();

	public ProtocolInWizard(String projectName) {
		this.setDialogSettings(new DialogSettings("新增接入协议"));
		this.projectName = projectName;
	}
	
	  /* 
     * (non-Javadoc) 
     * @see org.eclipse.jface.wizard.Wizard#addPage(org.eclipse.jface.wizard.IWizardPage) 
     */ 
//    public void addPage(IWizardPage page) { 
//    	 // 重写父类方法，添加向导页，并将向导页的向导设置为当前对象
//    } 
//
//	
//	public IWizardPage getNextPage(IWizardPage page) {
//		return pages.get(1);
//	}

	@Override
	public void createPageControls(Composite pageContainer) {
	    //super.createPageControls(pageContainer);
	}
	
	@Override
	public void addPages() {
		this.addPage(p1);
		this.addPage(p2);
//		if(!isFirstFlag){
//			this.addPage(p1);
//			this.addPage(p3);
//			//pages.add(p1);
//			isFirstFlag = true;
//		}else{
//			String protoType = this.getDialogSettings().get("protocol");
//			if ("WebService".equals(protoType)) {
//				p2 = new ProtocolInWsWizardPage();
//				this.addPage(p2);
//			 } else if ("HTTP".equals(protoType)) {
//				 p2 = new ProtocolInWsWizardPage();
//				 this.addPage(p2);
//			 } else if ("TCP".equals(protoType)) {
//				 p2 = new ProtocolInTcpWizardPage();
//				 this.addPage(p2 );
//			 }
//				//pages.add(p2);
//		}
//		//this.addPage(p3);
	}

	@Override
	public boolean performFinish() {
		IWizardPage[] pages = this.getPages();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		pageWizard = (ProtocolInDefWizardPage)pages[1];
		protocolInService = pageWizard.getProtocolInService();
		// 设置组合服务的基本信息 来自页面输入
		String serviceId = protocolInService.getProtocolName();
		try {
			String fileFolderStr = new StringBuilder("/").append(projectName).append(BipConstantUtil.ProtocolPath)
					.toString();
			IFolder folder = root.getFolder(new Path(fileFolderStr));

			String fileStr = (new StringBuilder(fileFolderStr)).append(serviceId + ".protolin").toString();

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
			protocolInService.setiFilePath(fileStr);
			ProtocolInNode node= new  ProtocolInNode(projectName);
			node.setNodeName(serviceId);
			node.setProjectName(projectName);
			node.setProtocolInService(protocolInService);
			final IFile basefile = root.getFile(basePPath);
			
			protocolInService.setiFilePath(fileStr);
			protocolInService.setMenuFilePath(basefile.getLocation().toOSString());
			HelpUtil.writeObject2(node, basefile);

			IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			TreeView view = (TreeView) wp.findView("wizard.view1");
			if (null != view) {
				node.setResource(basefile);
				ProtocolInFolder infolder = null;
				for (TreeItem item : view.getViewer().getTree().getItems()) {
					if (item.getText().equalsIgnoreCase(projectName)) {
						ProjectNode tmpProjectNode = (ProjectNode) item.getData();
						for (TreeNode tmpNode : tmpProjectNode.getChildren()) {
							if (tmpNode instanceof ProtocolFolder) {
								List tmpNodes = ((ProtocolFolder) tmpNode).getChildren();
								for (int i=0;i< tmpNodes.size();i++) {
									if(tmpNodes.get(i) instanceof ProtocolInFolder){
										infolder = (ProtocolInFolder) tmpNodes.get(i);
										break;
									}
								}
							}
						}
						break;
					}
				}
				if (infolder != null) {
					infolder.addChild(node);
					view.getViewer().add(infolder, node);
				}
			}

			IDE.openEditor(page, basefile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean canFinish() {
		if (this.getContainer().getCurrentPage() == p1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

}
