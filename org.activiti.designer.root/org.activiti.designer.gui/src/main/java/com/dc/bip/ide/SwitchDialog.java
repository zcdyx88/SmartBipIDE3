package com.dc.bip.ide;

import java.util.List;

import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.ServiceTask;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class SwitchDialog extends Dialog {
	private Text nameText;

	private ServiceTask esp;
	private Shell shell;
	private Table table;

	public SwitchDialog(Shell parentShell, Object object) {
		super(parentShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.esp = (ServiceTask) object;
		setText("编辑分支判断");
	}

	/**
	 * Opens the dialog and returns the input
	 * 
	 * @return String
	 */
	public String open() {
		// Create the dialog window
		shell = new Shell(getParent(), getStyle());
		shell.setText(getText());
		shell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		shell.setSize(580, 367);
		Point location = getParent().getShell().getLocation();
		Point size = getParent().getShell().getSize();
		shell.setLocation((location.x + size.x - 500) / 2, (location.y + size.y - 300) / 2);
		createContents(shell);
		new Label(shell, SWT.NONE);

		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 10);
		gd_table.heightHint = 182;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnType = new TableColumn(table, SWT.NONE);
		tblclmnType.setWidth(108);
		tblclmnType.setText("type");

		TableColumn tblclmnKe = new TableColumn(table, SWT.NONE);
		tblclmnKe.setWidth(122);
		tblclmnKe.setText("key");

		TableColumn tblclmnValue = new TableColumn(table, SWT.NONE);
		tblclmnValue.setWidth(100);
		tblclmnValue.setText("value");

		TableColumn tblclmnTarget = new TableColumn(table, SWT.NONE);
		tblclmnTarget.setWidth(229);
		tblclmnTarget.setText("target");
		
		List<SequenceFlow> outFlows = esp.getOutgoingFlows();
		if (null != outFlows && outFlows.size() > 0) {
			for(SequenceFlow sf : outFlows){
				TableItem item = new TableItem(table, SWT.NULL);
				item.setText(0, FlowElementUtil.getAttrValue(sf, "type"));
				item.setText(1, FlowElementUtil.getAttrValue(sf, "key"));
				item.setText(2, FlowElementUtil.getAttrValue(sf, "value"));
				item.setText(3, sf.getTargetRef());
			}
			
		}

		Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");

		GridData data = new GridData(GridData.FILL_HORIZONTAL);

		data.widthHint = 200;

		ok.setLayoutData(data);

		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (StringUtils.isEmpty(nameText.getText())) {
					MessageDialog.openError(shell, "Validation error", "name must be filled.");
					return;
				}
				esp.setName(nameText.getText());
				FlowElementUtil.saveAttr(esp, "type", "operation");
				shell.close();
			}
		});

		// Set the OK button as the default, so
		// user can type input and press Enter
		// to dismiss
		shell.setDefaultButton(ok);

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
		nameLabel.setText("判断名称");
		nameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		nameText = new Text(shell, SWT.BORDER);
		if (StringUtils.isNotEmpty(esp.getName())) {
			nameText.setText(esp.getName());
		}
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 200;
		nameText.setLayoutData(data);
		GridData typeData = new GridData(GridData.FILL_HORIZONTAL);
		typeData.widthHint = 200;
		new Label(shell, SWT.NONE);
	}
}
