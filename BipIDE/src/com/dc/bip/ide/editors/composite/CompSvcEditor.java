package com.dc.bip.ide.editors.composite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.wst.xml.ui.internal.Logger;

import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.util.HelpUtil;

public class CompSvcEditor extends MultiPageEditorPart {

	public static final String ID = "wizard.editor.CompSvcEditor"; //$NON-NLS-1$

	private CompositeService compSvc;
	private boolean dirty = false;
	private CompSvcBaseInfoPage compSvcBaseInfoPage;
	private ContextPage contextPage;
	
	public CompSvcEditor() {
	}
	
	public void setDirty(boolean isDirty){
		dirty = isDirty;
		firePropertyChange(PROP_DIRTY);
	}
	
	@Override
	public boolean isDirty() {
		return dirty;
	}

	protected void createPages() {
		// 新建也签 0
		compSvcBaseInfoPage = new CompSvcBaseInfoPage(getContainer(), 0, compSvc, this);
		int index = addPage(compSvcBaseInfoPage);
		setPageText(index, "基础信息");		
		
		// 上下文参数 1
		contextPage = new ContextPage(getContainer(), SWT.NONE, compSvc);
		index = addPage(contextPage);
		setPageText(index, "数据映射");
		setDirty(false);
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		try {
			super.init(site, input);
			FileEditorInput i = (FileEditorInput) input;
			IFile resource = i.getFile();
			compSvc = HelpUtil.readObject(resource);
			this.setPartName(compSvc.getBaseinfo().getServiceId());
		} catch (Exception e) {
			Logger.logException("exception initializing " + getClass().getName(), e);
		}
	}
	
	/**
	 * 保存时保存两个TAB  
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		if(null != compSvcBaseInfoPage){
			compSvcBaseInfoPage.saveComposite();
		}
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		this.getActiveEditor().doSaveAs();
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		 super.setFocus();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		 super.dispose();
	}


}
