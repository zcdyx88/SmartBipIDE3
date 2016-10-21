package com.dc.bip.ide;

import org.activiti.bpmn.model.EventSubProcess;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class LoopDialog extends Dialog {
	private Text nameText;
	private Text loopConditionText;
	private Text loopConditionTextkey;
	private Combo loopTypeCombo;
	private EventSubProcess esp;
	private Shell shell_1;
	private GridData data_1;

	public LoopDialog(Shell parentShell, Object object) {
		super(parentShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.esp = (EventSubProcess) object;
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
		shell_1.setText("编辑循环");
		shell_1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		shell_1.setSize(511, 205);
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
				if (StringUtils.isEmpty(loopConditionText.getText())) {
					MessageDialog.openError(shell_1, "Validation error", "set expression must be filled.");
					return;
				}
				esp.setName(nameText.getText());
				FlowElementUtil.saveAttr(esp, "loopType", loopTypeCombo.getText());
				FlowElementUtil.saveAttr(esp, "loopCondition", loopConditionText.getText());
				FlowElementUtil.saveAttr(esp, "loopConditionkey", loopConditionTextkey.getText());
				FlowElementUtil.saveAttr(esp, "type", "operation");
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
		nameLabel.setText("循环名称");
		nameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		nameText = new Text(shell, SWT.BORDER);
		if (StringUtils.isNotEmpty(esp.getName())) {
			nameText.setText(esp.getName());
		}
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 200;
		nameText.setLayoutData(data);

		Label loopTypeLabel = new Label(shell, SWT.NONE);
		loopTypeLabel.setAlignment(SWT.CENTER);
		loopTypeLabel.setText("循环类型");
		loopTypeLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		loopTypeCombo = new Combo(shell_1, SWT.NONE);
		loopTypeCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {

			}
		});

		loopTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		loopTypeCombo.add("while");
		loopTypeCombo.add("for");
		loopTypeCombo.add("foreach");

		String loopType = FlowElementUtil.getAttrValue(esp, "loopType");
		loopTypeCombo.setText(loopType);

		Label loopConditionLabel = new Label(shell, SWT.NONE);
		loopConditionLabel.setAlignment(SWT.CENTER);
		loopConditionLabel.setText("循环条件(set)");
		loopConditionLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		loopConditionText = new Text(shell, SWT.BORDER);
		GridData gd_loopConditionText = new GridData(GridData.FILL_HORIZONTAL);
		gd_loopConditionText.widthHint = 200;
		loopConditionText.setLayoutData(gd_loopConditionText);

		String loopCondition = FlowElementUtil.getAttrValue(esp, "loopCondition");
		if (StringUtils.isNotEmpty(loopCondition)) {
			loopConditionText.setText(loopCondition);
		}
		
		//新增循环key值
		Label loopConditionLabel_key = new Label(shell, SWT.NONE);
		loopConditionLabel_key.setAlignment(SWT.CENTER);
		loopConditionLabel_key.setText("循环变量(key)");
		loopConditionLabel_key.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		loopConditionTextkey = new Text(shell, SWT.BORDER);
		
		GridData gd_loopConditionText_key = new GridData(GridData.FILL_HORIZONTAL);
		gd_loopConditionText_key.widthHint = 200;
		loopConditionTextkey.setLayoutData(gd_loopConditionText_key);
		
		String loopConditionkey = FlowElementUtil.getAttrValue(esp, "loopConditionkey");
		if (StringUtils.isNotEmpty(loopConditionkey)) {
			loopConditionTextkey.setText(loopConditionkey);
		}
		
	}

}
