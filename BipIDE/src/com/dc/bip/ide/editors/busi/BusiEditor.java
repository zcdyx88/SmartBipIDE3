package com.dc.bip.ide.editors.busi;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.MultiPageEditorPart;
import com.dc.bip.ide.editors.util.xmlEditor.*;

public class BusiEditor extends MultiPageEditorPart {
	
	public static final String ID = "wizard.editor.busiEditor";
	
	private TextEditor editor;
	
	private XMLEditor xmlEditor;
	
	public BusiEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createPages() {
		createPage0();
		createPage1();
	}
	
	void createPage0() {
		try {
			editor = new TextEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, editor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested text editor",
				null,
				e.getStatus());
		}
	}
	
	void createPage1(){
		try{
			xmlEditor = new XMLEditor();
			int index = addPage(xmlEditor,getEditorInput());
			setPageText(index,xmlEditor.getTitle());
		}catch (PartInitException e) {
			ErrorDialog.openError(
					getSite().getShell(),
					"Error creating nested text editor",
					null,
					e.getStatus());
			}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}


}
