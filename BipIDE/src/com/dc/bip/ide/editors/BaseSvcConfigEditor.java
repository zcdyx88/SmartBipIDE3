package com.dc.bip.ide.editors;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.util.HelpUtil;

public class BaseSvcConfigEditor extends EditorPart implements IEditorPart {
	private Text text_id;
	private Text text_nickName;
	private Text text_desc;
	private Text text_moudle;
	private Text text_implement;
	private BaseService bsNode;
	private boolean dirty = false;
	private IFile file;
	
	
	private void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public BaseSvcConfigEditor(BaseService bs){
		this.bsNode = bs;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		String vm = "base.vm";         
	        try
	        {
	            IFolder folder = (IFolder)file.getParent();
	            if(!folder.exists())
	                HelpUtil.mkdirs(folder, true, true, monitor);
	            HelpUtil.change2Writeable(folder);
	            InputStream stream = HelpUtil.openContentStream(bsNode, vm);
	            if(file.exists())
	            {
	                if(file.isReadOnly())
	                    file.setReadOnly(false);
	                file.setContents(stream, true, true, monitor);
	            } else
	            {
	                file.create(stream, true, monitor);
	            }
	            stream.close();
	    		setDirty(false);
	    		firePropertyChange(PROP_DIRTY);
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		MessageDialog.openInformation(Display.getCurrent().getActiveShell(), null,"doSaveAs");
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.setSite(site);
		super.setInput(input);
		file =((FileEditorInput)input).getFile();	
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void createPartControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(null);
		
		Label lblid = new Label(composite, SWT.NONE);
		lblid.setBounds(21, 23, 25, 17);
		lblid.setText("ID\uFF1A");
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(10, 60, 36, 17);
		lblNewLabel.setText("\u522B\u540D\uFF1A");
		
		Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 209, 36, 17);
		label.setText("\u63CF\u8FF0\uFF1A");
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(10, 156, 36, 17);
		label_1.setText("\u6A21\u5757\uFF1A");
			
        try{
        	text_id = new Text(composite, SWT.BORDER);
       
			text_id.setEditable(false);
			text_id.setBounds(61, 19, 510, 24);
			text_id.setText(bsNode.getName());
		
		
			text_nickName = new Text(composite, SWT.BORDER);
			text_nickName.setBounds(61, 57, 510, 24);
			text_nickName.setText(bsNode.getNickName());
			text_nickName.addModifyListener(new ModifyListener(){
	
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
					firePropertyChange(PROP_DIRTY);
				}
				
			});
			
			text_desc = new Text(composite, SWT.BORDER);
			text_desc.setBounds(61, 206, 510, 103);
			text_desc.setText(bsNode.getDescription());
			text_desc.addModifyListener(new ModifyListener(){
	
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
					firePropertyChange(PROP_DIRTY);
				}
				
			});
			
			text_moudle = new Text(composite, SWT.BORDER);
			text_moudle.setEditable(false);
			text_moudle.setBounds(61, 153, 510, 24);
			text_moudle.setText(bsNode.getContributionId());
			
			text_implement = new Text(composite, SWT.BORDER);
			text_implement.setEditable(false);
			text_implement.setBounds(61, 102, 510, 24);
			text_implement.setText(bsNode.getImpls());
			text_implement.addModifyListener(new ModifyListener(){
				
				@Override
				public void modifyText(ModifyEvent e) {				
					setDirty(true);
					firePropertyChange(PROP_DIRTY);
				}
				
			});
			
			Label lblNewLabel_1 = new Label(composite, SWT.NONE);
			lblNewLabel_1.setBounds(10, 105, 46, 17);
			lblNewLabel_1.setText("\u5B9E\u73B0\u7C7B\uFF1A");
			
			initDataBindings();
			setDirty(false);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	 protected DataBindingContext initDataBindings()
	    {
		 		DataBindingContext bindingContext = new DataBindingContext();  
		         IObservableValue uiElement;  
		         IObservableValue modelElement;  
		         
		        // Lets bind it  
		      uiElement = SWTObservables.observeText(text_nickName, SWT.Modify);  
		       modelElement = BeansObservables.observeValue(bsNode, "nickName");  
		        bindingContext.bindValue(uiElement, modelElement, null, null);  
		  
		       uiElement = SWTObservables.observeText(text_implement, SWT.Modify);  
		        modelElement = BeansObservables.observeValue(bsNode, "impls");  
		        bindingContext.bindValue(uiElement, modelElement, null, null);  
		  
		        uiElement = SWTObservables.observeText(text_desc, SWT.Modify);  
		     modelElement = BeansObservables.observeValue(bsNode, "description");  	 
		        bindingContext.bindValue(uiElement, modelElement, null, null);  
	 

		        return bindingContext;
	    }
}
