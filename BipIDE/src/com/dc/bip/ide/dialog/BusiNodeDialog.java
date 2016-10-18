package com.dc.bip.ide.dialog;

import java.util.List;

import javax.wsdl.BindingOperation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.dc.bip.ide.views.objects.BSBindingNode;
import com.dc.bip.ide.views.objects.BusiNode;

public class BusiNodeDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Label lblNamespace;
	private BusiNode busiNode;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public BusiNodeDialog(Shell parent){
		super(parent);
	}
	public BusiNodeDialog(Shell parent, int style) {
		super(parent, style);
		
	}
	
	public BusiNodeDialog(Shell parent, int style, BusiNode busiNode) {
		super(parent, style);
		setText(busiNode.getNodeName());
		this.busiNode = busiNode;
		
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
		shell.setSize(536, 300);
		shell.setText(getText());
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(56, 82, 54, 12);
		lblNewLabel.setText("WSDL URL:");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(147, 79, 318, 18);
		String url = busiNode.getBusiSvc().getUrl();
		text.setText(url);
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(147, 112, 318, 18);
		String namespace = busiNode.getBusiSvc().getWsdlDef().getTargetNamespace();
		text_1.setText(namespace);
		
		lblNamespace = new Label(shell, SWT.NONE);
		lblNamespace.setBounds(56, 115, 54, 12);
		lblNamespace.setText("TargetNamespace");

	}
	
}
