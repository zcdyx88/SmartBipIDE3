package com.dc.bip.ide.wizards.protocol;


import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class ProtocolInWizardPage extends WizardPage {
	public String protocol = "";

	/**
	 * Create the wizard.
	 */
	public ProtocolInWizardPage() {
		super("wizardPage");
		setTitle("\u9009\u62E9\u534F\u8BAE\u7C7B\u578B");
		setDescription("\u6839\u636E\u9009\u62E9\u7684\u534F\u8BAE\u7C7B\u578B\u586B\u5199\u534F\u8BAE\u4FE1\u606F");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Group group = new Group(container, SWT.NONE);
		group.setText("\u534F\u8BAE");
		group.setBounds(71, 56, 342, 159);
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(42, 73, 61, 17);
		label.setText("\u534F\u8BAE\u7C7B\u578B\uFF1A");
		
		Combo combo = new Combo(group, SWT.NONE);
		combo.setItems(new String[] {"TCP", "HTTP", "WebService"});
		combo.setBounds(121, 65, 95, 25);
		combo.select(0);
		ProtocolInWizardPage.this.getWizard().getDialogSettings().put("protocol",  
				combo.getText());	
		combo.addModifyListener(new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent arg0) {
			Combo combo = (Combo)arg0.widget;
			protocol = combo.getText();
			ProtocolInWizardPage.this.getWizard().getDialogSettings().put("protocol",  
					protocol); 
			//ProtocolInWizardPage.this.getWizard();
			//ProtocolInWizardPage.this.getWizard().
		}
			
		});
	}

}
