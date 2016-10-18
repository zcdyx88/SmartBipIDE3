package com.dc.bip.ide.wizards.basesvc;


import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.dc.bip.ide.objects.BaseService;

public class BaseSvcWizardP extends WizardPage {


	 	private BaseService bs = new BaseService();
	 	private Text implsText=null;
	 	private Label lblId;
	 	private Label label_1;
	 	private Label label_2;
	 	
	    protected BaseSvcWizardP() {  
	    	super("baseSvcWizarfPage1"); 
	    }  
	    
	    @Override  
	    public void createControl(Composite parent) {  
	    	
	        // 在生成UI之前，先设为未完成  
	        // this.setPageComplete(false);  
	        Composite composite = new Composite(parent, SWT.NONE);  
	        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
	        composite.setLayout(new GridLayout(2, false)); 
	        

	        
	        Label label;
	        lblId = new Label(composite, SWT.NONE);
	        lblId.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,  
	                false));  
	        lblId.setText("ID:");  
	        Text text = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);  
	        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false)); 
	        text.setText((new StringBuilder()).append("Base").append(System.currentTimeMillis()).toString());
	        bs = new BaseService(text.getText());
	        
	        text.addModifyListener(new ModifyListener() {  
	            @Override  
	            public void modifyText(ModifyEvent e) {  	            
	            	bs.setName( ((Text) e.getSource()).getText());
//	            	implsText.setText((new StringBuilder("com.dc.branch.impls.base.")).append(bs.getName()).toString());
	            	implsText.setText((new StringBuilder("com.dcits.smartbip.runtime.impl.")).append(bs.getName()).toString());
	            	bs.setImpls(implsText.getText()); 
	                BaseSvcWizardP.this.getContainer().updateButtons();  
	            }  
	        }); 
	        
	        
	        label = new Label(composite, SWT.NONE);
	        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,  
	                false));  
	        label.setText("名称:");  
	        text = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);  
	        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	        text.setText("");  
//	        bs.setLocation(text.getText());
	        bs.setNickName(text.getText());
	        text.addModifyListener(new ModifyListener() {  
	            @Override  
	            public void modifyText(ModifyEvent e) {  
	            	bs.setNickName(((Text)e.getSource()).getText());
	                BaseSvcWizardP.this.getContainer().updateButtons();  
	            }  
	        });
	        
//	        label = new Label(composite, SWT.NONE);  
//	        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,  
//	                false));  
//	        label.setText("模块:");  
//	        text = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);  
//	        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
//	        text.setText("服务模块");
//	        text.setEditable(false);
	        bs.setContributionId("1");
	        
	      /*  text.addModifyListener(new ModifyListener() {  
	            @Override  
	            public void modifyText(ModifyEvent e) {  
	                password = ((Text) e.getSource()).getText();  
	                BaseSvcWizardP.this.getWizard().getDialogSettings().put("模块",  
	                        ((Text) e.getSource()).getText());  
	                // 因为模型改变了，所以要及时更改界面  
	                BaseSvcWizardP.this.getContainer().updateButtons();  
	            }  
	        }); */
	        
	        label_1 = new Label(composite, SWT.NONE);
	        label_1.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,  
	                false));  
	        label_1.setText("实现类:");  
	         implsText = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
	         implsText.setEditable(false);
	        implsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	        String filePath = (new StringBuilder("com.dcits.smartbip.runtime.impl.")).append(bs.getName()).toString();
	        implsText.setText(filePath);
	        bs.setImpls(implsText.getText());  
	        
	        text.addModifyListener(new ModifyListener() {  
	            @Override  
	            public void modifyText(ModifyEvent e) {  
//	            	 bs.setImpls(((Text) e.getSource()).getText());  	
	                BaseSvcWizardP.this.getContainer().updateButtons();  
	            }  
	        });  
	        
	        label_2 = new Label(composite, SWT.NONE);
	        label_2.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,  
	                false));  
	        label_2.setText("描述:");  
	        text = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);  
	        GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false);
	        gd_text.heightHint = 77;
	        text.setLayoutData(gd_text);  
	        text.addModifyListener(new ModifyListener() {  
	            @Override  
	            public void modifyText(ModifyEvent e) {  
	              bs.setDescription( ((Text) e.getSource()).getText());  
	                // 因为模型改变了，所以要及时更改界面  
	                BaseSvcWizardP.this.getContainer().updateButtons();  
	            }  
	        });  
	        this.setTitle("基础服务创建向导");  
	        this.setMessage("在当前目录下创建一个基础服务");  
	        this.setControl(composite);  
	    }  
	    @Override  
	    // 重写这个方法，不再使用以前的flag  
	    public boolean isPageComplete() {  
	        return bs.getName().length() > 0  && bs.getImpls().length() > 0;  
	    }  
	    
	    public BaseService getBaseService()
	    {
	    	return bs;
	    }

}
