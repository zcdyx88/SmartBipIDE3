package com.dc.bip.ide.editors.expression;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
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
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.wst.xml.ui.internal.Logger;

import com.dc.bip.ide.common.model.ExpressionConstants;
import com.dc.bip.ide.common.model.Params;
import com.dc.bip.ide.objects.ExpressionService;
import com.dc.bip.ide.objects.PramsInfo;
import com.dc.bip.ide.wizards.expression.ExpressionAddParamsWizard;

public class ExpressionInfoEditor extends EditorPart implements IEditorPart {

	public static final String ID = "ExpressionInfoEditorID"; //$NON-NLS-1$

	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
//	private ExpressionService expressionService = new ExpressionService();
	private Text text_id;
	private Table table;
	private Text text_name;
	private IFile file;
	private Text text_descri;
	private Text text_imps;
	private Params params;

	public ExpressionInfoEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		Section section = formToolkit.createSection(container, Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		section.setBounds(42, 10, 556, 204);
		formToolkit.paintBordersFor(section);
		section.setText("表达式基本信息");

		Composite composite_1 = new Composite(section, SWT.NONE);
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);
		section.setClient(composite_1);

		Label lblid = new Label(composite_1, SWT.NONE);
		lblid.setBounds(62, 27, 74, 17);
		formToolkit.adapt(lblid, true, true);
		lblid.setText("表达式ID：");

		text_id = new Text(composite_1, SWT.BORDER);
		text_id.setBounds(174, 21, 190, 23);
		text_id.setEditable(false);
		formToolkit.adapt(text_id, true, true);
		text_id.setText(params.get(ExpressionConstants.ID) == null ? "" : params.get(ExpressionConstants.ID));

		Label label_3 = new Label(composite_1, SWT.NONE);
		label_3.setBounds(62, 68, 74, 17);
		formToolkit.adapt(label_3, true, true);
		label_3.setText("表达式名称：");

		text_name = new Text(composite_1, SWT.BORDER);
		text_name.setBounds(174, 62, 190, 23);
		text_name.setEditable(false);
		formToolkit.adapt(text_name, true, true);
		text_name.setText(params.get(ExpressionConstants.NAME) == null ? "" : params.get(ExpressionConstants.NAME));

		Label label = new Label(composite_1, SWT.NONE);
		label.setBounds(61, 102, 61, 17);
		formToolkit.adapt(label, true, true);
		label.setText("实现类：");

		text_imps = new Text(composite_1, SWT.BORDER);
		text_imps.setBounds(174, 96, 360, 23);
		formToolkit.adapt(text_imps, true, true);
		text_imps.setText(params.get(ExpressionConstants.IMPLS) == null ? "" : params.get(ExpressionConstants.IMPLS));

		Label label_1 = new Label(composite_1, SWT.NONE);
		label_1.setBounds(62, 139, 61, 17);
		formToolkit.adapt(label_1, true, true);
		label_1.setText("描述：");

		text_descri = new Text(composite_1, SWT.BORDER);
		text_descri.setBounds(174, 133, 360, 23);
		formToolkit.adapt(text_descri, true, true);
		text_descri.setText(params.get(ExpressionConstants.DESCRIBE) == null ? "" : params.get(ExpressionConstants.DESCRIBE));

		Section sctnNewSection = formToolkit.createSection(container, Section.TWISTIE | Section.TITLE_BAR);
		sctnNewSection.setBounds(42, 232, 556, 142);
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText("表达式参数信息：");
		sctnNewSection.setExpanded(true);

		Composite composite = new Composite(sctnNewSection, SWT.NONE);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		sctnNewSection.setClient(composite);

		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(76, 10, 292, 96);
		formToolkit.adapt(table);
		formToolkit.paintBordersFor(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn reversalCheckField = new TableColumn(table, SWT.NONE);
		reversalCheckField.setWidth(142);
		reversalCheckField.setText("参数名称");

		TableColumn reversalCheckValue = new TableColumn(table, SWT.NONE);
		reversalCheckValue.setWidth(140);
		reversalCheckValue.setText("参数类型");
		Params expressionInputs = params.getChild("expressionInput");
		if(null != expressionInputs){
			
			for (Map.Entry<String, String> entry : expressionInputs.getParams().entrySet()) {
				TableItem tableItem = new TableItem(table, SWT.NONE);
				tableItem.setText(0, entry.getKey());
				tableItem.setText(1, entry.getValue());
			}
		}
		Button button_add = new Button(composite, SWT.NONE);
		button_add.setBounds(407, 10, 80, 27);
		formToolkit.adapt(button_add, true, true);
		button_add.setText("添加");
		button_add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		button_add.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseUp(MouseEvent e) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				ExpressionAddParamsWizard bsw = new ExpressionAddParamsWizard(table, params);
				WizardDialog dialog = new WizardDialog(shell, bsw);
				dialog.setTitle("增加表达式参数");
				dialog.open();
				// firePropertyChange(257);
			}
		});

		Button button_delete = new Button(composite, SWT.NONE);
		button_delete.setBounds(407, 63, 80, 27);
		formToolkit.adapt(button_delete, true, true);
		button_delete.setText("删除");

		button_delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(e.widget.getData());
			}
		});
		button_delete.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseUp(MouseEvent e) {
				int index = table.getSelectionIndex();
				if (index > 0) {
					String key = table.getItem(index).getText(0);
					table.remove(index);
					params.getChild("expressionInput").remove(key);
				}
			}
		});

		Button button_save = formToolkit.createButton(container, "保存", SWT.NONE);
		button_save.setBounds(289, 434, 80, 27);

	}

	@Override
	public void setFocus() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.setSite(site);
		super.setInput(input);
		FileInputStream freader = null;
		ObjectInputStream objectInputStream = null;
		try {
			FileEditorInput i = (FileEditorInput) input;
			IFile resource = i.getFile();
			params = new Params(resource.getLocation().toOSString());
			params.unPersist();
			setPartName(params.get(ExpressionConstants.ID));
		} catch (Exception e) {
			Logger.logException("exception initializing " + getClass().getName(), e);
		} finally {
			if (objectInputStream != null) {
				try {
					objectInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (freader != null) {
				try {
					freader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}
}
