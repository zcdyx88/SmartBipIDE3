package com.dc.bip.ide.editors.publiccode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.dc.bip.ide.context.PublicCode;

public class AddPublicCodePage extends Composite {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private String projectName;
	private Display display;
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public AddPublicCodePage(final Composite parent, int style, PublicCode[] bipServer,final String projectName) {
		
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		this.projectName = projectName;
		
		Composite composite = new Composite(this, SWT.NONE);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(207, 128, 61, 17);
		formToolkit.adapt(lblNewLabel, true, true);
		lblNewLabel.setText("New Label");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public boolean setFocus() {
		// TODO Auto-generated method stub
		return true;
	}
}
