package com.dc.bip.ide.popup.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BaseFolder;
import com.dc.bip.ide.views.objects.BipServerFolder;
import com.dc.bip.ide.views.objects.BusiFolder;
import com.dc.bip.ide.views.objects.CompositeFolder;
import com.dc.bip.ide.views.objects.FlowFolder;
import com.dc.bip.ide.views.objects.ProjectNode;
import com.dc.bip.ide.views.objects.ProtocolFolder;
import com.dc.bip.ide.views.objects.ProtocolInFolder;
import com.dc.bip.ide.views.objects.ProtocolOutFolder;
import com.dc.bip.ide.views.objects.TreeNode;

public class RefreshViewAction implements IObjectActionDelegate {

	private TreeNode node;

	@Override
	public void run(IAction action) {
		TreeView view = (TreeView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView("wizard.view1");
		Object[] objects = view.getViewer().getExpandedElements();
		view.reload();
		//刷新后展开刷新展开的目录
		if (view != null) {
			ProjectNode projectNode = null;
			BusiFolder busiFolder = null;
			BaseFolder baseFolder = null;
			FlowFolder flowFolder = null;
			BipServerFolder serverFolder = null;
			CompositeFolder compositeFolder = null;
			ProtocolFolder protocolFolder = null;
			ProtocolInFolder protocolInFolder = null;
			ProtocolOutFolder protocolOutFolder = null;
			for (TreeItem item : view.getViewer().getTree().getItems()) {
				if (item.getText().equalsIgnoreCase(node.getProjectName())) {
					projectNode = (ProjectNode) item.getData();
					for (TreeNode tmpNode : projectNode.getChildren()) {
						if (tmpNode instanceof BusiFolder) {
							busiFolder = (BusiFolder) tmpNode;
						} else if (tmpNode instanceof BaseFolder) {
							baseFolder = (BaseFolder) tmpNode;
						} else if (tmpNode instanceof FlowFolder) {
							flowFolder = (FlowFolder) tmpNode;
						} else if (tmpNode instanceof BipServerFolder) {
							serverFolder = (BipServerFolder) tmpNode;
						} else if (tmpNode instanceof CompositeFolder) {
							compositeFolder = (CompositeFolder)tmpNode;
						} else if (tmpNode instanceof ProtocolFolder) {
							protocolFolder = (ProtocolFolder)tmpNode;
							List<TreeNode> list = protocolFolder.getChildren();
							for(int i=0;i<list.size();i++){
								if(list.get(i) instanceof ProtocolInFolder){
									protocolInFolder =(ProtocolInFolder) list.get(i);
								}else if(list.get(i) instanceof ProtocolOutFolder){
									protocolOutFolder =(ProtocolOutFolder) list.get(i);
								}
							}
						}
					}
					break;
				}
			}
			List<Object> expandElements = new ArrayList<Object>();
			for(Object tmpNode :objects)
			{
				if (tmpNode instanceof BusiFolder) {
					expandElements.add(busiFolder);
				} else if (tmpNode instanceof BaseFolder) {
					expandElements.add(baseFolder);
				} else if (tmpNode instanceof FlowFolder) {
					expandElements.add(flowFolder);
				} else if (tmpNode instanceof BipServerFolder) {
					expandElements.add(serverFolder);
				}else if (tmpNode instanceof ProjectNode) {
					expandElements.add(projectNode);
				}else if (tmpNode instanceof CompositeFolder) {
					expandElements.add(compositeFolder);
				}else if (tmpNode instanceof ProtocolFolder) {
					expandElements.add(protocolFolder);
					expandElements.add(protocolInFolder);
					expandElements.add(protocolOutFolder);
					
				}
			}
			view.getViewer().setExpandedElements(expandElements.toArray());
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.getFirstElement() instanceof TreeNode) {
				node = (TreeNode) (structuredSelection.getFirstElement());
			}
		}
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

}
