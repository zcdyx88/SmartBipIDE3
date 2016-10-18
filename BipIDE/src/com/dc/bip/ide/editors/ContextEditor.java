package com.dc.bip.ide.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

public class ContextEditor extends MultiPageEditorPart {

	private String projectName;

	@Override
	protected void createPages() {
		IFile contextFile = ((FileEditorInput) getEditorInput()).getFile();
		projectName = contextFile.getProject().getName();
		String contextFileName = contextFile.getName();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;

	}

}
