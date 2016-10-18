package com.dc.bip.ide.wizards.busi;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.objects.ProtocolInService;
import com.dc.bip.ide.repository.impl.ProtocolServiceRepository;
import com.dc.bip.ide.service.ProtocolService;
import com.dc.bip.ide.util.PlatformUtils;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BSOperationNode;
import com.dc.bip.ide.views.objects.BusiFolder;
import com.dc.bip.ide.views.objects.ProjectNode;
import com.dc.bip.ide.views.objects.TreeNode;

public class BusiSvcAddWizard extends Wizard {
	private String projectName;
	private String serviceName;
	private final String NAME = "业务服务";

	public BusiSvcAddWizard(String projectName) {
		this.projectName = projectName;
		setWindowTitle("业务服务新增");
	}

	@Override
	public void addPages() {
		this.addPage(new BusiSvcAddWizardPage(this.projectName));
	}

	@Override
	public boolean performFinish() {
		IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		TreeView view = (TreeView) wp.findView("wizard.view1");
		IWizardPage[] pages = this.getPages();
		if (pages != null) {
			BusiSvcAddWizardPage page = (BusiSvcAddWizardPage) pages[0];
			String serviceId = page.getServiceID();
			serviceName = page.getServiceName();
			String protocolName = page.getProtocol();
			String packUnPackMode = page.getPackUnPack();
			String serviceDesc = page.getServiceDesc();
			BSOperationNode node = new BSOperationNode(serviceId);
			node.setProjectName(projectName);
			BusiSvcInfo busiSvcInfo = new BusiSvcInfo();
			busiSvcInfo.setProtocolName(protocolName);
			busiSvcInfo.setId(serviceId);
			busiSvcInfo.setName(serviceName);
			busiSvcInfo.setDecscription(serviceDesc);
			busiSvcInfo.setPackUnPackMode(packUnPackMode);
			busiSvcInfo.setProjectName(projectName);
			ProtocolService protocolService = ProtocolServiceRepository.getInstance().get(projectName);
			ProtocolInService protocolInfo = protocolService.getOutProtocolInfo(protocolName);
			if (null != protocolInfo) {
				busiSvcInfo.setProtocolType(protocolInfo.getProtocolType());
			}
			node.setBusiSvcInfo(busiSvcInfo);
			try {
				// 如果存在该.busi 文件 就删除
				IFile busiSvcNodeFile = node.getResource();
				if (busiSvcNodeFile.exists()) {
					boolean confirm = PlatformUtils.showConfirm("提示", "存在同名文件，请先手动删除。是否关闭创建页面？");
					if (confirm == true){
						return true;
					}else{
						return false;
					}
				}
				//在node中设置自己的持久化路径 
				node.persist();
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
						busiFolder.addChild(node);
						view.getViewer().add(busiFolder, node);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}