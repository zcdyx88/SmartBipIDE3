package com.dc.bip.ide;

import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.Pool;
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

public class PoolDialog extends Dialog {
	private Text nameText;
	private Text timeoutText;
	private Text countDownText;
	private Pool esp;
	private Shell shell_1;
	private GridData data_1;

	public PoolDialog(Shell parentShell, Object object) {
		super(parentShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.esp = (Pool) object;
		setText("loop edit");
	}

	/**
	 * Opens the dialog and returns the input
	 * 
	 * @return String
	 */
	public String open() {
		// Create the dialog window
		shell_1 = new Shell(getParent(), getStyle());
		shell_1.setText("编辑并行池");
		shell_1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		shell_1.setSize(511, 181);
		Point location = getParent().getShell().getLocation();
		Point size = getParent().getShell().getSize();
		shell_1.setLocation((location.x + size.x - 500) / 2, (location.y + size.y - 300) / 2);
		createContents(shell_1);

		//添加确认按钮
		Button ok = new Button(shell_1, SWT.PUSH);
		ok.setText("确认");
		ok.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (StringUtils.isEmpty(nameText.getText())) {
					MessageDialog.openError(shell_1, "Validation error", "name must be filled.");
					return;
				}
				if (StringUtils.isEmpty(timeoutText.getText())) {
					MessageDialog.openError(shell_1, "Validation error", "set expression must be filled.");
					return;
				}
				esp.setName(nameText.getText());
				FlowElementUtil.saveAttr(esp, "countDown", countDownText.getText());
				FlowElementUtil.saveAttr(esp, "timeout", timeoutText.getText());
				FlowElementUtil.saveAttr(esp, "type", "operation");
				ExtensionAttribute ea = new ExtensionAttribute();
				ea.setName("timeout");
				ea.setValue("hehehehe");
				esp.addAttribute(new ExtensionAttribute());
				shell_1.close();
			}
		});
		shell_1.setDefaultButton(ok);
		Button cancel = new Button(shell_1, SWT.PUSH);
		cancel.setText("取消");
		data_1 = new GridData(GridData.FILL_HORIZONTAL);
		data_1.widthHint = 201;
		cancel.setLayoutData(data_1);
		cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell_1.close();
			}
		});
		shell_1.open();
		Display display = getParent().getDisplay();
		while (!shell_1.isDisposed()) {
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
		nameLabel.setText("并行池名称");
		nameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		nameText = new Text(shell, SWT.BORDER);
		if (StringUtils.isNotEmpty(esp.getName())) {
			nameText.setText(esp.getName());
		}
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 200;
		nameText.setLayoutData(data);

		Label barrierLabel = new Label(shell, SWT.NONE);
		barrierLabel.setAlignment(SWT.CENTER);
		barrierLabel.setText("并行数");
		barrierLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		countDownText = new Text(shell, SWT.BORDER);
		GridData gd_barrierText = new GridData(GridData.FILL_HORIZONTAL);
		gd_barrierText.widthHint = 200;
		countDownText.setLayoutData(gd_barrierText);

		String countDown = FlowElementUtil.getAttrValue(esp, "countDown");
		if (StringUtils.isNotEmpty(countDown)) {
			countDownText.setText(countDown);
		}

		Label timeoutLabel = new Label(shell, SWT.NONE);
		timeoutLabel.setAlignment(SWT.CENTER);
		timeoutLabel.setText("超时时间");
		timeoutLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		timeoutText = new Text(shell, SWT.BORDER);
		GridData gd_timeoutText = new GridData(GridData.FILL_HORIZONTAL);
		gd_timeoutText.widthHint = 200;
		timeoutText.setLayoutData(gd_timeoutText);

		String timeout = FlowElementUtil.getAttrValue(esp, "timeout");
		if (StringUtils.isNotEmpty(timeout)) {
			timeoutText.setText(timeout);
		}
	}
}
