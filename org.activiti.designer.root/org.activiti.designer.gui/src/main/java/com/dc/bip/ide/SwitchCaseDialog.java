package com.dc.bip.ide;

import org.activiti.bpmn.model.SequenceFlow;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SwitchCaseDialog extends Dialog {
//	private Text idText;
	private Text nameText;
//	private Text typeText;
	private Text keyText;
	private Text valueText;

	private SequenceFlow esp;

	public SwitchCaseDialog(Shell parentShell, Object object) {
		super(parentShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.esp = (SequenceFlow) object;
		setText("编辑条件分支");
	}

	/**
	 * Opens the dialog and returns the input
	 * 
	 * @return String
	 */
	public String open() {
		// Create the dialog window
		Shell shell = new Shell(getParent(), getStyle());
		shell.setText(getText());
		shell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		shell.setSize(511, 218);
		Point location = getParent().getShell().getLocation();
		Point size = getParent().getShell().getSize();
		shell.setLocation((location.x + size.x - 500) / 2, (location.y + size.y - 300) / 2);
		createContents(shell);
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return null;
	}

	/**
	 * Creates the dialog's contents
	 * 
	 * @param shell
	 *            the dialog window
	 */
	private void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(2, false));
		Label nameLabel = new Label(shell, SWT.NONE);
		nameLabel.setAlignment(SWT.CENTER);
		nameLabel.setText("分支名称");
		nameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		nameText = new Text(shell, SWT.BORDER);
		if (StringUtils.isNotEmpty(esp.getName())) {
			nameText.setText(esp.getName());
		}
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 200;
		nameText.setLayoutData(data);

		Label keyLabel = new Label(shell, SWT.NONE);
		keyLabel.setAlignment(SWT.CENTER);
		keyLabel.setText("分支条件域");
		keyLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		keyText = new Text(shell, SWT.BORDER);
		String keyExp = FlowElementUtil.getAttrValue(esp, "key");
		if (StringUtils.isNotEmpty(keyExp)) {
			keyText.setText(keyExp);
		}
		GridData keyData = new GridData(GridData.FILL_HORIZONTAL);
		keyData.widthHint = 200;
		keyText.setLayoutData(keyData);


		Label valueLabel = new Label(shell, SWT.NONE);
		valueLabel.setAlignment(SWT.CENTER);
		valueLabel.setText("分支条件值");
		valueLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		valueText = new Text(shell, SWT.BORDER);
		String valueExp = FlowElementUtil.getAttrValue(esp, "value");
		if (StringUtils.isNotEmpty(valueExp)) {
			valueText.setText(valueExp);
		}
		GridData valueData = new GridData(GridData.FILL_HORIZONTAL);
		valueData.widthHint = 200;
		valueText.setLayoutData(valueData);

		Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		data = new GridData(GridData.FILL_HORIZONTAL);
		ok.setLayoutData(data);
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (StringUtils.isEmpty(nameText.getText())) {
					MessageDialog.openError(shell, "Validation error", "name must be filled.");
					return;
				}
				if (StringUtils.isEmpty(valueText.getText())) {
					MessageDialog.openError(shell, "Validation error", "value expression must be filled.");
					return;
				}
				if (StringUtils.isEmpty(keyText.getText())) {
					MessageDialog.openError(shell, "Validation error", "key must be filled.");
					return;
				}
			
				esp.setName(nameText.getText());
				FlowElementUtil.saveAttr(esp, "value", valueText.getText());
				FlowElementUtil.saveAttr(esp, "key", keyText.getText());
				FlowElementUtil.saveAttr(esp, "type", "operation");
				shell.close();
			}
		});

		// Create the cancel button and add a handler
		// so that pressing it will set input to null
		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText("Cancel");
		data = new GridData(GridData.FILL_HORIZONTAL);
		cancel.setLayoutData(data);
		cancel.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				shell.close();
			}
		});

		// Set the OK button as the default, so
		// user can type input and press Enter
		// to dismiss
		shell.setDefaultButton(ok);
	}
	
}
