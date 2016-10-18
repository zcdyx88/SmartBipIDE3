package com.dc.bip.ide.wizards.bindflow;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Section;

import com.dc.bip.ide.views.objects.FlowNode;

import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ComboViewer;

public class BindFlowWizardPage extends WizardPage {
	
	private List<String> flowList = new ArrayList<String>();
	private ComboViewer comboViewer ;
	private String flowName;
	/**
	 * Create the wizard.
	 */
	public BindFlowWizardPage(String projectName) {
		super("wizardPage");
		setTitle("绑定流程");
		setDescription("组合服务需要绑定一个服务流程，服务按照编码流程执行");
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		String flowPath = project.getFolder("/dev/services/flow/").getLocation().toString();
 		File flowFileFolder = new File(flowPath);
 		FilenameFilter flowFileNameFilter = new FilenameFilter() {
 			@Override
 			public boolean accept(File dir, String name) {
 				if (name.lastIndexOf('.') > 0) {
 					int lastIndex = name.lastIndexOf('.');
 					String str = name.substring(lastIndex);
 					if (str.equals(".bpmn")) {
 						return true;
 					}
 				}
 				return false;
 			}
 		};
		File[] flowFiles = flowFileFolder.listFiles(flowFileNameFilter);
		if (flowFiles != null && flowFiles.length > 0) {
			for (int i = 0; i < flowFiles.length; i++) {
				File file = flowFiles[i];
				flowList.add(file.getName());
			}
		}
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(0, 0, 470, 270);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		
		Label lblId = new Label(composite, SWT.NONE);
		lblId.setText("\u9009\u62E9\u6D41\u7A0B:");
		lblId.setBounds(43, 80, 58, 25);
		
		comboViewer = new ComboViewer(composite, SWT.NONE);
		Combo combo = comboViewer.getCombo();
		combo.setBounds(133, 80, 283, 25);
		for(String flowName : flowList){
			combo.add(flowName);
		}
		combo.addSelectionListener(new SelectionListener(){

			public void widgetSelected(SelectionEvent e) {
				Combo tmpCombo = comboViewer.getCombo();
				String text = tmpCombo.getText();
				flowName = text;
//				JOptionPane.showMessageDialog(null, text);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	

}
