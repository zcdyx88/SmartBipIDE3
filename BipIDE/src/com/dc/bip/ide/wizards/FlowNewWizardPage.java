package com.dc.bip.ide.wizards;

import java.util.Date;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class FlowNewWizardPage extends WizardPage {
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;

	/**
	 * Create the wizard.
	 */
	public FlowNewWizardPage() {
		super("wizardPage");
		setTitle("新建流程");
		setDescription("新建流程");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Label lblWsdl = new Label(container, SWT.NONE);
		lblWsdl.setBounds(88, 76, 68, 15);
		lblWsdl.setText("流程名称:");
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(164, 72, 283, 23);
		text.setText("flow" +  new Date().getTime());
		
		Label label = new Label(container, SWT.NONE);
		label.setBounds(88, 118, 68, 15);
		label.setText("开始节点：");
		
		text_1 = new Text(container, SWT.BORDER);
		text_1.setBounds(163, 115, 284, 23);
		text_1.setText("begin");
		
		Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("结束节点：");
		label_1.setBounds(88, 155, 68, 23);
		
		text_2 = new Text(container, SWT.BORDER);
		text_2.setBounds(164, 155, 284, 23);
		
	}
	
	public String getFlowName(){
		return text.getText();
	}
	public String getBenginNodeName(){
		return text_1.getText();
	}
	public String getEndNodeName(){
		return text_2.getText();
	}
	
}
