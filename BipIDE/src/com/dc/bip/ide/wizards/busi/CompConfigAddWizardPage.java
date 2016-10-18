package com.dc.bip.ide.wizards.busi;

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

import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.objects.CompositeService;

public class CompConfigAddWizardPage extends WizardPage {
	private String PATH = "file://";
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	private Text serviceIDText;
	private Text serviceNameText;
	private Combo configTypeCombo;
	private Text configNameText;
	
	private CompositeService svcInfo;


	/**
	 * Create the wizard.
	 */
	public CompConfigAddWizardPage() {
		super("wizardPage");
		setTitle("新增业务服务配置");
		setDescription("新增业务服务配置");
	}
	
	public CompConfigAddWizardPage(CompositeService svcInfo){
		super("wizardPage");
		this.svcInfo = svcInfo;
		setTitle("新增业务服务配置");
		setDescription("新增业务服务配置");
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
		serviceIDText.setBounds(125, 25, 329, 25);
		serviceIDText.setText(this.svcInfo.getBaseinfo().getServiceId());
		serviceIDText.setEnabled(false);

		Label serviceNameLabel = new Label(container, SWT.NONE);
		serviceNameLabel.setAlignment(SWT.RIGHT);
		serviceNameLabel.setBounds(50, 60, 70, 20);
		// 服务名称
		serviceNameLabel.setText("\u670D\u52A1\u540D\u79F0:");
		// 文本输入框
		serviceNameText = new Text(container, SWT.BORDER);
		serviceNameText.setBounds(125, 55, 329, 25);
		serviceNameText.setText(this.svcInfo.getBaseinfo().getServiceName());
		serviceNameText.setEnabled(false);

		Label configTypeLabel = new Label(container, SWT.NONE);
		configTypeLabel.setText("配置类型:");
		configTypeLabel.setAlignment(SWT.RIGHT);
		configTypeLabel.setBounds(50, 90, 70, 20);
		Label configNameLabel = new Label(container, SWT.NONE);
		configNameLabel.setText("文件名称:");
		configNameLabel.setAlignment(SWT.RIGHT);
		configNameLabel.setBounds(50, 120, 70, 20);
		configTypeCombo = new Combo(container, SWT.NONE);
		configTypeCombo.setBounds(125, 88, 329, 22);
		configTypeCombo.setItems(new String[]{"服务定义","WSDL"});
		configTypeCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if("服务定义".equals(configTypeCombo.getText())){
					configNameText.setText("service_" + serviceIDText.getText() + ".xml");
				}else if("WSDL".equalsIgnoreCase(configTypeCombo.getText())){
					configNameText.setText( serviceIDText.getText() + ".wsdl");
				}
			}
		});
		configNameText = new Text(container, SWT.BORDER);
		configNameText.setBounds(125, 115, 329, 25);
		formToolkit.adapt(configNameText, true, true);
		configTypeCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// 因为模型改变了，所以要及时更改界面
				CompConfigAddWizardPage.this.getContainer().updateButtons();
			}
		});
		configNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// 因为模型改变了，所以要及时更改界面
				CompConfigAddWizardPage.this.getContainer().updateButtons();
			}
		});
	}

	public boolean isPageComplete() {
		return getServiceID().length() > 0 && getServiceName().length() > 0 && getConfigType().length() > 0
				&& getConfigName().length() > 0;
	}

	public String getServiceID() {
		return serviceIDText.getText() == null ? "" : serviceIDText.getText();
	}

	public String getServiceName() {
		return serviceNameText.getText() == null ? "" : serviceNameText.getText();
	}

	public String getConfigType() {
		return configTypeCombo.getText() == null ? "" : configTypeCombo.getText();
	}

	public String getConfigName() {
		return configNameText.getText() == null ? "" : configNameText.getText();
	}

}
