package org.activiti.designer.property.extension.field;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
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
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
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

import com.dc.bip.ide.objects.BaseService;


public class CustomProperyBaseSvcChoiceField extends AbstractCustomPropertyField {
	  private CCombo comboControl;
	  private Map<String, String> values;
	  private IProject currentProject;
	  private List<BaseService>  baseSvcs = new ArrayList<BaseService>();
	  private String[] labels = null;
	  private CLabel nameLabel;
	  private CLabel descLabel;
	  
	public CustomProperyBaseSvcChoiceField(AbstractPropertyCustomTaskSection section, Task task, Field field, IProject project) {
		super(section, task, field);
		this.currentProject = project;
		baseSvcs =  this.getBaseSVCList();
	}


	@Override
	public Composite render(Composite parent, TabbedPropertySheetWidgetFactory factory, FocusListener listener) {
		  final Composite result = factory.createFlatFormComposite(parent);
		    FormData data;

		      if (baseSvcs != null) {
			    int length = baseSvcs.size();
		        String[] itemValues = new String[length*2];
		        for(int i = 0 ;i<length;i++)
		        {
		        	itemValues[i*2]=baseSvcs.get(i).getName();
		        	itemValues[i*2+1]=baseSvcs.get(i).getName();
		        }	    

		        if (itemValues.length % 2 != 0) {
		          throw new IllegalArgumentException("Only an even number of property items is supported, every label must have a value item");
		        }
		        
		        values = new HashMap<String, String>();
		        labels = new String[itemValues.length / 2];

		        for (int i = 0; i < itemValues.length; i += 2) {
//		        	System.out.println(i+1+"= "+itemValues[i + 1]+" ;"+i+"= "+itemValues[i]);
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
					BaseService bs = getBaseSvcByName(comboControl.getText());
					if(null != bs )
					{
						nameLabel.setText("Name: "+bs.getName());
						descLabel.setText("Desc: "+bs.getDescription());
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
		return PropertyType.BASESVC_CHOICE;
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
	  
	  private List<BaseService> getBaseSVCList()
	  {
		  if (currentProject == null)
		  {
			  System.out.println("δ��ȡ����ǰ�Ĺ���");
			  return new ArrayList<BaseService>();
		  }
		  
		 
		  String folderPath = currentProject.getFolder("/dev/services/base/").getLocation().toString();
			File baseFolder = new File(folderPath);
			 FilenameFilter fileNameFilter = new FilenameFilter() {			   
		            @Override
		            public boolean accept(File dir, String name) {
		               if(name.lastIndexOf('.')>0)
		               {
		                  int lastIndex = name.lastIndexOf('.');
		                  String str = name.substring(lastIndex);
		                  if(str.equals(".base1"))
		                  {
		                     return true;
		                  }
		               }
		               return false;
		            }
		         };
		     	File[] baseFiles = baseFolder.listFiles(fileNameFilter);
		     	BaseService bs;
		     	List<BaseService> baseSvcList = new ArrayList<BaseService>();
				if (baseFiles != null && baseFiles.length>0)
				{
					for(int i = 0 ;i<baseFiles.length;i++)
					{
						try {
							bs = parseBaseSvcXml(new FileInputStream(baseFiles[i]));
							baseSvcList.add(bs);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//bs = new BaseService(baseFiles[i].getName());											
					}	
				}
				return baseSvcList;
	  }
	  
	   private  BaseService parseBaseSvcXml(InputStream input) {
			Document doc;
			BaseService	bs = new BaseService();
			try {
				SAXReader reader = new SAXReader();
				doc = reader.read(input);
				bs =new BaseService(getNodeText(doc, "/root/id", true));
				bs.setImpls(getNodeText(doc, "/root/impl", true));
				bs.setDescription(getNodeText(doc, "/root/description", true));
				bs.setLocation(getNodeText(doc, "/root/location", true));	
				bs.setNickName(getNodeText(doc, "/root/nickName", true));		
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bs;
		}
	   
	    private String getNodeText(Document doc, String idPath, boolean trim) {
			Node node = doc.selectSingleNode(idPath);
			if (node != null) {
				if (trim) {
					if (node.getText() != null)
						return node.getText().trim();
					else
						return "";
				} else {
					return node.getText();
				}
			} else {
				return null;
			}
		}
	    
	    private BaseService getBaseSvcByName(String name)
	    {
	    	for(BaseService bs : baseSvcs)
	    	{
	    		if(bs.getName().equalsIgnoreCase(name))
	    		{
	    			return bs;
	    		}
	    	}
	    	return null;
	    	
	    }

}
