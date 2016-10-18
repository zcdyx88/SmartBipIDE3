package com.dc.bip.ide.editors.bipserver;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dc.bip.ide.objects.BipServer;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.PlatformUtils;

public class BipServerInfoPage extends Composite {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtNewText;
	private Text txtNewText_1;
	private Text txtNewText_2;
	private String projectName;
	private Display display;
	private Text txtNewText_3;
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public BipServerInfoPage(final Composite parent, int style, BipServer bipServer,final String projectName) {
		
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		this.projectName = projectName;

		ScrolledForm scrldfrmNewScrolledform = formToolkit.createScrolledForm(this);
		scrldfrmNewScrolledform.setAlwaysShowScrollBars(true);
		scrldfrmNewScrolledform.setExpandVertical(true);
		scrldfrmNewScrolledform.setExpandHorizontal(true);
		scrldfrmNewScrolledform.setMinHeight(400);
		scrldfrmNewScrolledform.setMinWidth(400);
		formToolkit.paintBordersFor(scrldfrmNewScrolledform);
		scrldfrmNewScrolledform.setText("BIP\u670D\u52A1\u7AEF");

		Section sctnNewSection = formToolkit.createSection(scrldfrmNewScrolledform.getBody(), Section.TITLE_BAR);
		sctnNewSection.setBounds(10, 10, 473, 201);
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText("\u57FA\u672C\u4FE1\u606F");

		Composite composite_1 = formToolkit.createComposite(sctnNewSection, SWT.NONE);
		formToolkit.paintBordersFor(composite_1);
		sctnNewSection.setClient(composite_1);

		Label lblNewLabel = formToolkit.createLabel(composite_1, "ID:", SWT.NONE);
		lblNewLabel.setForeground(SWTResourceManager.getColor(65, 105, 225));
		lblNewLabel.setBounds(30, 17, 61, 17);
		txtNewText = formToolkit.createText(composite_1, "New Text", SWT.NONE);
		txtNewText.setBounds(114, 13, 211, 23);
		txtNewText.setText(bipServer.getId());
		txtNewText.setEditable(false);
		txtNewText.setEnabled(false);
		Label lblNewLabel_1 = formToolkit.createLabel(composite_1, "IP:", SWT.NONE);
		lblNewLabel_1.setForeground(SWTResourceManager.getColor(65, 105, 225));
		lblNewLabel_1.setBounds(30, 57, 61, 17);

		txtNewText_1 = formToolkit.createText(composite_1, "New Text", SWT.NONE);
		txtNewText_1.setBounds(114, 53, 211, 23);
		txtNewText_1.setText(bipServer.getIp());
		Label lblNewLabel_2 = formToolkit.createLabel(composite_1, "JmxPort:", SWT.NONE);
		lblNewLabel_2.setForeground(SWTResourceManager.getColor(65, 105, 225));
		lblNewLabel_2.setBounds(30, 97, 61, 17);

		txtNewText_2 = formToolkit.createText(composite_1, "New Text", SWT.NONE);
		txtNewText_2.setBounds(114, 93, 211, 23);
		txtNewText_2.setText(bipServer.getJmxport());
		Label lblNewLabel_3 = formToolkit.createLabel(composite_1, "RegPort:", SWT.NONE);
		lblNewLabel_3.setForeground(SWTResourceManager.getColor(65, 105, 225));
		lblNewLabel_3.setBounds(30, 137, 61, 17);
		
		txtNewText_3 = formToolkit.createText(composite_1, "New Text", SWT.NONE);
		txtNewText_3.setBounds(114, 133, 211, 23);
		txtNewText_3.setText(bipServer.getRegport());

		Button btnNewButton = formToolkit.createButton(scrldfrmNewScrolledform.getBody(), "\u4FDD\u5B58", SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String id = txtNewText.getText();
				String ip = txtNewText_1.getText();
				String jmxport = txtNewText_2.getText();
				String regport = txtNewText_3.getText();
				BipServer bipServer = new BipServer();
				bipServer.setId(id);
				bipServer.setIp(ip);
				bipServer.setJmxport(jmxport);
				bipServer.setRegport(regport);
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				String bipServerFilePath = (new StringBuilder("/")).append(projectName).append(BipConstantUtil.ServerPath)
						.append(id).append(".bipserver").toString();
				IFile bipServerFile = root.getFile(new Path(bipServerFilePath));
				HelpUtil.writeObject(bipServer, bipServerFile);
				PlatformUtils.showConfirm("保存服务器信息", "是否保存");
				
//				TreeView view = (TreeView) page.findView("wizard.view1");
//				if (null != view) {
//					view.reload();
//				}
//				IEditorPart[] editors = page.getEditors();
//				for (IEditorPart editor : editors) {
//					if("Bip服务端编辑器".equals(editor.getTitle())){
//						page.closeEditor(editor, true);
//					}
//				}
				
				
			}
		});
		btnNewButton.setBounds(300, 300, 80, 27);

		scrldfrmNewScrolledform.reflow(true);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public boolean setFocus() {
		// TODO Auto-generated method stub
		return true;
	}
}
