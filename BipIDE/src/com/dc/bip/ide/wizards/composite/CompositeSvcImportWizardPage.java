package com.dc.bip.ide.wizards.composite;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.swt.widgets.Button;

public class CompositeSvcImportWizardPage extends WizardPage {
	private Text text;
	private String PATH = "file://";
	private Text textName;

	/**
	 * Create the wizard.
	 */
	public CompositeSvcImportWizardPage() {
		super("wizardPage");
		setTitle("导入组合服务");
		setDescription("通过WSDL URL地址生成组合服务");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);

		Label lblWsdl = new Label(container, SWT.NONE);
		lblWsdl.setAlignment(SWT.RIGHT);
		lblWsdl.setBounds(38, 110, 72, 23);
		lblWsdl.setText("WSDL URL:");

		text = new Text(container, SWT.BORDER);
		text.setBounds(116, 107, 283, 23);

		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.setBounds(419, 105, 80, 27);
		btnNewButton.setText("\u6D4F\u89C8..");

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(49, 71, 61, 17);
		lblNewLabel.setText("\u670D\u52A1\u540D\u79F0:");

		textName = new Text(container, SWT.BORDER);
		textName.setBounds(116, 71, 283, 23);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				FileDialog fileSelect = new FileDialog(shell, SWT.SINGLE);
				fileSelect.setText("文件选择");
				String url = fileSelect.open();
				url = url.replaceAll("\\\\", "/");
				url = PATH + url;
				text.setText(url);
			}
		});

		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// 因为模型改变了，所以要及时更改界面
				CompositeSvcImportWizardPage.this.getContainer().updateButtons();
			}
		});
		textName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// 因为模型改变了，所以要及时更改界面
				CompositeSvcImportWizardPage.this.getContainer().updateButtons();
			}
		});

	}

	public boolean isPageComplete() {
		return getUrl().length() > 0 && getName().length() > 0;
	}

	public String getUrl() {
		return text.getText();
	}

	public String getName() {
		return textName.getText();
	}
}
