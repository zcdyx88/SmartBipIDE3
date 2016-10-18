package com.dc.bip.ide.editors.busisvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.eclipse.core.commands.util.Tracing;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.misc.Policy;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageSelectionProvider;
import org.eclipse.wst.xml.ui.internal.Logger;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;

import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.views.objects.BSOperationNode;

public class BusiSvcContentEditor extends MultiPageEditorPart {

	public static final String ID = "com.dc.bip.ide.editors.BusiSvcContentEditor"; //$NON-NLS-1$
	
	private static final String TRACING_COMPONENT = "MPE"; 
	
	private ListenerList pageChangeListeners = new ListenerList(
			ListenerList.IDENTITY);

	private BusiSvcInfo busiSvcInfo;
	private BSOperationNode bsOperationNode;
	private BuiSvcInfoEditor busiSvcInfoEditor;
	private IEditorInput oldEditorInput;

	public BusiSvcContentEditor() {
	}

	protected void createPages() {
		try {
			// WsdlInfoPage wsdlInfo = new WsdlInfoPage(getContainer(),
			// SWT.NONE, busiSvcInfo);

			busiSvcInfoEditor = new BuiSvcInfoEditor(bsOperationNode);
			int index3 = addPage(busiSvcInfoEditor, this.getEditorInput());
			setPageText(index3, "基础信息");

//			super.createPages();
//			super.removePage(2);
//			setPageText(1, " SDA ");

			SoapTestPage soapTestPage = new SoapTestPage(getContainer(), SWT.NONE, busiSvcInfo);
			int index2 = addPage(soapTestPage);
			setPageText(index2, "报文测试");
			
			LocalSoapPage localSoapPage = new LocalSoapPage(getContainer(), SWT.NONE, busiSvcInfo);
			int index = addPage(localSoapPage);
			setPageText(index,"本地SOAP服务");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		try {
			super.init(site, input);
			FileEditorInput i = (FileEditorInput) input;
			IFile resource = i.getFile();
			File file = resource.getLocation().toFile();
			String projectName = resource.getProject().getName();
			FileInputStream in = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(in);
			bsOperationNode = (BSOperationNode) objectInputStream.readObject();
			bsOperationNode.setProjectName(projectName);
			busiSvcInfo = bsOperationNode.getBusiSvcInfo();
			setPartName(busiSvcInfo.getName());
		} catch (Exception e) {
			Logger.logException("exception initializing " + getClass().getName(), e);
		}		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
//		this.getActiveEditor().doSave(monitor);
		busiSvcInfoEditor.doSave(monitor);
	}

	@Override
	public void doSaveAs() {
		// this.getActiveEditor().doSaveAs();
		busiSvcInfoEditor.doSaveAs();
	}

	@Override
	public boolean isSaveAsAllowed() {
		// return this.getActiveEditor().isSaveAsAllowed();
		return busiSvcInfoEditor != null && busiSvcInfoEditor.isSaveAsAllowed();

	}

	public BSOperationNode getBsOperationNode() {
		return bsOperationNode;
	}

	public void setBsOperationNode(BSOperationNode bsOperationNode) {
		this.bsOperationNode = bsOperationNode;
	}
}
