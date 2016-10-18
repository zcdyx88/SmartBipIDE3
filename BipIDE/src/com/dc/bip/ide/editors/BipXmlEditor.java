package com.dc.bip.ide.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;

public class BipXmlEditor extends XMLMultiPageEditorPart{
	
	protected void createPages() {
		try {
			Composite composite = new Composite(getContainer(), SWT.NONE);
	        GridLayout layout = new GridLayout();
	        composite.setLayout(layout);
	        layout.numColumns = 2;

	        Button fontButton = new Button(composite, SWT.NONE);
	        GridData gd = new GridData(GridData.BEGINNING);
	        gd.horizontalSpan = 2;
	        fontButton.setLayoutData(gd);
	        fontButton.setText("one");

	        int index = addPage(composite);
	        setPageText(index, "Properties");
	       

	        Composite composite1 = new Composite(getContainer(), SWT.NONE);
	        GridLayout layout1 = new GridLayout();
	        composite1.setLayout(layout1);
	        layout1.numColumns = 2;

	        Button fontButton1 = new Button(composite1, SWT.NONE);
	        GridData gd1 = new GridData(GridData.BEGINNING);
	        gd1.horizontalSpan = 2;
	        fontButton1.setLayoutData(gd);
	        fontButton1.setText("two");
	       
	        index = addPage(composite1);
	        setPageText(index, "Properties1");
	        
	        super.createPages();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
