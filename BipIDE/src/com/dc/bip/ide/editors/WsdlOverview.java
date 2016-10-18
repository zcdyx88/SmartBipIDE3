package com.dc.bip.ide.editors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.SWT;

public class WsdlOverview extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public WsdlOverview(Composite parent, int style) {
		super(parent, style);
		setLayout(new RowLayout(SWT.HORIZONTAL));

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
