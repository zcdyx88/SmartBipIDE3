package com.dc.bip.ide.wizards.context;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.dc.bip.ide.util.BipConstantUtil;

public class EditContextParamWizardPage extends WizardPage {
	private Text text_3;
	private Text text_4;
	private Text text_1;
	private TableItem tableItem;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Combo combo;
	/**
	 * Create the wizard.
	 */
	public EditContextParamWizardPage(TableItem tableItem) {
		super("wizardPage");
		setTitle("编辑上下文参数");
		this.tableItem = tableItem;
//		setDescription("通过WSDL URL地址生成业务服务");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(0, 0, 564, 310);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		
		Label lblWsdl = new Label(composite, SWT.NONE);
		lblWsdl.setAlignment(SWT.RIGHT);
		lblWsdl.setBounds(64, 55, 61, 23);
		lblWsdl.setText("\u7C7B\u578B\uFF1A");
		
		Label lblJmxport = new Label(composite, SWT.NONE);
		lblJmxport.setAlignment(SWT.RIGHT);
		lblJmxport.setBounds(78, 93, 47, 23);
		lblJmxport.setText("\u503C\uFF1A");
		
		text_3 = new Text(composite, SWT.BORDER);
		text_3.setBounds(151, 90, 283, 23);
		text_3.setText(tableItem.getText(2));
		
		Label lblRegport = new Label(composite, SWT.NONE);
		lblRegport.setAlignment(SWT.RIGHT);
		lblRegport.setBounds(64, 132, 61, 23);
		lblRegport.setText("\u63CF\u8FF0\uFF1A");
		
		text_4 = new Text(composite, SWT.BORDER);
		text_4.setBounds(152, 129, 283, 23);
		text_4.setText(tableItem.getText(3));
		
		Label lblId = new Label(composite, SWT.NONE);
		lblId.setAlignment(SWT.RIGHT);
		lblId.setText("ID\uFF1A");
		lblId.setBounds(76, 16, 47, 23);
		
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setEditable(false);
		text_1.setBounds(151, 13, 283, 23);
		text_1.setText(tableItem.getText(0));	
		
		combo = new Combo(composite, SWT.NONE);
		combo.setBounds(151, 55, 283, 25);
		combo.setItems(BipConstantUtil.ParamTypes);
		combo.setText(tableItem.getText(1));
		formToolkit.adapt(combo);
		formToolkit.paintBordersFor(combo);
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	public String getPararmID() {
		return text_1.getText();
	}
	
	public String getPararmType() {
		return combo.getText();
	}

	public String getPararmValue() {
		return text_3.getText();
	}
	
	public String getPararmDesc() {
		return text_4.getText();
	}
}
