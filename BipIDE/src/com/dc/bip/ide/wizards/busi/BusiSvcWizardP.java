package com.dc.bip.ide.wizards.busi;

import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class BusiSvcWizardP extends WizardPage{
	
	private String url = "";  
    private String opName = "";
    private String serviceName = "";

 	
    protected BusiSvcWizardP() {  
        super("MyWizardPage1");  
    }  
    @Override  
    public void createControl(Composite parent) {  
        // 在生成UI之前，先设为未完成  
        // this.setPageComplete(false);  
        Composite composite = new Composite(parent, SWT.NONE);  
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));  
        composite.setLayout(new GridLayout(2, false));  
        Label label = new Label(composite, SWT.NONE);  
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,  
                false));  
        label.setText("\u670D\u52A1\u8DEF\u5F84:");  
        Text text = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);  
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        url = text.getText();
        BusiSvcWizardP.this.getWizard().getDialogSettings().put("url",  text.getText());  
        text.addModifyListener(new ModifyListener() {  
            @Override  
            public void modifyText(ModifyEvent e) {  
                // 把数据存在自己手里，对外提供get API  
            	url = ((Text) e.getSource()).getText();  
                // 把数据统一交给wizard里面的通用存储器DialogSettings来存值储  
            	BusiSvcWizardP.this.getWizard().getDialogSettings().put("url",  
                        ((Text) e.getSource()).getText());  
                // 因为模型改变了，所以要及时更改界面  
            	BusiSvcWizardP.this.getContainer().updateButtons();  
            }  
        }); 
        
        
        label = new Label(composite, SWT.NONE);  
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,  
                false));  
        label.setText("\u670D\u52A1\u540D\u79F0:");  
        text = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);  
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        serviceName = text.getText();
        text.addModifyListener(new ModifyListener() {  
            @Override  
            public void modifyText(ModifyEvent e) {  
            	serviceName = ((Text) e.getSource()).getText();  
                BusiSvcWizardP.this.getWizard().getDialogSettings().put("serviceName",  
                        ((Text) e.getSource()).getText());  
                // 因为模型改变了，所以要及时更改界面  
                BusiSvcWizardP.this.getContainer().updateButtons();  
            }  
        });
        String filePath = System.getProperty("user.dir")+File.separator+url+".java";
        this.setTitle("业务服务导入向导");  
        this.setMessage("在当前目录下导入业务服务");  
        this.setControl(composite);
        
        Label lblNewLabel = new Label(composite, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("\u64CD\u4F5C\u573A\u666F:");
        
        text = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);  
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        opName =  text.getText();
        text.addModifyListener(new ModifyListener() {  
            @Override  
            public void modifyText(ModifyEvent e) {  
            	opName = ((Text) e.getSource()).getText();  
                BusiSvcWizardP.this.getWizard().getDialogSettings().put("opName",  
                        ((Text) e.getSource()).getText());  
                // 因为模型改变了，所以要及时更改界面  
                BusiSvcWizardP.this.getContainer().updateButtons();  
            }  
        });
    }  
    @Override  
    // 重写这个方法，不再使用以前的flag  
    public boolean isPageComplete() {  
        return url.length() > 0 && opName.length() > 0;  
    }  


}
