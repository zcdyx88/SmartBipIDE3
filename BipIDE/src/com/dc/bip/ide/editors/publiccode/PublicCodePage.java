package com.dc.bip.ide.editors.publiccode;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import com.dc.bip.ide.context.PublicCode;
import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.wizards.publiccode.EditPublicCodeWizard;
import com.dc.bip.ide.wizards.publiccode.NewPublicCodeWizard;

public class PublicCodePage extends Composite {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private String projectName;
	private Display display;
	private Table table;
	private String codeFileName;
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public PublicCodePage(final Composite parent, int style, List<PublicCode> codeList,final String projectName,final String codeFileName) {
		
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		this.projectName = projectName;
		this.codeFileName = codeFileName;
		

		ScrolledForm scrldfrmNewScrolledform = formToolkit.createScrolledForm(this);
		scrldfrmNewScrolledform.setAlwaysShowScrollBars(true);
		scrldfrmNewScrolledform.setExpandVertical(true);
		scrldfrmNewScrolledform.setExpandHorizontal(true);
		scrldfrmNewScrolledform.setMinHeight(400);
		scrldfrmNewScrolledform.setMinWidth(400);
		formToolkit.paintBordersFor(scrldfrmNewScrolledform);
		scrldfrmNewScrolledform.setText("\u516C\u5171\u4EE3\u7801\u7F16\u8F91");
		
		table = formToolkit.createTable(scrldfrmNewScrolledform.getBody(), SWT.NONE);
		table.setBounds(10, 10, 600, 308);
		formToolkit.paintBordersFor(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(150);
		tblclmnNewColumn.setText("代码ID");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(150);
		tblclmnNewColumn_1.setText("代码名称");
		
		TableColumn tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(150);
		tblclmnNewColumn_2.setText("代码值");

		TableColumn tblclmnNewColumn_3 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_3.setWidth(146);
		tblclmnNewColumn_3.setText("代码描述");
		if(codeList!=null && codeList.size() > 0){
			for(PublicCode code : codeList){
				TableItem tableItem = new TableItem(table, SWT.NONE);
				tableItem.setText(0,code.getId());
				tableItem.setText(1,code.getName());
				tableItem.setText(2,code.getValue());
				tableItem.setText(3,code.getDesc());
			}	
		}
		
		//添加
		Button btnNewButton = formToolkit.createButton(scrldfrmNewScrolledform.getBody(), "\u6DFB\u52A0", SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				NewPublicCodeWizard bsw = new NewPublicCodeWizard(projectName,table);
		        WizardDialog dialog = new WizardDialog(shell, bsw);
		        dialog.setTitle("新增BIP服务端配置");
		        dialog.open();  
		        
			}
		});
		btnNewButton.setBounds(624, 10, 80, 27);
		//编辑
		Button btnNewButton_1 = formToolkit.createButton(scrldfrmNewScrolledform.getBody(), "\u7F16\u8F91", SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectedIndex = table.getSelectionIndex();
				TableItem selectedItem = table.getItem(selectedIndex);
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				EditPublicCodeWizard bsw = new EditPublicCodeWizard(projectName,selectedItem);
		        WizardDialog dialog = new WizardDialog(shell, bsw);
		        dialog.setTitle("编辑BIP服务端配置");
		        dialog.open();  
			}
		});
		btnNewButton_1.setBounds(624, 94, 80, 27);
		//删除
		Button btnNewButton_2 = formToolkit.createButton(scrldfrmNewScrolledform.getBody(), "\u5220\u9664", SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectedIndex = table.getSelectionIndex();
				table.remove(selectedIndex);
			}
		});
		btnNewButton_2.setBounds(624, 184, 80, 27);
		//保存
		Button btnNewButton_3 = formToolkit.createButton(scrldfrmNewScrolledform.getBody(), "\u4FDD\u5B58", SWT.NONE);
		btnNewButton_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] allItems = table.getItems();
				List<PublicCode> codes = new ArrayList<PublicCode>();
				for(TableItem item:allItems){
					String id = item.getText(0);
					String name = item.getText(1);
					String value = item.getText(2);
					String desc = item.getText(3);
					PublicCode code = new PublicCode();
					code.setId(id);
					code.setName(name);
					code.setValue(value);
					code.setDesc(desc);
					codes.add(code);
				}
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				String codeFilePath = (new StringBuilder("/")).append(projectName).append(BipConstantUtil.CompositePath)
						.append(codeFileName).toString();
				IPath codePath = new Path(codeFilePath);
				IFile codefile = root.getFile(codePath);
				HelpUtil.writeObject(codes, codefile);
				String compFilePath = (new StringBuilder("/")).append(projectName).append(BipConstantUtil.CompositePath)
						.append(codeFileName.substring(0, codeFileName.lastIndexOf("."))).append(".composite").toString();
				IPath compPath = new Path(compFilePath);
				IFile compfile = root.getFile(compPath);
				CompositeService compSvc = HelpUtil.readObject(compfile);
				compSvc.setCodes(codes);
				HelpUtil.writeObject(compSvc, compfile);
				JOptionPane.showMessageDialog(null, "保存成功");
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IEditorPart[] editors = page.getEditors();
				for (IEditorPart editor : editors) {
					String title = editor.getTitle();
					if ("公共代码编辑器".equals(title)) {
						page.closeEditor(editor, true);
					}
				}
			}
		});
		btnNewButton_3.setBounds(624, 268, 80, 27);
		


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
