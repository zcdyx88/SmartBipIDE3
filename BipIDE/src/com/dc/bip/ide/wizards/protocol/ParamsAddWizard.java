package com.dc.bip.ide.wizards.protocol;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.objects.PramsInfo;
import com.dc.bip.ide.objects.ProtocolInService;
import com.dc.bip.ide.objects.ReversalCondition;

public class ParamsAddWizard extends Wizard implements INewWizard {
	private Table table;
	private ProtocolInService protocolService;

	public ParamsAddWizard(Table table, ProtocolInService busiSvcInfo) {
		this.table = table;
		this.protocolService = busiSvcInfo;
	}

	@Override
	public void addPages() {
		this.addPage(new ParamsAddWizardPage());
	}

	@Override
	public boolean performFinish() {
		IWizardPage[] pages = this.getPages();
		ParamsAddWizardPage paramsAddWizardPage = (ParamsAddWizardPage) pages[0];
		TableItem tableItem = new TableItem(table, SWT.NONE);
		String key = paramsAddWizardPage.getParamKey();
		String value = paramsAddWizardPage.getParamValue();
		tableItem.setText(0,key);
		tableItem.setText(1, value);
		PramsInfo reversalCondition = new PramsInfo(key, value);
		if("TCP".equals(protocolService.getProtocolType())){
			protocolService.getParamsModels().add(reversalCondition);
		}else{
			protocolService.getHeadMsgDef().add(reversalCondition);
		}
		
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	class LongRunningOperation implements IRunnableWithProgress {
		// The total sleep time
		private static final int TOTAL_TIME = 10000;
		// The increment sleep time
		private static final int INCREMENT = 500;
		private boolean indeterminate;

		public LongRunningOperation(boolean indeterminate) {
			this.indeterminate = indeterminate;
		}

		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			monitor.beginTask("Running long running operation", indeterminate ? IProgressMonitor.UNKNOWN : TOTAL_TIME);
			// monitor.subTask("Doing first half");
			for (int total = 0; total < TOTAL_TIME && !monitor.isCanceled(); total += INCREMENT) {
				Thread.sleep(INCREMENT);
				monitor.worked(INCREMENT);
				if (total == TOTAL_TIME / 2)
					monitor.subTask("Doing second half");
			}
			monitor.done();
			if (monitor.isCanceled())
				throw new InterruptedException("The long running operation was cancelled");
		}
	}
}
