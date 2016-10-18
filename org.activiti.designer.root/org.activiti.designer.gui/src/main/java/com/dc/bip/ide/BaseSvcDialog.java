package com.dc.bip.ide;

import java.util.List;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.designer.util.eclipse.ActivitiUiUtil;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.service.BaseSvcService;

public class BaseSvcDialog extends Dialog {
	private Text idText;
	private Text nameText;
	private Text serviceIdText;

	private FlowElement esp;
	private Shell shell_1;
	private Table table;

	private Diagram diagram;

	public BaseSvcDialog(Diagram diagram, Shell parentShell, Object object) {
		super(parentShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.esp = (FlowElement) object;
		this.diagram = diagram;
		setText("基础服务编辑");
	}

	/**
	 * Opens the dialog and returns the input
	 * 
	 * @return String
	 */
	public String open() {
		// Create the dialog window
		shell_1 = new Shell(getParent(), getStyle());
		shell_1.setText(getText());
		shell_1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		shell_1.setSize(612, 399);
		Point location = getParent().getShell().getLocation();
		Point size = getParent().getShell().getSize();
		shell_1.setLocation((location.x + size.x - 500) / 2, (location.y + size.y - 300) / 2);
		createContents(shell_1);

		Button btnSearch = new Button(shell_1, SWT.NONE);
		btnSearch.setBounds(500, 10, 100, 22);
		btnSearch.setText("搜索");
		btnSearch.addListener(SWT.MouseDown, new org.eclipse.swt.widgets.Listener() {
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(serviceIdText.getText())) {
					fillTableRows(table);
				} else {
					refreshTableContent(table, serviceIdText.getText());
				}
			}

		});

		table = new Table(shell_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(5, 80, 596, 224);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnServiceId = new TableColumn(table, SWT.CENTER);
		tblclmnServiceId.setWidth(100);
		tblclmnServiceId.setText("服务ID");

		TableColumn tblclmnServiceName = new TableColumn(table, SWT.CENTER);
		tblclmnServiceName.setWidth(100);
		tblclmnServiceName.setText("服务名称");

		TableColumn tblclmnServiceDesc = new TableColumn(table, SWT.CENTER);
		tblclmnServiceDesc.setWidth(377);
		tblclmnServiceDesc.setText("服务描述");

		// 填充表格
		fillTableRows(table);
		// 双击事件
		table.addListener(SWT.MouseDoubleClick, new org.eclipse.swt.widgets.Listener() {
			@Override
			public void handleEvent(Event event) {
				TableItem item = table.getItem(table.getSelectionIndex());
				serviceIdText.setText(item.getText(0));
				nameText.setText(item.getText(0));
			}

		});

		Button ok = new Button(shell_1, SWT.PUSH);
		ok.setBounds(5, 347, 100, 22);
		ok.setText("确定");
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
//				if (StringUtils.isEmpty(idText.getText())) {
//					MessageDialog.openError(shell_1, "Validation error", "id must be filled.");
//					return;
//				}
				if (StringUtils.isEmpty(nameText.getText())) {
					MessageDialog.openError(shell_1, "Validation error", "name must be filled.");
					return;
				}
				if (StringUtils.isEmpty(serviceIdText.getText())) {
					MessageDialog.openError(shell_1, "Validation error", "serviceIdText expression must be filled.");
					return;
				}
//				esp.setId(idText.getText());
				esp.setName(nameText.getText());
				FlowElementUtil.saveAttr(esp, "serviceId", serviceIdText.getText());
				// esp.addAttribute(attribute);
				shell_1.close();
			}
		});

		// Set the OK button as the default, so
		// user can type input and press Enter
		// to dismiss
		shell_1.setDefaultButton(ok);

		// Create the cancel button and add a handler
		// so that pressing it will set input to null
		Button cancel = new Button(shell_1, SWT.PUSH);
		cancel.setBounds(120, 347, 100, 22);
		cancel.setText("取消");
		cancel.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				shell_1.close();
			}
		});
		shell_1.open();
		Display display = getParent().getDisplay();
		while (!shell_1.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return null;
	}

	/**
	 * Creates the dialog's contents
	 * 
	 * @param shell
	 *            the dialog window
	 */
	private void createContents(final Shell shell) {
		shell_1.setLayout(null);

		String serviceIdExp = FlowElementUtil.getAttrValue(esp, "serviceId");

		Label serviceIdLabel = new Label(shell, SWT.NONE);
		serviceIdLabel.setBounds(5, 10, 50, 18);
		serviceIdLabel.setAlignment(SWT.LEFT);
		serviceIdLabel.setText("服务ID");
		serviceIdLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		serviceIdText = new Text(shell, SWT.BORDER);
		serviceIdText.setBounds(75, 10, 400, 18);
		if (StringUtils.isNotEmpty(serviceIdExp)) {
			serviceIdText.setText(serviceIdExp);
		}

		Label nameLabel = new Label(shell, SWT.NONE);
		nameLabel.setBounds(5, 40, 50, 18);
		nameLabel.setAlignment(SWT.LEFT);
		nameLabel.setText("服务名称");
		nameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		nameText = new Text(shell, SWT.BORDER);
		nameText.setBounds(75, 40, 400, 18);
		if (StringUtils.isNotEmpty(esp.getName())) {
			nameText.setText(serviceIdExp);
		}
	}

	public void fillTableRows(Table table) {
		if (diagram != null) {
			final IProject project = ActivitiUiUtil.getProjectFromDiagram(diagram);
			List<BaseService> baseServices = BaseSvcService.getBaseSVCList(project);
			for (BaseService bsi : baseServices) {
				TableItem item = new TableItem(table, SWT.NULL);
				item.setText(0, bsi.getName());
				item.setText(1, bsi.getName());
				item.setText(2, bsi.getDescription());

			}
		}

	}

	public void refreshTableContent(Table table, String key) {
		if (diagram != null) {
			table.removeAll();
			final IProject project = ActivitiUiUtil.getProjectFromDiagram(diagram);
			List<BaseService> baseServices = BaseSvcService.getBaseSVCList(project);
			for (BaseService bsi : baseServices) {
				if (bsi.getName().contains(key) || bsi.getName().contains(key) || bsi.getDescription().contains(key)) {
					TableItem item = new TableItem(table, SWT.NULL);
					item.setText(0, bsi.getName());
					item.setText(1, bsi.getName());
					item.setText(2, bsi.getDescription());
				}
			}
		}

	}
}
