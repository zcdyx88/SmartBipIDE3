package com.dc.bip.ide.wizards.busi;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.dc.bip.ide.common.globalconfig.GlobalConstants;
import com.dc.bip.ide.objects.ProtocolInService;
import com.dc.bip.ide.repository.impl.ProtocolServiceRepository;
import com.dc.bip.ide.service.ProtocolService;

public class BusiSvcAddWizardPage extends WizardPage {
	private String PATH = "file://";
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	private Text serviceIDText;
	private Text serviceNameText;
	private Text serviceDescText;
	private Combo protocolCombo;
	private Combo packUnPackCombo;
	private String projectName;

	/**
	 * Create the wizard.
	 */
	public BusiSvcAddWizardPage() {
		super("wizardPage");
		setTitle("新增业务服务");
		setDescription("新增一个业务服务");
	}

	public BusiSvcAddWizardPage(String projectName) {
		super("wizardPage");
		this.projectName = projectName;
		setTitle("新增业务服务");
		setDescription("新增一个业务服务");
		this.projectName = projectName;
		// TODO Auto-generated constructor stub
	}



	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		Label serviceIDLabel = new Label(container, SWT.NONE);
		serviceIDLabel.setAlignment(SWT.RIGHT);
		serviceIDLabel.setBounds(50, 30, 70, 20);
		serviceIDLabel.setText("服务ID:");
		// 文本输入框
		serviceIDText = new Text(container, SWT.BORDER);
		serviceIDText.setBounds(125, 25, 283, 25);
		Label serviceNameLabel = new Label(container, SWT.NONE);
		serviceNameLabel.setAlignment(SWT.RIGHT);
		serviceNameLabel.setBounds(50, 60, 70, 20);
		// 服务名称
		serviceNameLabel.setText("\u670D\u52A1\u540D\u79F0:");
		// 文本输入框
		serviceNameText = new Text(container, SWT.BORDER);
		serviceNameText.setBounds(125, 55, 283, 25);

		Label protocolLabel = new Label(container, SWT.NONE);
		protocolLabel.setText("绑定协议:");
		protocolLabel.setAlignment(SWT.RIGHT);
		protocolLabel.setBounds(50, 90, 70, 20);

		Label packUnPackLabel = new Label(container, SWT.NONE);
		packUnPackLabel.setText("数据适配:");
		packUnPackLabel.setAlignment(SWT.RIGHT);
		packUnPackLabel.setBounds(50, 120, 70, 20);
		

		protocolCombo = new Combo(container, SWT.NONE);
		protocolCombo.setBounds(125, 88, 283, 25);
		ProtocolService protocolService = ProtocolServiceRepository.getInstance().get(projectName);
		for(ProtocolInService protocolInService :protocolService.getAllOutProtocols()){
			protocolCombo.add(protocolInService.getProtocolName());
		}

		packUnPackCombo = new Combo(container, SWT.NONE);
		packUnPackCombo.setBounds(125, 116, 283, 22);
		packUnPackCombo.setItems(GlobalConstants.PACK_UNPACK_MODE);

		Label serviceDesc = new Label(container, SWT.NONE);
		serviceDesc.setText("服务描述:");
		serviceDesc.setAlignment(SWT.RIGHT);
		serviceDesc.setBounds(50, 150, 70, 20);

		serviceDescText = formToolkit.createText(container, "New Text", SWT.NONE);
		serviceDescText.setText("");
		serviceDescText.setBounds(125, 147, 283, 104);

		serviceIDText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// 因为模型改变了，所以要及时更改界面
				BusiSvcAddWizardPage.this.getContainer().updateButtons();
			}
		});
		serviceNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// 因为模型改变了，所以要及时更改界面
				BusiSvcAddWizardPage.this.getContainer().updateButtons();
			}
		});

	}

	public boolean isPageComplete() {
		return getServiceID().length() > 0 && getServiceName().length() > 0;
	}
	public String getServiceID() {
		return serviceIDText.getText() == null ? "" : serviceIDText.getText();
	}
	public String getServiceName() {
		return serviceNameText.getText() == null ? "" : serviceNameText.getText();
	}
	public String getServiceDesc() {
		return serviceDescText.getText() == null ? "" : serviceDescText.getText();
	}
	public String getProtocol() {
		return protocolCombo.getText() == null ? "" : protocolCombo.getText();
	}

	public String getPackUnPack() {
		return packUnPackCombo.getText() == null ? "" : packUnPackCombo.getText();
	}

}
