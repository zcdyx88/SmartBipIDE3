package com.dc.bip.ide.editors.busi;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;

public class SDAEditor extends XMLMultiPageEditorPart {
	
	public static final String ID = "wizard.editor.sdaEditor";
	
	
	public SDAEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPages() {
//		createPage1()
		super.createPages();
	}


	@Override
	public void doSave(IProgressMonitor monitor) {
//		this.getActiveEditor().doSave(monitor);
		super.doSave(monitor);

	}

	@Override
	public void doSaveAs() {
//		this.getActiveEditor().doSaveAs();
		super.doSaveAs();
	}

	@Override
	public boolean isSaveAsAllowed() {
//		return false;
		return super.isSaveAsAllowed();
	}


}
