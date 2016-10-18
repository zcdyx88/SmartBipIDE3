package com.dc.bip.ide.wizards.expression;


import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.dc.bip.ide.common.model.ExpressionConstants;
import com.dc.bip.ide.common.model.Params;
import com.dc.bip.ide.objects.ExpressionService;
import com.dc.bip.ide.objects.PramsInfo;
import com.dc.bip.ide.wizards.basesvc.BaseSvcWizardP;
import com.dc.bip.ide.wizards.protocol.ParamsAddWizard;

public class ExpressionWizardPage extends WizardPage {
	private Text text;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table table;
	//private ExpressionService expressionService = new ExpressionService();
	private Params  params = new Params();
	private Params  paramsChildren = new Params();
	private Text text_descri;
	private Text text_imps;
	
	public Params getParams() {
		return params;
	}

	public void setParams(Params params) {
		this.params = params;
	}

	/**
	 * Create the wizard.
	 */
	public ExpressionWizardPage() {
		super("wizardPage");
		setTitle("新增表达式");
		setDescription("表达式信息录入");
		params = new Params();
		paramsChildren = new Params();
		params.setChild("expressionInput", paramsChildren);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Group group = new Group(container, SWT.NONE);
		group.setText("表达式基本信息");
		group.setBounds(42, 32, 500, 280);
		
		Label lblid = new Label(group, SWT.NONE);
		lblid.setBounds(41, 43, 61, 17);
		lblid.setText("表达式ID：");
		
		text = new Text(group, SWT.BORDER);
		text.setBounds(145, 37, 196, 23);
		 text.addModifyListener(new ModifyListener() {  
	            @Override  
	            public void modifyText(ModifyEvent e) {  	            
	            	params.getParams().put(ExpressionConstants.ID, ((Text) e.getSource()).getText());
//	            	implsText.setText((new StringBuilder("com.dc.branch.impls.base.")).append(bs.getName()).toString());
	            	text_imps.setText((new StringBuilder("com.dcits.smartbip.runtime.impl.")).append(((Text) e.getSource()).getText()).toString());
	            	params.getParams().put(ExpressionConstants.IMPLS, text_imps.getText()); 
	            	ExpressionWizardPage.this.getContainer().updateButtons();  
	            }  
	        }); 
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(41, 72, 80, 17);
		label.setText("表达式名称：");
		
		text = new Text(group, SWT.BORDER);
		text.setBounds(145, 66, 196, 23);
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				params.set(ExpressionConstants.NAME, ((Text) e.getSource()).getText());
				ExpressionWizardPage.this.getContainer().updateButtons();
			}
		});
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setBounds(41, 170, 61, 17);
		formToolkit.adapt(label_1, true, true);
		label_1.setText("参数列表：");
		
		table = formToolkit.createTable(group, SWT.NONE);
		table.setBounds(41, 193, 255, 77);
		formToolkit.paintBordersFor(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn reversalCheckField = new TableColumn(table, SWT.NONE);
		reversalCheckField.setWidth(125);
		reversalCheckField.setText("参数名称");

		TableColumn reversalCheckValue = new TableColumn(table, SWT.NONE);
		reversalCheckValue.setWidth(125);
		reversalCheckValue.setText("参数类型");
		
		Button button_add = new Button(group, SWT.NONE);
		button_add.setBounds(318, 194, 80, 27);
		formToolkit.adapt(button_add, true, true);
		button_add.setText("添加");
		button_add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		button_add.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseUp(MouseEvent e) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				ExpressionAddParamsWizard bsw = new ExpressionAddParamsWizard(table, null);
				WizardDialog dialog = new WizardDialog(shell, bsw);
				dialog.setTitle("增加表达式参数");
				dialog.open();
				//firePropertyChange(257);
			}
		});
		
		Button button_delete = new Button(group, SWT.NONE);
		button_delete.setBounds(318, 243, 80, 27);
		formToolkit.adapt(button_delete, true, true);
		button_delete.setText("删除");
		
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setBounds(41, 102, 61, 17);
		formToolkit.adapt(lblNewLabel, true, true);
		lblNewLabel.setText("实现类：");
		
		text_imps = new Text(group, SWT.BORDER);
		text_imps.setBounds(145, 95, 345, 23);
		text_imps.setEditable(false);
		formToolkit.adapt(text_imps, true, true);
		
		
		Label label_2 = new Label(group, SWT.NONE);
		label_2.setBounds(41, 137, 61, 17);
		formToolkit.adapt(label_2, true, true);
		label_2.setText("描述：");
		
		text_descri = new Text(group, SWT.BORDER);
		text_descri.setBounds(145, 131, 345, 23);
		formToolkit.adapt(text_descri, true, true);
		text_descri.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				params.set(ExpressionConstants.DESCRIBE, ((Text) e.getSource()).getText());
				ExpressionWizardPage.this.getContainer().updateButtons();
			}
		});
		
		button_delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(e.widget.getData());
			}
		});
		button_delete.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseUp(MouseEvent e) {
				int index = table.getSelectionIndex();
				if(index>0){
					String inputName = table.getItem(index).getText(0);
					params.getChild("expressionInput").remove(inputName);
					table.remove(index);
//					params.getChildParams().r.getParamsModels().remove(expressionService.getParamsModels().get(index));
					//firePropertyChange(PROP_DIRTY);
				}
			}
		});
	}
}