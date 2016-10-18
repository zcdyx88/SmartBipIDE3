package org.activiti.designer.property.extension.field;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.bpmn.model.Task;
import org.activiti.designer.integration.servicetask.PropertyType;
import org.activiti.designer.integration.validator.RequiredFieldValidator;
import org.activiti.designer.property.AbstractPropertyCustomTaskSection;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.util.BusiSvcService;

public class CustomProperyBusiSvcChoiceField extends AbstractCustomPropertyField {
	  private CCombo comboControl;
	  private Map<String, String> values;
	  private IProject currentProject;
	  private List<BusiSvcInfo>  busiSvcs = new ArrayList<BusiSvcInfo>();
	  private String[] labels = null;
	  private CLabel nameLabel;
	  private CLabel descLabel;
	  
	public CustomProperyBusiSvcChoiceField(AbstractPropertyCustomTaskSection section, Task task, Field field, IProject project) {
		super(section, task, field);
		this.currentProject = project;
		busiSvcs =  this.getBusiSVCList();
	}

	@Override
	public Composite render(Composite parent, TabbedPropertySheetWidgetFactory factory, FocusListener listener) {
		  final Composite result = factory.createFlatFormComposite(parent);
		    FormData data;

		      if (busiSvcs != null) {
			    int length = busiSvcs.size();
		        String[] itemValues = new String[length*2];
		        for(int i = 0 ;i<length;i++)
		        {
		        	itemValues[i*2]=busiSvcs.get(i).getId();
		        	itemValues[i*2+1]=busiSvcs.get(i).getId();
		        }	    

		        if (itemValues.length % 2 != 0) {
		          throw new IllegalArgumentException("Only an even number of property items is supported, every label must have a value item");
		        }
		        
		        values = new HashMap<String, String>();
		        labels = new String[itemValues.length / 2];

		        for (int i = 0; i < itemValues.length; i += 2) {
		          values.put(itemValues[i + 1], itemValues[i]);
		          labels[i / 2] = itemValues[i];
		        }
		      }

		    comboControl = factory.createCCombo(result, SWT.DROP_DOWN);
		    
		    comboControl.setEnabled(true);
		    comboControl.setItems(labels);

		    if (getPropertyAnnotation().required()) {
		      addFieldValidator(comboControl, RequiredFieldValidator.class);
		    }

		    if (getPropertyAnnotation().fieldValidator() != null) {
		      addFieldValidator(comboControl, getPropertyAnnotation().fieldValidator());
		    }

		    comboControl.addFocusListener(listener);
		    comboControl.addKeyListener(new KeyListener(){
				@Override
				public void keyPressed(KeyEvent e) {
					 
				}

				@Override
				public void keyReleased(KeyEvent e) {
					String filterStr = comboControl.getText();   					
   					if(null == filterStr  || "" == filterStr)
   				     {   				    	 
   				    	 comboControl.setItems(labels);
   				     }
   				     else
   				     {   				    	 
   				    	 String[]  tmpItems = comboControl.getItems();
   				    	comboControl.add("");
   				    	 for(String tmpLabel : tmpItems)
   				    	 {
				    			 comboControl.remove(tmpLabel); 
   				    	 }   				    	 
   				    	 for(String tmpLabel : labels)
   				    		 if(tmpLabel.contains(filterStr))
   				    		 {   				    			 
   				    			comboControl.add(tmpLabel); 
   				    		 }
   				    	 }
	   					if(1 < comboControl.getItemCount())
		    			 {
			    			   comboControl.remove("");
		    			 }
	   				 }    
			});	
		    comboControl.addSelectionListener(new SelectionListener(){

				@Override
				public void widgetSelected(SelectionEvent e) {
					BusiSvcInfo bs = getBusiSvcByName(comboControl.getText());
					if(null != bs )
					{
						nameLabel.setText("Name: "+bs.getId());
						descLabel.setText("Desc: "+bs.getDecscription());
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					
				}		    	
		    });
		    
		    data = new FormData();
		    data.left = new FormAttachment(0);
		    data.top = new FormAttachment(10);
		    data.right = new FormAttachment(100);
		    comboControl.setLayoutData(data);
		    
		   nameLabel = factory.createCLabel(result, "");
		    data = new FormData();
		    data.left = new FormAttachment(10);
		    data.top = new FormAttachment(comboControl,10);
		    data.right = new FormAttachment(90);
		    nameLabel.setLayoutData(data);	   
		   
		   descLabel = factory.createCLabel(result, "");
		    data = new FormData();
		    data.left = new FormAttachment(10);
		    data.top = new FormAttachment(nameLabel);
		    data.right = new FormAttachment(90);
		    descLabel.setLayoutData(data);		
		    
		    return result;
	}

	@Override
	public PropertyType getPrimaryPropertyType() {
		return PropertyType.BUSISVC_CHOICE;
	}

	@Override
	  public void refresh() {
	    final String value = getSimpleValueOrDefault();
	    for (final Entry<String, String> entry : values.entrySet()) {
	      if (entry.getKey().equals(value)) {
	        comboControl.setText(entry.getValue());
	        break;
	      }
	    }
	  }

	  @Override
	  public String getSimpleValue() {
	    String result = "";
	    for (final Entry<String, String> entry : values.entrySet()) {
	      if (entry.getValue().equals(comboControl.getText())) {
	        result = entry.getKey();
	        break;
	      }
	    }
	    return result;
	  }
	  
	  private List<BusiSvcInfo> getBusiSVCList()
	  {
		  if (currentProject == null)
		  {
			  return new ArrayList<BusiSvcInfo>();
		  }
		  
		  return BusiSvcService.getAll(currentProject.getName());		
	  }
	  
	  private BusiSvcInfo getBusiSvcByName(String name)
	    {
	    	for(BusiSvcInfo bs : busiSvcs)
	    	{
	    		if(bs.getId().equalsIgnoreCase(name))
	    		{
	    			return bs;
	    		}
	    	}
	    	return null;
	    	
	    }
}
