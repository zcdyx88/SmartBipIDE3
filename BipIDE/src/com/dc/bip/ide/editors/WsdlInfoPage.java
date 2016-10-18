package com.dc.bip.ide.editors;

import org.eclipse.swt.widgets.Composite;

import com.dc.bip.ide.objects.BusiSvcInfo;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;

public class WsdlInfoPage extends Composite {

	private BusiSvcInfo busiSvcInfo;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public WsdlInfoPage(Composite parent, int style, BusiSvcInfo busiSvcInfo) {
		super(parent, style);
		this.busiSvcInfo = busiSvcInfo;
		
		Group group = new Group(this, SWT.NONE);
		group.setBounds(10, 35, 830, 88);
		group.setText("WSDL INFO");
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(20, 22, 54, 12);
		label.setText("WSDL URL:");
		
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setBounds(87, 22, 600, 12);
		lblNewLabel.setText(busiSvcInfo.getWsdlUrl());
		
		

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
