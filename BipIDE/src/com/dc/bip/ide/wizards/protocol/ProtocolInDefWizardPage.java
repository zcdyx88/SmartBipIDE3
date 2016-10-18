package com.dc.bip.ide.wizards.protocol;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.dc.bip.ide.objects.ProtocolInService;

public class ProtocolInDefWizardPage extends AWizardPage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Text text;
	private ProtocolInService protocolInService = new ProtocolInService();
	

	public ProtocolInService getProtocolInService() {
		return protocolInService;
	}

	public void setProtocolInService(ProtocolInService protocolInService) {
		this.protocolInService = protocolInService;
	}


	/**
	 * Create the wizard.
	 */
	public ProtocolInDefWizardPage() {
		super("wizardPage");
		setTitle("TCP接入协议");
		setDescription("填写TCP接入协议相关信息");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		String  protocolType= ProtocolInDefWizardPage.this.getWizard().getDialogSettings().get("protocol");  
		if(protocolType!=null && protocolType.equals("TCP")){
			this.setTitle("TCP接入协议");
			this.setDescription("填写TCP接入协议相关信息");
			
			Group group = new Group(container, SWT.NONE);
			group.setText("\u57FA\u672C\u4FE1\u606F");
			group.setBounds(32, 10, 461, 229);
			
			Label label = new Label(group, SWT.NONE);
			label.setBounds(39, 26, 61, 17);
			label.setText("\u534F\u8BAE\u7C7B\u578B\uFF1A");
			
			text = new Text(group, SWT.BORDER);
			text.setEditable(false);
			text.setBounds(162, 20, 125, 23);
			String type = "";
			if(ProtocolInDefWizardPage.this.getWizard().getDialogSettings().get("protocol") != null){
				type=ProtocolInDefWizardPage.this.getWizard().getDialogSettings().get("protocol");
			}
			protocolInService.setProtocolType(type);
			text.setText(type);
			
			Label label_1 = new Label(group, SWT.NONE);
			label_1.setBounds(39, 52, 61, 17);
			label_1.setText("\u540D\u79F0\uFF1A");
			
			text = new Text(group, SWT.BORDER);
			text.setBounds(162, 49, 210, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setProtocolName(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
			Label lblIp = new Label(group, SWT.NONE);
			lblIp.setBounds(39, 81, 61, 17);
			lblIp.setText("IP\uFF1A");
			
			text = new Text(group, SWT.BORDER);
			text.setBounds(162, 78, 210, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setIp(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			Label label_2 = new Label(group, SWT.NONE);
			label_2.setBounds(39, 110, 61, 17);
			label_2.setText("\u7AEF\u53E3\uFF1A");
			
			text = new Text(group, SWT.BORDER);
			text.setBounds(162, 107, 97, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setPort(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
			Label label_8 = new Label(group, SWT.NONE);
			label_8.setBounds(39, 139, 61, 17);
			label_8.setText("\u8D85\u65F6\u65F6\u95F4\uFF1A");
			
			text = new Text(group, SWT.BORDER);
			text.setBounds(162, 136, 83, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setOverTime(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
			Label label_3 = new Label(group, SWT.NONE);
			label_3.setBounds(39, 168, 61, 17);
			label_3.setText("读策略：");
			
			text = new Text(group, SWT.BORDER);
			text.setBounds(162, 165, 289, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setReadMethod(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
			text = new Text(group, SWT.BORDER);
			text.setBounds(162, 193, 289, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setWriteMethod(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
			Label label_9 = new Label(group, SWT.NONE);
			label_9.setBounds(39, 199, 61, 17);
			label_9.setText("写策略");
			
			Group group_1 = new Group(container, SWT.NONE);
			group_1.setText("\u7EBF\u7A0B\u6C60\u53C2\u6570");
			group_1.setBounds(32, 245, 461, 94);
			
			Label label_4 = new Label(group_1, SWT.NONE);
			label_4.setBounds(42, 32, 77, 17);
			label_4.setText("\u6700\u5927\u7EBF\u7A0B\u6570\uFF1A");
			
			text = new Text(group_1, SWT.BORDER);
			text.setBounds(141, 29, 73, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setMaxThreads(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
			Label label_5 = new Label(group_1, SWT.NONE);
			label_5.setBounds(42, 61, 77, 17);
			label_5.setText("\u6700\u5C0F\u7EBF\u7A0B\u6570\uFF1A");
			
			text = new Text(group_1, SWT.BORDER);
			text.setBounds(141, 58, 73, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setMinThreads(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
			Label label_6 = new Label(group_1, SWT.NONE);
			label_6.setBounds(258, 32, 82, 17);
			label_6.setText("\u5904\u7406\u961F\u5217\u4E2A\u6570\uFF1A");
			
			text = new Text(group_1, SWT.BORDER);
			text.setBounds(364, 26, 73, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setAcceptSize(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
			Label label_7 = new Label(group_1, SWT.NONE);
			label_7.setBounds(258, 61, 101, 17);
			label_7.setText("\u5904\u7406\u961F\u5217\u957F\u5EA6\uFF1A");
			
			text = new Text(group_1, SWT.BORDER);
			text.setBounds(364, 61, 73, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setAcceptQueueSize(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
		}else{
			this.setTitle("WebService/HTTP接入协议");
			this.setDescription("填写WebService/HTTP接入协议相关信息");
			
			Group group = new Group(container, SWT.NONE);
			group.setText("\u57FA\u672C\u4FE1\u606F");
			group.setBounds(32, 20, 461, 147);
			
			Label label = new Label(group, SWT.NONE);
			label.setBounds(39, 26, 61, 17);
			label.setText("\u534F\u8BAE\u7C7B\u578B\uFF1A");
			
			text = new Text(group, SWT.BORDER);
			text.setBounds(162, 20, 125, 23);
			String protocol="";
			if(ProtocolInDefWizardPage.this.getWizard().getDialogSettings().get("protocol")!=null){
				protocol= ProtocolInDefWizardPage.this.getWizard().getDialogSettings().get("protocol");
				protocolInService.setProtocolType(protocol);
			}
			text.setText(protocol);
			
			Label label_1 = new Label(group, SWT.NONE);
			label_1.setBounds(39, 52, 61, 17);
			label_1.setText("\u540D\u79F0\uFF1A");
			
			text = new Text(group, SWT.BORDER);
			text.setBounds(162, 49, 210, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setProtocolName(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
			
			Label label_2 = new Label(group, SWT.NONE);
			label_2.setBounds(39, 81, 61, 17);
			label_2.setText("\u5730\u5740\uFF1A");
			
			text = new Text(group, SWT.BORDER);
			text.setBounds(162, 78, 271, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setUrl(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
			Label label_3 = new Label(group, SWT.NONE);
			label_3.setBounds(39, 120, 61, 17);
			label_3.setText("\u901A\u8BAF\u534F\u8BAE\uFF1A");
			
			Combo combo = new Combo(group, SWT.NONE);
			combo.setItems(new String[] {"HTTP 1.0", "HTTP 2.0"});
			combo.setBounds(162, 112, 125, 25);
			combo.select(0);
			protocolInService.setContactProtocolType(combo.getText());
			combo.addModifyListener(new ModifyListener(){
			
				@Override
				public void modifyText(ModifyEvent arg0) {
					Combo combo = (Combo)arg0.widget;
					String protocol = combo.getText();
					protocolInService.setContactProtocolType(protocol);
					
				}
					
				});
			
			Group group_1 = new Group(container, SWT.NONE);
			group_1.setText("\u7EBF\u7A0B\u6C60\u53C2\u6570");
			group_1.setBounds(32, 173, 461, 94);
			
			Label label_4 = new Label(group_1, SWT.NONE);
			label_4.setBounds(42, 32, 77, 17);
			label_4.setText("\u6700\u5927\u7EBF\u7A0B\u6570\uFF1A");
			
			text = new Text(group_1, SWT.BORDER);
			text.setBounds(141, 29, 73, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setMaxThreads(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
			Label label_5 = new Label(group_1, SWT.NONE);
			label_5.setBounds(42, 61, 77, 17);
			label_5.setText("\u6700\u5C0F\u7EBF\u7A0B\u6570\uFF1A");
			
			text = new Text(group_1, SWT.BORDER);
			text.setBounds(141, 58, 73, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setMinThreads(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
			Label label_6 = new Label(group_1, SWT.NONE);
			label_6.setBounds(258, 32, 82, 17);
			label_6.setText("\u5904\u7406\u961F\u5217\u4E2A\u6570\uFF1A");
			
			text = new Text(group_1, SWT.BORDER);
			text.setBounds(364, 26, 73, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setAcceptSize(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
			Label label_7 = new Label(group_1, SWT.NONE);
			label_7.setBounds(258, 61, 101, 17);
			label_7.setText("\u5904\u7406\u961F\u5217\u957F\u5EA6\uFF1A");
			
			text = new Text(group_1, SWT.BORDER);
			text.setBounds(364, 61, 73, 23);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					protocolInService.setAcceptQueueSize(((Text) e.getSource()).getText());
					ProtocolInDefWizardPage.this.getContainer().updateButtons();
				}
			});
			
//			Label lblNewLabel = new Label(container, SWT.NONE);
//			lblNewLabel.setBounds(32, 293, 61, 17);
//			lblNewLabel.setText("\u81EA\u5B9A\u4E49\u62A5\u6587\u5934\uFF1A");
//			
//			text = new Text(container, SWT.BORDER);
//			text.setBounds(106, 292, 383, 23);

		}

	}
	
//	@Override
//	public boolean isPageComplete() {
//		boolean isCanPress = false;
//		String  protocolType= ProtocolInDefWizardPage.this.getWizard().getDialogSettings().get("protocol");  
//		if(protocolType!=null && protocolType.equals("TCP")){
//			if(protocolInService.getIp().length()>0 && protocolInService.getPort().length()>0 && protocolInService.getOverTime().length()>0 
//					&& protocolInService.getReadMethod().length()>0 && protocolInService.getReadMethod().length()>0 
//					&& protocolInService.getMaxThreads().length()>0 && protocolInService.getMinThreads().length()>0 
//					&& protocolInService.getAcceptSize().length()>0 && protocolInService.getAcceptQueueSize().length()>0 ){
//				isCanPress = true;
//			}
//		}else{
//			if(protocolInService.getUrl().length()>0 && protocolInService.getContactProtocolType().length()>0 
//					&& protocolInService.getReadMethod().length()>0 && protocolInService.getReadMethod().length()>0 
//					&& protocolInService.getMaxThreads().length()>0 && protocolInService.getMinThreads().length()>0 
//					&& protocolInService.getAcceptSize().length()>0 && protocolInService.getAcceptQueueSize().length()>0 ){
//				isCanPress = true;
//			}
//		}
//		return isCanPress;
//		
//	}
}


