package com.dc.bip.ide.editors.composite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.ide.IDE;

import com.dc.bip.ide.gef.model.Param;
import com.dc.bip.ide.gef.model.ParamScope;
import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.objects.IService;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.PlatformUtils;
import com.dc.bip.ide.wizards.context.EditContextParamWizard;
import com.dc.bip.ide.wizards.context.NewContextParamWizard;

public class ContextPage extends Composite {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table globalContext;
	private Table sessionContext;	
	private CompositeService compositeService;
	
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ContextPage(final Composite parent,int style, CompositeService compositeService2) {
		
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		this.compositeService = compositeService2;
		
		ScrolledForm scrldfrmNewScrolledform = formToolkit.createScrolledForm(this);
		scrldfrmNewScrolledform.setTouchEnabled(true);
		scrldfrmNewScrolledform.setDelayedReflow(true);
		scrldfrmNewScrolledform.setExpandVertical(true);
		scrldfrmNewScrolledform.setExpandHorizontal(true);
		scrldfrmNewScrolledform.setMinHeight(800);
		scrldfrmNewScrolledform.setMinWidth(800);
		formToolkit.paintBordersFor(scrldfrmNewScrolledform);
		scrldfrmNewScrolledform.setText("\u4E0A\u4E0B\u6587\u7F16\u8F91");
		
		Section sctnNewSection = formToolkit.createSection(scrldfrmNewScrolledform.getBody(), Section.TITLE_BAR);
		sctnNewSection.setBounds(10, 20, 756, 227);
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText("\u516C\u5171\u4E0A\u4E0B\u6587");
		sctnNewSection.setExpanded(true);
		
		Composite composite = formToolkit.createComposite(sctnNewSection, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		sctnNewSection.setClient(composite);
		
		globalContext = formToolkit.createTable(composite, SWT.NONE);
		globalContext.setBounds(0, 10, 600, 166);
		formToolkit.paintBordersFor(globalContext);
		globalContext.setHeaderVisible(true);
		globalContext.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(globalContext, SWT.NONE);
		tblclmnNewColumn.setWidth(150);
		tblclmnNewColumn.setText("ID");

		TableColumn tblclmnNewColumn_1 = new TableColumn(globalContext, SWT.NONE);
		tblclmnNewColumn_1.setWidth(150);
		tblclmnNewColumn_1.setText("类型");
		
		TableColumn tblclmnNewColumn_2 = new TableColumn(globalContext, SWT.NONE);
		tblclmnNewColumn_2.setWidth(150);
		tblclmnNewColumn_2.setText("值");

		TableColumn tblclmnNewColumn_3 = new TableColumn(globalContext, SWT.NONE);
		tblclmnNewColumn_3.setWidth(182);
		tblclmnNewColumn_3.setText("描述");
		
		//显示global参数
		List<Param> globalParams = HelpUtil.readGlobalParams(compositeService);
		TableItem globalItem;
		for (Param tmpParam : globalParams)
		{
			globalItem = new TableItem(globalContext, SWT.None);
			globalItem.setText(0, tmpParam.getName());
			globalItem.setText(1,tmpParam.getType());
			globalItem.setText(2,tmpParam.getValue());
			globalItem.setText(3, tmpParam.getDesc());
		}
		
		Button btnNewButton = formToolkit.createButton(composite, "\u65B0\u589E", SWT.NONE);
		btnNewButton.setBounds(654, 25, 80, 27);
		btnNewButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub	
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				NewContextParamWizard bsw = new NewContextParamWizard(globalContext);
		        WizardDialog dialog = new WizardDialog(shell, bsw);
		        dialog.setTitle("增加上下文参数");
		        dialog.open();  
			}			
		});
		
		Button btnNewButton_1 = formToolkit.createButton(composite, "\u7F16\u8F91", SWT.NONE);
		btnNewButton_1.setBounds(654, 58, 80, 27);
		btnNewButton_1.addMouseListener(new MouseListener(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void mouseUp(MouseEvent e) {
				int selectedIndex = globalContext.getSelectionIndex();
				TableItem selectedItem = globalContext.getItem(selectedIndex);
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				EditContextParamWizard bsw = new EditContextParamWizard(selectedItem);
		        WizardDialog dialog = new WizardDialog(shell, bsw);
		        dialog.setTitle("编辑上下文参数");
		        dialog.open();  
			}			
		});
		
		Button btnNewButton_2 = formToolkit.createButton(composite, "\u5220\u9664", SWT.NONE);
		btnNewButton_2.setBounds(654, 91, 80, 27);
		btnNewButton_2.addMouseListener(new MouseListener(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void mouseUp(MouseEvent e) {
	/*			int selectedIndex = sessionContext.getSelectionIndex();
				TableItem selectedItem = sessionContext.getItem(selectedIndex);*/
				globalContext.remove(globalContext.getSelectionIndex());
			}			
		});
		
		Button globalSaveButton = formToolkit.createButton(composite, "\u4FDD\u5B58", SWT.NONE);
		globalSaveButton.setBounds(654, 127, 80, 27);
		globalSaveButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				List<Param> globalParams = new ArrayList<Param>();
				for(TableItem item : globalContext.getItems())
				{
					    Param globalParam = new Param(item.getText(0));
					    globalParam.setType(item.getText(1));
					    globalParam.setValue(item.getText(2));
					    globalParam.setDesc(item.getText(3));
					    globalParam.setOperationId("global");
					    globalParam.setScope(ParamScope.Global);
					    globalParams.add(globalParam);
				}
				HelpUtil.wirteGlobalParams(compositeService, globalParams);
			}			
		});
		
		
		Section sctnNewSection_1 = formToolkit.createSection(scrldfrmNewScrolledform.getBody(), Section.TITLE_BAR);
		sctnNewSection_1.setBounds(10, 253, 756, 221);
		formToolkit.paintBordersFor(sctnNewSection_1);
		sctnNewSection_1.setText("\u4F1A\u8BDD\u4E0A\u4E0B\u6587");
		
		Composite composite_1 = formToolkit.createComposite(sctnNewSection_1, SWT.NONE);
		formToolkit.paintBordersFor(composite_1);
		sctnNewSection_1.setClient(composite_1);
		
		sessionContext = formToolkit.createTable(composite_1, SWT.NONE);
		sessionContext.setBounds(0, 10, 600, 152);
		formToolkit.paintBordersFor(sessionContext);
		sessionContext.setHeaderVisible(true);
		sessionContext.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn_4 = new TableColumn(sessionContext, SWT.NONE);
		tblclmnNewColumn_4.setWidth(150);
		tblclmnNewColumn_4.setText("ID");

		TableColumn tblclmnNewColumn_5 = new TableColumn(sessionContext, SWT.NONE);
		tblclmnNewColumn_5.setWidth(100);
		tblclmnNewColumn_5.setText("类型");
		
		TableColumn tblclmnNewColumn_6 = new TableColumn(sessionContext, SWT.NONE);
		tblclmnNewColumn_6.setWidth(100);
		tblclmnNewColumn_6.setText("值");

		TableColumn tblclmnNewColumn_7 = new TableColumn(sessionContext, SWT.NONE);
		tblclmnNewColumn_7.setWidth(250);
		tblclmnNewColumn_7.setText("描述");
		
		String compositeSvcId = compositeService.getBaseinfo().getServiceId();
		StringBuilder mapFolder = new StringBuilder();
		mapFolder.append(BipConstantUtil.CompositePath).append(compositeSvcId).append("/map/");
		StringBuilder mapFile = new StringBuilder();
		
		TableItem item =  new TableItem(sessionContext,SWT.None);
		item.setText(0,compositeService.getBaseinfo().getServiceId()+" IN");
		item.setText(1,"Object");
		item.setText(3,"组合服务"+compositeService.getBaseinfo().getServiceId()+"请求参数");
		item.setData(mapFile.append(mapFolder).append(compositeSvcId).append("_IN.map").toString());
		
		StringBuilder mapFile2 = new StringBuilder();
		item =  new TableItem(sessionContext,SWT.None);
		item.setText(0,compositeService.getBaseinfo().getServiceId()+" OUT");		
		item.setText(1,"Object");
		item.setText(3,"组合服务"+compositeService.getBaseinfo().getServiceId()+"响应参数");
		item.setData(mapFile2.append(mapFolder).append(compositeSvcId).append("_OUT.map").toString());
		
		
		for(IService service: this.compositeService.getBusiSvcNodesFromFlow())
		{
		
			    StringBuilder busiSvcInFile = new StringBuilder();
				BusiSvcInfo busiSvcInfo = (BusiSvcInfo)service;
				 item = new TableItem(sessionContext, SWT.None);
				item.setText(0, busiSvcInfo.getId()+" IN");
				item.setText(1,"Object");
				item.setText(3, "业务服务"+busiSvcInfo.getId()+"请求参数");
				item.setData(busiSvcInFile.append(mapFolder).append(compositeSvcId).append("_").append(busiSvcInfo.getId()).append("_IN.map").toString());
				
				StringBuilder busiSvcOutFile = new StringBuilder();
				item = new TableItem(sessionContext, SWT.None);
				item.setText(0, busiSvcInfo.getId()+" OUT");
				item.setText(1,"Object");
				item.setText(3, "业务服务"+busiSvcInfo.getId()+"响应参数");	
				item.setData(busiSvcOutFile.append(mapFolder).append(compositeSvcId).append("_").append(busiSvcInfo.getId()).append("_OUT.map").toString());
		}
		//显示session参数
		List<Param> sessionParams = HelpUtil.readSessionParams(compositeService);
		
		for (Param tmpParam : sessionParams)
		{
			    item = new TableItem(sessionContext, SWT.None);
				item.setText(0, tmpParam.getName());
				item.setText(1,tmpParam.getType());
				item.setText(2,tmpParam.getValue());
				item.setText(3, tmpParam.getDesc());
				item.setData(BipConstantUtil.SessionParamKey);
		}
		
		Button addButton = formToolkit.createButton(composite_1, "\u65B0\u589E", SWT.NONE);
		addButton.setBounds(654, 20, 80, 27);
		addButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub	
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				NewContextParamWizard bsw = new NewContextParamWizard(sessionContext);
		        WizardDialog dialog = new WizardDialog(shell, bsw);
		        dialog.setTitle("增加上下文参数");
		        dialog.open();  
			}			
		});
		
		Button editButton = formToolkit.createButton(composite_1, "\u7F16\u8F91", SWT.NONE);
		editButton.setBounds(654, 53, 80, 27);
		editButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void mouseUp(MouseEvent e) {
				int selectedIndex = sessionContext.getSelectionIndex();
				TableItem selectedItem = sessionContext.getItem(selectedIndex);
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				EditContextParamWizard bsw = new EditContextParamWizard(selectedItem);
		        WizardDialog dialog = new WizardDialog(shell, bsw);
		        dialog.setTitle("编辑上下文参数");
		        dialog.open();  
			}			
		});
		
		Button delButton = formToolkit.createButton(composite_1, "\u5220\u9664", SWT.NONE);
		delButton.setBounds(654, 86, 80, 27);
		delButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void mouseUp(MouseEvent e) {
	/*			int selectedIndex = sessionContext.getSelectionIndex();
				TableItem selectedItem = sessionContext.getItem(selectedIndex);*/
				TableItem item = sessionContext.getItem(sessionContext.getSelectionIndex());
				if(item.getData() instanceof String  && ((String)item.getData()).equalsIgnoreCase(BipConstantUtil.SessionParamKey))
				{
					sessionContext.remove(sessionContext.getSelectionIndex());
				}else
				{
					PlatformUtils.showInfo("提示", "只能删除自定义的上下文参数");
					return;
				}
				
			}			
		});
		
		
		Button saveButton = formToolkit.createButton(composite_1, "\u4FDD\u5B58", SWT.NONE);
		saveButton.setBounds(654, 122, 80, 27);
		scrldfrmNewScrolledform.setMinSize(new Point(800, 800));
		saveButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				List<Param> sessionParams = new ArrayList<Param>();
				for(TableItem item : sessionContext.getItems())
				{					
					if(BipConstantUtil.SessionParamKey.equalsIgnoreCase((String)item.getData()))
					{
						Param sessionParam = new Param(item.getText(0));
						sessionParam.setType(item.getText(1));
						sessionParam.setValue(item.getText(2));
						sessionParam.setDesc(item.getText(3));
						sessionParam.setOperationId("constant");
						sessionParam.setScope(ParamScope.Session);
						sessionParams.add(sessionParam);
					}
				}
				HelpUtil.wirteSessionParams(compositeService, sessionParams);			
			}			
		});

		Button mapButton = formToolkit.createButton(composite_1, "\u6620\u5C04", SWT.NONE);
		mapButton.setBounds(654, 158, 80, 27);
		mapButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseUp(MouseEvent e) {
				if(null == compositeService.getFlowname() || 0 == compositeService.getFlowname().length())
				{
					MessageDialog.openInformation(getShell(), "提示", "请先绑定流程");
					return;
				}
				int selectedIndex = sessionContext.getSelectionIndex();
				if(-1 == selectedIndex)
				{
					MessageDialog.openInformation(getShell(), "提示", "请选择映射参数对象");
					return;
				}
				TableItem selectedItem = sessionContext.getItem(selectedIndex);				
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//				StringBuilder sb = new StringBuilder();
//				sb.append(BipConstantUtil.CompositePath).append(compositeService.getBaseinfo().getServiceId()).append(".map");
				IFile file = compositeService.getProject().getFile((String)selectedItem.getData());
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file);
				} catch (PartInitException e1) {
					e1.printStackTrace();
				}
			}			
		});

		scrldfrmNewScrolledform.reflow(true);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public boolean setFocus() {
		// TODO Auto-generated method stub
		return true;
	}
}
