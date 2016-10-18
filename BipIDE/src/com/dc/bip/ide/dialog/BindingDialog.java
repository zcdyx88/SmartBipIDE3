package com.dc.bip.ide.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.reficio.ws.builder.SoapBuilder;

import com.dc.bip.ide.objects.BusiSvc;
import com.dc.bip.ide.views.objects.BSBindingNode;

public class BindingDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Label lblNamespace;
	private Label lblLabel;
	private Text text_2;
	private Table table;
	private TableColumn tblclmnEndpoint;
	private SoapBuilder soapBuilder;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public BindingDialog(Shell parent){
		super(parent);
	}
	public BindingDialog(Shell parent, int style, BSBindingNode bindingNode) {
		super(parent, style);
		setText(bindingNode.getNodeName());
		this.soapBuilder = bindingNode.getSoapBuilder();
		
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(56, 44, 54, 12);
		lblNewLabel.setText("WSDL URL:");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(147, 44, 200, 18);
		String url = soapBuilder.getServiceUrls().get(0);
		text.setData(url);
		text.setText(url);
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(147, 68, 200, 18);
		
		lblNamespace = new Label(shell, SWT.NONE);
		lblNamespace.setBounds(56, 71, 54, 12);
		lblNamespace.setText("Namespace");
		
		lblLabel = new Label(shell, SWT.NONE);
		lblLabel.setBounds(56, 95, 54, 12);
		lblLabel.setText("Binding");
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(147, 92, 200, 18);
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(56, 141, 291, 74);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("Name");
		
		tblclmnEndpoint = new TableColumn(table, SWT.NONE);
		tblclmnEndpoint.setWidth(100);
		tblclmnEndpoint.setText("Endpoint");
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(56, 123, 68, 12);
		lblNewLabel_1.setText("Operations");
		
	}
}
