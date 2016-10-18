package com.dc.bip.ide.wizards.composite;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.dc.bip.ide.objects.CompServiceBaseInfo;
import com.dc.bip.ide.objects.CompositeService;

public class CompositeSvcWizardPage extends WizardPage {

	private CompositeService compositeService  = new CompositeService();
	private CompServiceBaseInfo compServiceBaseInfo  = new CompServiceBaseInfo();
	

	private static String wp = "compositeSvcWizarfPage1";

	protected CompositeSvcWizardPage() {
		super(wp);
	}

	@Override
	public void createControl(Composite parent) {
		// 在生成UI之前，先设为未完成
		// this.setPageComplete(false);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(2, false));

		// id
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		label.setText("P服务ID:");
		Text text = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text.setText((new StringBuilder()).append("Proxy").append(System.currentTimeMillis()).toString());
		compServiceBaseInfo.setServiceId((new StringBuilder()).append("Proxy").append(System.currentTimeMillis()).toString());

		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				compServiceBaseInfo.setServiceId(((Text) e.getSource()).getText());
				CompositeSvcWizardPage.this.getContainer().updateButtons();
			}
		});

		// pname
		label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		label.setText("N服务名称:");
		text = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text.setText("");
		compServiceBaseInfo.setServiceName(text.getText());
		
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				compServiceBaseInfo.setServiceName(((Text) e.getSource()).getText());
				CompositeSvcWizardPage.this.getContainer().updateButtons();
			}
		});

		label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		label.setText("D功能描述:");
		text = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_text.heightHint = 99;
		text.setLayoutData(gd_text);
		compServiceBaseInfo.setDescrition(text.getText());
		
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				compServiceBaseInfo.setDescrition(((Text) e.getSource()).getText());
				CompositeSvcWizardPage.this.getContainer().updateButtons();
			}
		});
		this.setTitle("组合服务创建向导");
		this.setMessage("在当前目录下创建一个组合服务");
		this.setControl(composite);
	}

	@Override
	public boolean isPageComplete() {
		compositeService.setNEW(true);  //设置为true
		return compServiceBaseInfo.getServiceName().length() > 0 ;
//		return true;
	}

	public CompositeService getCompositeService() {
		//返回pop窗口compServiceBaseInfo对象
		compositeService.setBaseinfo(compServiceBaseInfo);
		compositeService.setNEW(true);  //设置为true
//		System.out.println(" Wizards compositeService.getBaseinfo()="+ compositeService.getBaseinfo());
		return compositeService;
	}


}
