package com.dc.bip.ide.editors.basesvc;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.util.HelpUtil;

public class BaseSvcEditor extends MultiPageEditorPart {

	@SuppressWarnings("restriction")
	private CompilationUnitEditor codeEditor = null;
	private BaseSvcConfigEditor configEditor = null;
	private BaseService bs;
	private String projectName;

	@Override
	protected void createPages() {


		createConfigEditorPage();
		createCodeEditorPage();

	}

	@SuppressWarnings("restriction")
	private void createCodeEditorPage() {
		try {
			codeEditor = new CompilationUnitEditor();
			String codeFilePath = (new StringBuilder("/")).append(projectName).append("/src/")
					.append(bs.getImpls().replaceAll("\\.", "/")).append(".java").toString();
			IFile codeFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(codeFilePath));

			IEditorInput codeFileInput = new FileEditorInput(codeFile);

			int index = addPage(codeEditor, codeFileInput);
			setPageText(index, "源码");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void createConfigEditorPage() {
		try {
			configEditor = new BaseSvcConfigEditor(bs);
			int index = addPage(configEditor, getEditorInput());
			setPageText(index, "配置");
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		this.getActiveEditor().doSave(monitor);

	}

	@Override
	public void doSaveAs() {
		this.getActiveEditor().doSaveAs();

	}

	@Override
	public boolean isSaveAsAllowed() {
		return this.getActiveEditor().isSaveAsAllowed();

	}
	
	public void init(IEditorSite site, IEditorInput input)			
			throws PartInitException {
		super.init(site, input);
		IFile baseFile = ((FileEditorInput) getEditorInput()).getFile();
		projectName = baseFile.getProject().getName();
		baseFile.getProject().getName();
		try {
			bs = HelpUtil.parseBaseSvcXml(baseFile.getContents());
		} catch (CoreException e) {
			e.printStackTrace();
		}
		setPartName(bs.getName());
	}


}
