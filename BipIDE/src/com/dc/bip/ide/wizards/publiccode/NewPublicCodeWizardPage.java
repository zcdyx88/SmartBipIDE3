package com.dc.bip.ide.wizards.publiccode;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.custom.ScrolledComposite;

public class NewPublicCodeWizardPage extends WizardPage {
	private Text text;
	private Text text_3;
	private Text text_4;
	private Text text_1;

	/**
	 * Create the wizard.
	 */
	public NewPublicCodeWizardPage() {
		super("wizardPage");
		setTitle("\u65B0\u589E\u4E0A\u4E0B\u6587\u53C2\u6570");
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
		lblWsdl.setBounds(64, 55, 61, 23);
		lblWsdl.setText("\u4EE3\u7801\u540D\u79F0:");
		
		text = new Text(composite, SWT.BORDER);
		text.setBounds(150, 52, 283, 23);
		
		Label lblJmxport = new Label(composite, SWT.NONE);
		lblJmxport.setBounds(78, 93, 47, 23);
		lblJmxport.setText("\u4EE3\u7801\u503C:");
		
		text_3 = new Text(composite, SWT.BORDER);
		text_3.setBounds(151, 90, 283, 23);
		
		Label lblRegport = new Label(composite, SWT.NONE);
		lblRegport.setBounds(64, 132, 61, 23);
		lblRegport.setText("\u4EE3\u7801\u63CF\u8FF0:");
		
		text_4 = new Text(composite, SWT.BORDER);
		text_4.setBounds(152, 129, 283, 23);
		
		Label lblId = new Label(composite, SWT.NONE);
		lblId.setText("\u4EE3\u7801ID:");
		lblId.setBounds(76, 16, 47, 23);
		
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setBounds(151, 13, 283, 23);
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	public String getID() {
		return text_1.getText();
	}
	
	public String getName() {
		return text.getText();
	}

	public String getValue() {
		return text_3.getText();
	}
	
	public String getDesc() {
		return text_4.getText();
	}
	
}
