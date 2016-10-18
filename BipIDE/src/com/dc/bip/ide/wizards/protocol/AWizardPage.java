package com.dc.bip.ide.wizards.protocol;

import java.io.Serializable;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.dc.bip.ide.objects.ProtocolInService;

public class AWizardPage  extends WizardPage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5202308546203690426L;



	protected AWizardPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	private ProtocolInService protocolInService;
	

	public ProtocolInService getProtocolInService() {
		return protocolInService;
	}

	public void setProtocolInService(ProtocolInService protocolInService) {
		this.protocolInService = protocolInService;
	}

	@Override
	public void createControl(Composite arg0) {
		// TODO Auto-generated method stub
		
	}


}
