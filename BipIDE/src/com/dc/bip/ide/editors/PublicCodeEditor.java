package com.dc.bip.ide.editors;

import java.util.List;

import javax.swing.JFrame;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;

import com.dc.bip.ide.context.PublicCode;
import com.dc.bip.ide.editors.bipserver.BipServerInfoPage;
import com.dc.bip.ide.editors.busi.SDAEditor;
import com.dc.bip.ide.editors.composite.CompSvcBaseInfoPage;
import com.dc.bip.ide.editors.publiccode.PublicCodePage;
import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.objects.BipServer;
import com.dc.bip.ide.util.HelpUtil;

public class PublicCodeEditor extends MultiPageEditorPart {
	
	private String projectName;

	@Override
	protected void createPages() {
	    IFile codeFile= ((FileEditorInput)getEditorInput()).getFile();
	    projectName = codeFile.getProject().getName();
	    List<PublicCode> publicCodes = HelpUtil.readPublicCodeObject(codeFile);
	    String codeFileName = codeFile.getName();
	    PublicCodePage publicCodePage = new PublicCodePage(getContainer(), 0, publicCodes,projectName,codeFileName);
		int index = addPage(publicCodePage);
	    setPageText(index, "公共代码");
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

	

}
