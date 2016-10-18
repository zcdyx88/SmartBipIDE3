package com.dc.bip.ide.wizards.busi;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ReversalConditionAddWizardPage extends WizardPage {
	private Text reversalCheckValue;
	private Text reversalConditionDesc;
	private Text reversalCheckField;

	/**
	 * Create the wizard.
	 */
	public ReversalConditionAddWizardPage() {
		super("wizardPage");
		setTitle("新增冲正条件");
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
		lblWsdl.setText("冲正值：");

		reversalCheckValue = new Text(composite, SWT.BORDER);
		reversalCheckValue.setBounds(150, 52, 283, 23);

		Label lblJmxport = new Label(composite, SWT.NONE);
		lblJmxport.setAlignment(SWT.RIGHT);
		lblJmxport.setBounds(78, 93, 47, 23);
		lblJmxport.setText("描述：");

		reversalConditionDesc = new Text(composite, SWT.BORDER);
		reversalConditionDesc.setBounds(151, 90, 283, 23);

		Label lblId = new Label(composite, SWT.NONE);
		lblId.setAlignment(SWT.RIGHT);
		lblId.setText(" 冲正检查域：");
		lblId.setBounds(49, 16, 74, 23);

		reversalCheckField = new Text(composite, SWT.BORDER);
		reversalCheckField.setBounds(151, 13, 283, 23);
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	public String getReversalCheckField() {
		return reversalCheckField.getText();
	}

	public String getReversalCheckValue() {
		return reversalCheckValue.getText();
	}

	public String getReversalConditionDesc() {
		return reversalConditionDesc.getText();
	}

	public String getDesc() {
		return reversalConditionDesc.getText();
	}
}
