package com.dc.bip.ide.wizards.bipserver;

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

public class NewBipServerWizardPage extends WizardPage {
	private Text text;
	private Text text_3;
	private Text text_4;
	private Text text_1;

	/**
	 * Create the wizard.
	 */
	public NewBipServerWizardPage() {
		super("wizardPage");
		setTitle("新增BIP服务端配置");
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
		Label lblId = new Label(composite, SWT.NONE);
		lblId.setText("ID:");
		lblId.setBounds(85, 16, 47, 23);
		
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setBounds(151, 13, 283, 23);
		
		Label lblWsdl = new Label(composite, SWT.NONE);
		lblWsdl.setBounds(85, 55, 37, 23);
		lblWsdl.setText("IP:");
		
		text = new Text(composite, SWT.BORDER);
		text.setBounds(150, 52, 283, 23);
		
		Label lblJmxport = new Label(composite, SWT.NONE);
		lblJmxport.setBounds(50, 93, 72, 23);
		lblJmxport.setText("JmxPort:");
		
		text_3 = new Text(composite, SWT.BORDER);
		text_3.setBounds(151, 90, 283, 23);
		
		Label lblRegport = new Label(composite, SWT.NONE);
		lblRegport.setBounds(49, 132, 72, 23);
		lblRegport.setText("RegPort:");
		
		text_4 = new Text(composite, SWT.BORDER);
		text_4.setBounds(152, 129, 283, 23);
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	public String getID() {
		return text_1.getText();
	}
	
	public String getIP() {
		return text.getText();
	}

	public String getJmxPort() {
		return text_3.getText();
	}
	
	public String getRegPort() {
		return text_4.getText();
	}
	
}
