package com.dc.bip.ide;

import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.FlowElement;
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

public class CommonDialog extends Dialog {
	private Text idText;
	private Text nameText;

	private FlowElement esp;
	private Shell shell_1;

	public CommonDialog(Shell parentShell, Object object) {
		super(parentShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.esp = (FlowElement) object;
		setText(esp.getName() + " edit");
	}

	/**
	 * Opens the dialog and returns the input
	 * 
	 * @return String
	 */
	public String open() {
		// Create the dialog window
		shell_1 = new Shell(getParent(), getStyle());
		shell_1.setText(getText());
		shell_1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		shell_1.setSize(511, 157);
		Point location = getParent().getShell().getLocation();
		Point size = getParent().getShell().getSize();
		shell_1.setLocation((location.x + size.x - 500) / 2, (location.y + size.y - 300) / 2);
		createContents(shell_1);
				new Label(shell_1, SWT.NONE);
		
		
				Button ok = new Button(shell_1, SWT.PUSH);
				ok.setText("OK");
				GridData data = new GridData(GridData.FILL_HORIZONTAL);
				ok.setLayoutData(data);
				ok.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent event) {
						if (StringUtils.isEmpty(idText.getText())) {
							MessageDialog.openError(shell_1, "Validation error", "id must be filled.");
							return;
						}
						if (StringUtils.isEmpty(nameText.getText())) {
							MessageDialog.openError(shell_1, "Validation error", "name must be filled.");
							return;
						}
						esp.setId(idText.getText());
						esp.setName(nameText.getText());
						// esp.addAttribute(attribute);
						shell_1.close();
					}
				});
				
						// Set the OK button as the default, so
						// user can type input and press Enter
						// to dismiss
						shell_1.setDefaultButton(ok);
		
				// Create the cancel button and add a handler
				// so that pressing it will set input to null
				Button cancel = new Button(shell_1, SWT.PUSH);
				cancel.setText("Cancel");
				data = new GridData(GridData.FILL_HORIZONTAL);
				cancel.setLayoutData(data);
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

		Label idLabel = new Label(shell, SWT.NONE);
		idLabel.setAlignment(SWT.CENTER);
		idLabel.setText("id");
		idLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		idText = new Text(shell, SWT.BORDER);
		if (StringUtils.isNotEmpty(esp.getId())) {
			idText.setText(esp.getId());
		}
		GridData idData = new GridData(GridData.FILL_HORIZONTAL);
		idData.widthHint = 200;
		idText.setLayoutData(idData);

		
		Label nameLabel = new Label(shell, SWT.NONE);
		nameLabel.setAlignment(SWT.CENTER);
		nameLabel.setText("name");
		nameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		nameText = new Text(shell, SWT.BORDER);
		if (StringUtils.isNotEmpty(esp.getName())) {
			nameText.setText(esp.getName());
		}
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 200;
		nameText.setLayoutData(data);
		new Label(shell_1, SWT.NONE);
	}
	
	
}
