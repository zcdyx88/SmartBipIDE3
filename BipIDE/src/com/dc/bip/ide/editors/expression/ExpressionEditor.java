package com.dc.bip.ide.editors.expression;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import org.eclipse.wst.xml.ui.internal.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.dc.bip.ide.common.model.ExpressionConstants;
import com.dc.bip.ide.common.model.Params;
import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.objects.ExpressionService;
import com.dc.bip.ide.views.objects.ExpressionNode;

public class ExpressionEditor extends MultiPageEditorPart {

	@SuppressWarnings("restriction")
	private CompilationUnitEditor codeEditor = null;
	private ExpressionInfoEditor infoEditor = null;
	private String projectName;
	private Params params;

	@Override
	protected void createPages() {


		createConfigEditorPage();
		createCodeEditorPage();

	}

	@SuppressWarnings("restriction")
	private void createCodeEditorPage() {
		try {
			codeEditor = new CompilationUnitEditor();
			String codeFilePath = (new StringBuilder("/")).append(params.get("projectName")).append("/src/")
					.append(params.get(ExpressionConstants.IMPLS).replaceAll("\\.", "/")).append(".java").toString();
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
			infoEditor = new ExpressionInfoEditor();
			int index = addPage(infoEditor, getEditorInput());
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
		super.setSite(site);
		super.setInput(input);
		FileInputStream freader = null;
		ObjectInputStream objectInputStream = null;
		try {
			FileEditorInput i = (FileEditorInput) input;
			IFile resource = i.getFile();
			params = new Params(resource.getLocation().toFile().getAbsolutePath());
			params.unPersist();
			this.setPartName(params.get("nodeName"));
			projectName = params.get("projectName");
			System.out.println("projectName:"+projectName);
		} catch (Exception e) {
			Logger.logException("exception initializing " + getClass().getName(), e);
		}finally{
			if(objectInputStream!=null){
				try {
					objectInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(freader !=null){
				try {
					freader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


}
