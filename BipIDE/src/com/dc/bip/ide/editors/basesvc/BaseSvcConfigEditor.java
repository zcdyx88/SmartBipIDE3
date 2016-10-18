package com.dc.bip.ide.editors.basesvc;

import java.io.InputStream;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.views.objects.BSNode;
import com.dc.bip.ide.views.objects.TreeNode;
import com.dc.bip.ide.wizards.PublishServiceWizard;

public class BaseSvcConfigEditor extends EditorPart implements IEditorPart {
	private Text text_id;
	private Text text_nickName;
	private Text text_desc;
	private Text text_moudle;
	private Text text_implement;
	private BaseService bsNode;
	private boolean dirty = false;
	private IFile file;
	private FormToolkit toolkit;

	private void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public BaseSvcConfigEditor(BaseService bs) {
		this.bsNode = bs;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		String vm = "base.vm";
		try {
			IFolder folder = (IFolder) file.getParent();
			if (!folder.exists())
				HelpUtil.mkdirs(folder, true, true, monitor);
			HelpUtil.change2Writeable(folder);
			InputStream stream = HelpUtil.openContentStream(bsNode, vm);
			if (file.exists()) {
				if (file.isReadOnly())
					file.setReadOnly(false);
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
			setDirty(false);
			firePropertyChange(PROP_DIRTY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
		MessageDialog.openInformation(Display.getCurrent().getActiveShell(), null, "doSaveAs");
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.setSite(site);
		super.setInput(input);
		file = ((FileEditorInput) input).getFile();
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void createPartControl(Composite parent) {
		// Composite composite = new Composite(parent, SWT.NONE);
		// composite.setLayout(null);
		/// 页面调整
		toolkit = new FormToolkit(parent.getDisplay());
		parent.setLayout(new FillLayout());
		// Section section = new Section(parent, ExpandableComposite.EXPANDED |
		// ExpandableComposite.TITLE_BAR);
		Section section = toolkit.createSection(parent, Section.TITLE_BAR);
		section.setText("基础服务信息维护");
		Composite composite = toolkit.createComposite(section, SWT.NONE);
		toolkit.paintBordersFor(composite);
		section.setClient(composite);

		Label lblid = new Label(composite, SWT.NONE);
		lblid.setBounds(21, 23, 25, 17);
		lblid.setText("ID\uFF1A");
		toolkit.adapt(lblid, true, true);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(10, 60, 36, 17);
		lblNewLabel.setText("\u540D\u79F0\uFF1A");
		toolkit.adapt(lblNewLabel, true, true);

		Label label = new Label(composite, SWT.NONE);
		// label.setBounds(10, 209, 36, 17);
		label.setBounds(10, 156, 36, 17);
		label.setText("\u63CF\u8FF0\uFF1A");
		toolkit.adapt(label, true, true);

		// Label label_1 = new Label(composite, SWT.NONE);
		// label_1.setBounds(10, 156, 36, 17);
		// label_1.setText("\u6A21\u5757\uFF1A");
		// toolkit.adapt(label_1, true, true);
		try {
			text_id = new Text(composite, SWT.BORDER);

			text_id.setEditable(false);
			text_id.setBounds(61, 19, 510, 24);
			text_id.setText(bsNode.getName());
			toolkit.adapt(text_id, true, true);

			text_nickName = new Text(composite, SWT.BORDER);
			text_nickName.setBounds(61, 57, 510, 24);
			text_nickName.setText(bsNode.getNickName());
			toolkit.adapt(text_nickName, true, true);
			text_nickName.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
					firePropertyChange(PROP_DIRTY);
				}

			});

			text_desc = new Text(composite, SWT.BORDER);
			// text_desc.setBounds(61, 206, 510, 103);
			text_desc.setBounds(61, 153, 510, 103);
			text_desc.setText(bsNode.getDescription());
			toolkit.adapt(text_desc, true, true);
			text_desc.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
					firePropertyChange(PROP_DIRTY);
				}

			});

			// text_moudle = new Text(composite, SWT.BORDER);
			// text_moudle.setEditable(false);
			// text_moudle.setBounds(61, 153, 510, 24);
			// text_moudle.setText(bsNode.getContributionId());
			// toolkit.adapt(text_moudle, true, true);

			text_implement = new Text(composite, SWT.BORDER);
			text_implement.setBounds(61, 102, 510, 24);
			text_implement.setText(bsNode.getImpls());
			toolkit.adapt(text_implement, true, true);
			Label lblNewLabel_1 = new Label(composite, SWT.NONE);
			lblNewLabel_1.setBounds(10, 105, 46, 17);
			lblNewLabel_1.setText("\u5B9E\u73B0\u7C7B\uFF1A");
			toolkit.adapt(lblNewLabel_1, true, true);

			// 发布按钮
			Button btnNewButton = new Button(composite, SWT.NONE);
			btnNewButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
				}
			});
			btnNewButton.setBounds(491, 352, 80, 27);
			btnNewButton.addMouseListener(new MouseListener() {
				@Override
				public void mouseDoubleClick(MouseEvent e) {

				}

				@Override
				public void mouseDown(MouseEvent e) {
				}

				@Override
				public void mouseUp(MouseEvent e) {
					String codeFilePath = (new StringBuilder("/")).append(file.getProject().getName()).append("/src/")
							.append(bsNode.getImpls().replaceAll("\\.", "/")).append(".java").toString();
					IFile codeFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(codeFilePath));
					bsNode.setCodeFile(codeFile);
					BSNode bNode = new BSNode("BS");
					bNode.setBaseService(bsNode);
					TreeNode treeNode = (TreeNode)bNode;
					treeNode.setProjectName(file.getProject().getName());
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					PublishServiceWizard pbsw = new PublishServiceWizard(treeNode);
					WizardDialog dialog = new WizardDialog(shell, pbsw);
					dialog.setTitle("发布服务");
					dialog.open();
				}

			});
			toolkit.adapt(btnNewButton, true, true);
			btnNewButton.setText("\u53D1\u5E03");
			
			
			text_implement.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
					firePropertyChange(PROP_DIRTY);
				}

			});
			initDataBindings();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setFocus() {

	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		IObservableValue uiElement;
		IObservableValue modelElement;

		// Lets bind it
		uiElement = SWTObservables.observeText(text_nickName, SWT.Modify);
		modelElement = BeansObservables.observeValue(bsNode, "nickName");
		bindingContext.bindValue(uiElement, modelElement, null, null);

		uiElement = SWTObservables.observeText(text_implement, SWT.Modify);
		modelElement = BeansObservables.observeValue(bsNode, "impls");
		bindingContext.bindValue(uiElement, modelElement, null, null);

		uiElement = SWTObservables.observeText(text_desc, SWT.Modify);
		modelElement = BeansObservables.observeValue(bsNode, "description");
		bindingContext.bindValue(uiElement, modelElement, null, null);
		setDirty(false);
		firePropertyChange(PROP_DIRTY);
		return bindingContext;
	}
}
