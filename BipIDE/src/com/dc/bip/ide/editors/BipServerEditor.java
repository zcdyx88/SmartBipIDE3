package com.dc.bip.ide.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.dc.bip.ide.editors.bipserver.BipServerInfoPage;
import com.dc.bip.ide.objects.BipServer;
import com.dc.bip.ide.util.HelpUtil;

public class BipServerEditor extends MultiPageEditorPart {
	
	private BipServer bipServer;
	private String projectName;

	@Override
	protected void createPages() {
	    BipServerInfoPage bipServerInfoPage = new BipServerInfoPage(getContainer(), 0, bipServer,projectName);
		int index = addPage(bipServerInfoPage);
	    setPageText(index, "基本信息");
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
//		this.getActiveEditor().doSave(monitor);
		
	}

	@Override
	public void doSaveAs() {
//		this.getActiveEditor().doSaveAs();
		
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
	    IFile baseFile= ((FileEditorInput)getEditorInput()).getFile();
	    projectName = baseFile.getProject().getName();
	    bipServer = HelpUtil.readBipServerObject(baseFile.getLocation().toFile());
	    this.setPartName(bipServer.getId());
	}

	

}
