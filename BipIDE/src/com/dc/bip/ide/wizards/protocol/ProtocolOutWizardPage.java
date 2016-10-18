package com.dc.bip.ide.wizards.protocol;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;

public class ProtocolOutWizardPage extends AWizardPage {
	
	public String protocol ;
	
	/**
	 * Create the wizard.
	 */
	public ProtocolOutWizardPage() {
		super("wizardPage");
		setTitle("接出协议");
		setDescription("根据接出协议类型填写相关信息");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Group group = new Group(container, SWT.NONE);
		group.setText("\u63A5\u51FA\u534F\u8BAE");
		group.setBounds(154, 103, 337, 105);
		
		Combo combo = new Combo(group, SWT.NONE);
		combo.setItems(new String[] {"TCP", "HTTP", "WebService"});
		combo.setBounds(144, 46, 105, 25);
		combo.select(0);
		ProtocolOutWizardPage.this.getWizard().getDialogSettings().put("protocol", combo.getText());
		combo.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent arg0) {
				Combo combo = (Combo)arg0.widget;
				protocol = combo.getText();
				ProtocolOutWizardPage.this.getWizard().getDialogSettings().put("protocol", protocol);
			}
				
			});
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(50, 54, 88, 17);
		label.setText("\u63A5\u51FA\u534F\u8BAE\u7C7B\u578B\uFF1A");
	}

}
