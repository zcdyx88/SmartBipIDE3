package com.dc.bip.ide.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.views.objects.BSNode;
import com.dc.bip.ide.views.objects.BSOperationNode;
import com.dc.bip.ide.views.objects.BipServerNode;
import com.dc.bip.ide.views.objects.CompositeNode;
import com.dc.bip.ide.views.objects.ExpressionNode;
import com.dc.bip.ide.views.objects.FlowNode;
import com.dc.bip.ide.views.objects.ProtocolInNode;
import com.dc.bip.ide.views.objects.ProtocolOutNode;

public class TreeView extends ViewPart {

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;

	
	private Action doubleClickAction;

	
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public TreeView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {		
		
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);		
		viewer.setContentProvider(new TreeViewContentProvider());
		viewer.setLabelProvider(new TreeViewLabelProvider());
		
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());	
		//makeActions();
	    hookContextMenu();
		hookDoubleClickAction();
		//contributeToActionBars();
	}

	// 挂接菜单
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				//TreeView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	/*private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(action3);
		manager.add(new Separator());
		// drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		// manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		// drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		
		 * action1 = new Action() { public void run() { showMessage(
		 * "Action 1 executed"); } };
		 
		action1 = new Action()
				{
			
				};
		action1.setText("开发");
		action1.setToolTipText("基础服务开发");
		action1.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			public void run() {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				BaseSvcW bsw = new BaseSvcW();
		        WizardDialog dialog = new WizardDialog(shell, new ImportBaseSvcW());
		        dialog.setTitle("导入基础服务");
		        dialog.open(); 
			}
		};
		action2.setText("导入");
		action2.setToolTipText("基础服务导入");
		action2.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	
		action3 = new Action() {
			public void run() {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				BaseSvcW bsw = new BaseSvcW();
		        WizardDialog dialog = new WizardDialog(shell, new ImportBaseSvcW());
		        dialog.setTitle("导入基础服务");
		        dialog.open();    
			}
		};
		action3.setText("维护");
		action3.setToolTipText("基础服务维护");
		action3.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
	}*/
	
	/**
	 * 增加双击事件监听
	 */
	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
		            Object first = ((IStructuredSelection) selection).getFirstElement();
		            if (first instanceof BSNode) {
		            	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		            	try {
							IDE.openEditor(page, ((BSNode) first).getBaseService().getBaseFile());
						} catch (PartInitException e) {
							e.printStackTrace();
						}
		            }
		            //使用编辑器打开业务节点
		            if (first instanceof BSOperationNode) {
		            	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		            	try {
							IDE.openEditor(page, ((BSOperationNode) first).getResource());
						} catch (PartInitException e) {
							e.printStackTrace();
						}
		            }
		            //双击查看流程文件
		            if (first instanceof FlowNode) {
		            	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		            	
		            	try {
							IDE.openEditor(page, ((FlowNode) first).getResource());
						} catch (PartInitException e) {
							e.printStackTrace();
						}
		            }		  
		            //双击查看组合服务节点
		            if (first instanceof CompositeNode) {
		            	String projectName = ((CompositeNode) first).getProjectName();
		            	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		            	String nodeName = ((CompositeNode) first).getNodeName();
		    			String compositeFilePath = (new StringBuilder("/")).append(projectName).append(BipConstantUtil.CompositePath).append(nodeName).append(".composite").toString();
		    		    IFile compositeFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(compositeFilePath));
		    		    String sdaFilePath = (new StringBuilder("/")).append(projectName).append(BipConstantUtil.CompositePath).append(nodeName).append(".xsd").toString();
		    		    IFile sdaFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(sdaFilePath));
		    		    String wsdlFilePath = (new StringBuilder("/")).append(projectName).append(BipConstantUtil.CompositePath).append(nodeName).append(".wsdl").toString();
		    		    IFile wsdlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(wsdlFilePath));
		    		    
		    		    String  codeFilePath = (new StringBuilder("/")).append(projectName).append(BipConstantUtil.CompositePath).append(nodeName).append(".code").toString();
		    		    IFile codeFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(codeFilePath));
		    		    
		    		    IFile[] tmp = new IFile[]{compositeFile};
		            	try {
							IDE.openEditors(page, tmp);
						} catch (Exception e) {
							e.printStackTrace();
						}
		            }
		            if (first instanceof BipServerNode) {
		            	String projectName = ((BipServerNode) first).getProjectName();
		            	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		            	String nodeName = ((BipServerNode) first).getNodeName();
		    			String bipserverFilePath = (new StringBuilder("/")).append(projectName).append(BipConstantUtil.ServerPath).append(nodeName).append(".bipserver").toString();
		    		    IFile bipserverFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(bipserverFilePath));
		            	try {
							IDE.openEditor(page, bipserverFile);
						} catch (Exception e) {
							e.printStackTrace();
						}
		            }	
		            //输入协议
		            if (first instanceof ProtocolInNode) {
		            	String projectName = ((ProtocolInNode) first).getProjectName();
		            	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		            	String nodeName = ((ProtocolInNode) first).getProtocolInService().getProtocolName();
		    			String bipserverFilePath = (new StringBuilder("/")).append(projectName).append(BipConstantUtil.ProtocolPath).append(nodeName).append(".protolin").toString();
		    		    IFile protocolInFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(bipserverFilePath));
		            	try {
		            		IDE.openEditor(page, protocolInFile);
						} catch (Exception e) {
							e.printStackTrace();
						}
		            }	
		            
		            //输出协议
		            if (first instanceof ProtocolOutNode) {
		            	String projectName = ((ProtocolOutNode) first).getProjectName();
		            	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		            	String nodeName = ((ProtocolOutNode) first).getProtocolInService().getProtocolName();
		    			String bipserverFilePath = (new StringBuilder("/")).append(projectName).append(BipConstantUtil.ProtocolPath).append(nodeName).append(".protolout").toString();
		    		    IFile protocolInFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(bipserverFilePath));
		            	try {
		            		IDE.openEditor(page, protocolInFile);
						} catch (Exception e) {
							e.printStackTrace();
						}
		            }	
		            
		            //表达式管理
		            if (first instanceof ExpressionNode) {
		            	String projectName = ((ExpressionNode) first).getProjectName();
		            	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		            	String nodeName = ((ExpressionNode) first).getParams().get("nodeName");
		    			String bipserverFilePath = (new StringBuilder("/")).append(projectName).append(BipConstantUtil.ExpressionPath).append(nodeName).append(".express").toString();
		    		    IFile protocolInFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(bipserverFilePath));
		            	try {
		            		IDE.openEditor(page, protocolInFile);
						} catch (Exception e) {
							e.printStackTrace();
						}
		            }	
		            

		        }
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public void reload()
	{
		viewer.refresh();
	}

	public TreeViewer getViewer() {
		return viewer;
	}
	
}
