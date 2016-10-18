package com.dc.bip.ide.wizards.expression;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ExpressionAddParamsWizardPage extends WizardPage {
	private Text paramKey;
	private Text paramValue;

	/**
	 * Create the wizard.
	 */
	public ExpressionAddParamsWizardPage() {
		super("wizardPage");
		setTitle("新增表达式参数名称及类型");
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);

		ScrolledComposite scrolledComposite = new ScrolledComposite(container,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(0, 0, 564, 310);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite composite = new Composite(scrolledComposite, SWT.NONE);

		Label lblWsdl = new Label(composite, SWT.NONE);
		lblWsdl.setAlignment(SWT.RIGHT);
		lblWsdl.setBounds(49, 55, 76, 23);
		lblWsdl.setText("参数名称：");

		paramKey = new Text(composite, SWT.BORDER);
		paramKey.setBounds(150, 52, 283, 23);

		Label lblJmxport = new Label(composite, SWT.NONE);
		lblJmxport.setAlignment(SWT.RIGHT);
		lblJmxport.setBounds(49, 93, 76, 23);
		lblJmxport.setText("参数类型：");

		paramValue = new Text(composite, SWT.BORDER);
		paramValue.setBounds(151, 90, 283, 23);

		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}


	public String getParamKey() {
		return paramKey.getText();
	}

	public String getParamValue() {
		return paramValue.getText();
	}

}
