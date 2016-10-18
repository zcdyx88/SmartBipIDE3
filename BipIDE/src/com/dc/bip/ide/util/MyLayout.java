package com.dc.bip.ide.util;

import org.eclipse.swt.*; 
import org.eclipse.swt.custom.*; 
import org.eclipse.swt.graphics.*; 
import org.eclipse.swt.layout.*; 
import org.eclipse.swt.widgets.*; 
 
public class MyLayout { 
    public static void main (String [] args) { 
        Display display = new Display (); 
        Shell shell = new Shell (display); 
        GridLayout gridLayout = new GridLayout (); 
        gridLayout.numColumns = 4; 
        shell.setLayout (gridLayout); 
 
        Label label0 = new Label (shell, SWT.NONE); 
        label0.setText ("label0"); 
         
        Text text1 = new Text (shell, SWT.BORDER); 
        text1.setText ("text1"); 
        GridData data = new GridData (); 
        data.horizontalAlignment = GridData.FILL; 
        data.horizontalSpan = 2; 
        data.grabExcessHorizontalSpace = true; 
        text1.setLayoutData (data); 
 
        Button button2 = new Button (shell, SWT.PUSH); 
        button2.setText ("button2"); 
        data = new GridData (); 
        data.horizontalAlignment = GridData.FILL; 
        data.verticalAlignment = GridData.FILL; 
        data.horizontalSpan = 2; 
        data.grabExcessHorizontalSpace = false; //Wrong! 
        button2.setLayoutData(data);
         
        Text text3 = new Text (shell, SWT.BORDER); 
        text3.setText ("text3"); 
        data = new GridData (); 
        data.horizontalAlignment = GridData.FILL; 
        data.verticalAlignment = GridData.FILL; 
        data.horizontalSpan = 4; 
        data.grabExcessHorizontalSpace = false; //Wrong! 
        text3.setLayoutData (data); 
 
        shell.pack (); 
        shell.open (); 
 
        while (!shell.isDisposed ()) { 
            if (!display.readAndDispatch ()) 
                display.sleep (); 
        } 
        display.dispose (); 
    } 
}