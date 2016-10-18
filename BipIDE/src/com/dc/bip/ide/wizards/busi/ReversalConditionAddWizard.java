package com.dc.bip.ide.wizards.busi;

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
import com.dc.bip.ide.objects.ReversalCondition;

public class ReversalConditionAddWizard extends Wizard implements INewWizard {
	private Table table;
	private BusiSvcInfo svcInfo;

	public ReversalConditionAddWizard(Table table, BusiSvcInfo busiSvcInfo) {
		this.table = table;
		this.svcInfo = busiSvcInfo;
	}

	@Override
	public void addPages() {
		this.addPage(new ReversalConditionAddWizardPage());
	}

	@Override
	public boolean performFinish() {
		IWizardPage[] pages = this.getPages();
		ReversalConditionAddWizardPage reversalConditionAddWizardPage = (ReversalConditionAddWizardPage) pages[0];
		TableItem tableItem = new TableItem(table, SWT.NONE);
		String retCode = reversalConditionAddWizardPage.getReversalCheckField();
		String retValue = reversalConditionAddWizardPage.getReversalCheckValue();
		String desc = reversalConditionAddWizardPage.getReversalConditionDesc();
		tableItem.setText(0,retCode);
		tableItem.setText(1, retValue);
		tableItem.setText(2, desc);
		ReversalCondition reversalCondition = new ReversalCondition(retCode, retValue, desc);
		svcInfo.getReversalConditions().add(reversalCondition);
		
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
