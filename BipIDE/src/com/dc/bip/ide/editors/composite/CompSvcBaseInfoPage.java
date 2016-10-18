package com.dc.bip.ide.editors.composite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dc.bip.ide.common.ProtocolConstants;
import com.dc.bip.ide.common.globalconfig.GlobalConstants;
import com.dc.bip.ide.common.model.ConfigFile;
import com.dc.bip.ide.objects.CompServiceBaseInfo;
import com.dc.bip.ide.objects.CompServiceSDAInfo;
import com.dc.bip.ide.objects.CompServiceWsdlInfo;
import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.objects.ProtocolInService;
import com.dc.bip.ide.repository.impl.ProtocolServiceRepository;
import com.dc.bip.ide.service.ProtocolService;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.PlatformUtils;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.CompositeNode;
import com.dc.bip.ide.wizards.PublishServiceWizard;
import com.dc.bip.ide.wizards.bindflow.BindFlowWizard;
import com.dc.bip.ide.wizards.busi.CompConfigAddWizard;

public class CompSvcBaseInfoPage extends Composite {
	private Combo combo_protocolType;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text serviceIdText;
	private Text serviceNameText;
	private Text serviceDescText;
	private Text txt_protocolURL;
	private Text txt_protocolNsp;
	private Text txt_protocolBinding;
	private Text txt_protocolEndPoint;
	private Text txt_protocolOption;
	private String projectName;
	private Table table_fileList;
	private Table table_flow;
	private Combo packUnPackModeCombo;// 数据适配
	private String flowNameSave;
	private CompSvcEditor editor;
	// 组合服务页面绑定的组合服务对象
	private CompositeService compositeService;
	// 协议Section
	private Section protocolInfoSection;
	// 协议信息section的composite
	private Composite protocolComposite;
	// 绑定协议Label
	private Label serviceProtocolLabel;
	// 协议的Combo
	private Combo protocolCombo;
	// 当前工程的协议操作服务
	private ProtocolService protocolService;
	// 协议类型
	private Text text_protocolType;
	// 协议通用
	private Text timeoutText;
	private Text maxThreadText;
	private Text minThreadText;
	private Text queueNumText;
	private Text queueSizeText;
	// TCP协议
	private Text ipText;
	private Text portText;
	private Text readPolicyText;
	private Text writePolicyText;
	// HTTP WS协议
	private Text urlText;
	private Text httpVersionText;
	// WS协议
	private Section wsSection;

	private Table configFileTab;

	public void setDirty() {
		editor.setDirty(true);
	}

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CompSvcBaseInfoPage(final Composite parent, int style, final CompositeService compservice,
			CompSvcEditor editor) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		this.compositeService = compservice;
		this.projectName = this.compositeService.getProject().getName();
		protocolService = ProtocolServiceRepository.getInstance().get(projectName);

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		ScrolledForm scrldfrm_compScrolledform = formToolkit.createScrolledForm(this);
		scrldfrm_compScrolledform.setAlwaysShowScrollBars(true);
		scrldfrm_compScrolledform.setExpandVertical(true);
		scrldfrm_compScrolledform.setExpandHorizontal(true);
		scrldfrm_compScrolledform.setMinHeight(400);
		scrldfrm_compScrolledform.setMinWidth(400);
		formToolkit.paintBordersFor(scrldfrm_compScrolledform);
		scrldfrm_compScrolledform.setText("\u7EC4\u5408\u670D\u52A1"); // 组合服务
		this.editor = editor;

		// 协议信息的section
		protocolInfoSection = formToolkit.createSection(scrldfrm_compScrolledform.getBody(),
				Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		protocolInfoSection.setBounds(530, 10, 500, 522);
		formToolkit.paintBordersFor(protocolInfoSection);
		protocolInfoSection.setText("协议信息");

		// 基本信息展示区
		Section sctn_serviceinfo = formToolkit.createSection(scrldfrm_compScrolledform.getBody(), Section.TITLE_BAR);
		sctn_serviceinfo.setBounds(10, 10, 500, 251);
		formToolkit.paintBordersFor(sctn_serviceinfo);
		sctn_serviceinfo.setText("\u57FA\u672C\u4FE1\u606F");
		Composite composite_serviceinfo = formToolkit.createComposite(sctn_serviceinfo, SWT.NONE);
		formToolkit.paintBordersFor(composite_serviceinfo);
		sctn_serviceinfo.setClient(composite_serviceinfo);

		Label serviceIdLabel = formToolkit.createLabel(composite_serviceinfo, "\u670D\u52A1ID", SWT.NONE);
		serviceIdLabel.setAlignment(SWT.RIGHT);
		serviceIdLabel.setForeground(SWTResourceManager.getColor(65, 105, 225));
		serviceIdLabel.setBounds(0, 27, 61, 17);
		serviceIdText = formToolkit.createText(composite_serviceinfo, "New Text", SWT.NONE);
		serviceIdText.setText(compservice.getBaseinfo().getServiceId());
		serviceIdText.setBounds(67, 24, 400, 23);
		serviceIdText.setEditable(false);
		serviceIdText.setEnabled(false);

		Label serviceNameLabel = formToolkit.createLabel(composite_serviceinfo, "\u670D\u52A1\u540D\u79F0", SWT.NONE);
		serviceNameLabel.setAlignment(SWT.RIGHT);
		serviceNameLabel.setForeground(SWTResourceManager.getColor(65, 105, 225));
		serviceNameLabel.setBounds(0, 67, 61, 17);
		serviceNameText = formToolkit.createText(composite_serviceinfo, "New Text", SWT.NONE);
		serviceNameText.setText(compservice.getBaseinfo().getServiceName());
		serviceNameText.setBounds(67, 64, 400, 23);

		Label serviceDescLabel = formToolkit.createLabel(composite_serviceinfo, "\u670D\u52A1\u63CF\u8FF0", SWT.NONE);
		serviceDescLabel.setAlignment(SWT.RIGHT);
		serviceDescLabel.setForeground(SWTResourceManager.getColor(65, 105, 225));
		serviceDescLabel.setBounds(0, 107, 61, 17);
		serviceDescText = formToolkit.createText(composite_serviceinfo, "New Text", SWT.NONE);
		serviceDescText.setText(compservice.getBaseinfo().getDescrition());
		serviceDescText.setBounds(67, 104, 400, 23);

		// 构建协议的下拉列表
		createProtocolCombo(composite_serviceinfo);
		// 为协议下拉列表赋值
		if (compservice.getProtocolName() != null) {
			protocolCombo.setText(compservice.getProtocolName());
		}

		Label packUnPackModeLabel = formToolkit.createLabel(composite_serviceinfo, "数据适配", SWT.NONE);
		packUnPackModeLabel.setAlignment(SWT.RIGHT);
		packUnPackModeLabel.setForeground(SWTResourceManager.getColor(65, 105, 225));
		packUnPackModeLabel.setBounds(0, 187, 61, 17);

		packUnPackModeCombo = new Combo(composite_serviceinfo, SWT.NONE);
		packUnPackModeCombo.setBounds(67, 184, 400, 22);
		packUnPackModeCombo.setItems(GlobalConstants.PACK_UNPACK_MODE);
		formToolkit.adapt(packUnPackModeCombo);
		formToolkit.paintBordersFor(packUnPackModeCombo);
		if (compositeService.getPackUnPackMode() != null) {
			packUnPackModeCombo.setText(compositeService.getPackUnPackMode());
		}

		// 配置文件Section
		Section configFileSection = formToolkit.createSection(scrldfrm_compScrolledform.getBody(),
				Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		configFileSection.setBounds(10, 401, 500, 193);
		formToolkit.paintBordersFor(configFileSection);
		configFileSection.setText("配置文件管理");
		Composite configFileComposite = formToolkit.createComposite(configFileSection, SWT.NONE);
		configFileSection.setClient(configFileComposite);
		configFileTab = formToolkit.createTable(configFileComposite, SWT.NONE);
		configFileTab.setBounds(0, 10, 488, 145);
		formToolkit.paintBordersFor(configFileTab);
		configFileTab.setHeaderVisible(true);
		configFileTab.setLinesVisible(true);
		configFileTab.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event event) {
				int row = configFileTab.getSelectionIndex();
				TableItem t = configFileTab.getItem(row);
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(t.getText(2)));
				if (null != file) {
					try {
						IDE.openEditor(page, file);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		TableColumn tblclmnNewColumn = new TableColumn(configFileTab, SWT.NONE);
		tblclmnNewColumn.setWidth(150);
		tblclmnNewColumn.setText("名称");
		TableColumn tblclmnNewColumn_1 = new TableColumn(configFileTab, SWT.NONE);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText("类型");
		TableColumn tblclmnNewColumn_2 = new TableColumn(configFileTab, SWT.NONE);
		tblclmnNewColumn_2.setWidth(250);
		tblclmnNewColumn_2.setText("路径");
		List<ConfigFile> configFiles = compservice.getConfigs();
		if (null != configFiles) {
			for (ConfigFile configFile : configFiles) {
				TableItem tableItem = new TableItem(configFileTab, SWT.NONE);
				tableItem.setText(0, configFile.getName());
				tableItem.setText(1, configFile.getType());
				tableItem.setText(2, configFile.getPath());
			}
		}
		//新增配置文件的按钮
		ImageHyperlink addConfigLink = formToolkit.createImageHyperlink(configFileSection, SWT.CENTER);
		addConfigLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				CompConfigAddWizard compConfigAddWizard = new CompConfigAddWizard(compservice, configFileTab);
				WizardDialog dialog = new WizardDialog(shell, compConfigAddWizard);
				dialog.setTitle("新增配置文件");
				dialog.open();
			}
		});
		// 设置超链接的图标
		addConfigLink.setImage(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD).createImage());
		// 将该图标超链接添加到内容区的工具栏中
		configFileSection.setTextClient(addConfigLink);

		// 绑定流程展示区
		Section section_flow = formToolkit.createSection(scrldfrm_compScrolledform.getBody(),
				Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		section_flow.setBounds(10, 267, 500, 128);
		formToolkit.paintBordersFor(section_flow);
		section_flow.setText("已绑定流程列表");
		table_flow = new Table(section_flow, SWT.BORDER | SWT.FULL_SELECTION);
		table_flow.setBounds(10, 10, 492, 96);
		table_flow.setLinesVisible(true);
		table_flow.setHeaderVisible(true);
		formToolkit.adapt(table_flow);
		formToolkit.paintBordersFor(table_flow);
		section_flow.setClient(table_flow);
		table_flow.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event e) {
				int selectedIndex = table_flow.getSelectionIndex();
				TableItem selectedItem = table_flow.getItem(selectedIndex);
				String fileName = selectedItem.getText(1);
				if (!"".equals(fileName)) {
					String flowFilePathStr = (new StringBuilder("/")).append(projectName).append("/dev/services/flow/")
							.append(fileName).toString();
					// 这里需要打开bpmn
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IFile flowFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(flowFilePathStr));
					try {
						IDE.openEditor(page, flowFile);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}
		});

		TableColumn tableColumn_flowname = new TableColumn(table_flow, SWT.CENTER);
		tableColumn_flowname.setWidth(170);
		tableColumn_flowname.setText("流程名称");
		TableColumn tableColumn_flowpath = new TableColumn(table_flow, SWT.CENTER);
		tableColumn_flowpath.setWidth(317);
		tableColumn_flowpath.setText("文件路径");
		TableItem tableItem_flow = new TableItem(table_flow, SWT.NONE);
		String flowname = "";
		String flownamePath = "";
		// 判断是否绑定流程
		if (compservice.getFlowname() != null) {
			flownamePath = compservice.getFlowname();
			flowNameSave = flownamePath;
			flowname = compservice.getFlowname().substring(0, compservice.getFlowname().indexOf("."));
		}
		tableItem_flow.setText(0, flowname);
		tableItem_flow.setText(1, flownamePath);

		ImageHyperlink imageHyperlink = formToolkit.createImageHyperlink(section_flow, SWT.CENTER);
		imageHyperlink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				// 传table_flow构造
				BindFlowWizard bindFlow = new BindFlowWizard(projectName, compositeService, table_flow);
				WizardDialog dialog = new WizardDialog(shell, bindFlow);
				dialog.setTitle("绑定流程");
				dialog.open();
				setDirty();
			}
		});
		// 设置超链接的图标
		imageHyperlink.setImage(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD).createImage());
		section_flow.setTextClient(imageHyperlink);
		// 发布按钮功能
		Button btnPublishButton = formToolkit.createButton(scrldfrm_compScrolledform.getBody(), "\u53D1\u5E03",
				SWT.NONE);
		btnPublishButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (null != compositeService.getBaseinfo() && null != compositeService.getSdafo()
						&& null != compositeService.getWsdlinfo()) {
					CompositeNode compNode = new CompositeNode("temp");
					compNode.setCompSvc(compositeService);
					compNode.setProjectName(projectName);
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					PublishServiceWizard pbsw = new PublishServiceWizard(compNode);
					WizardDialog dialog = new WizardDialog(shell, pbsw);
					dialog.setTitle("发布服务");
					dialog.open();
				} else {
					PlatformUtils.showInfo("提示", "组合服务对象数据不完整！");
				}
			}
		});
		btnPublishButton.setBounds(576, 450, 80, 27);

		if (compservice.getWsdlinfo() != null) {
			combo_protocolType.setText(compservice.getWsdlinfo().getProtocoltype() == null ? ""
					: compservice.getWsdlinfo().getProtocoltype());
			txt_protocolURL
					.setText(compservice.getWsdlinfo().getUrl() == null ? "" : compservice.getWsdlinfo().getUrl());
			txt_protocolNsp.setText(
					compservice.getWsdlinfo().getNamespace() == null ? "" : compservice.getWsdlinfo().getNamespace());
			txt_protocolBinding
					.setText(compservice.getWsdlinfo().getBind() == null ? "" : compservice.getWsdlinfo().getBind());
			txt_protocolEndPoint.setText(
					compservice.getWsdlinfo().getEndpoint() == null ? "" : compservice.getWsdlinfo().getEndpoint());
			txt_protocolOption.setText(
					compservice.getWsdlinfo().getOperation() == null ? "" : compservice.getWsdlinfo().getOperation());
		}
		scrldfrm_compScrolledform.reflow(true);
	}

	private void createProtocolCombo(Composite composite) {
		// 默认的协议面板
		protocolComposite = formToolkit.createComposite(protocolInfoSection, SWT.NONE);
		serviceProtocolLabel = formToolkit.createLabel(composite, "绑定协议", SWT.NONE);
		serviceProtocolLabel.setAlignment(SWT.RIGHT);
		serviceProtocolLabel.setForeground(SWTResourceManager.getColor(65, 105, 225));
		serviceProtocolLabel.setBounds(0, 147, 61, 17);
		protocolCombo = new Combo(composite, SWT.NONE);
		protocolCombo.setBounds(67, 144, 400, 22);
		// 修改协议下拉框之后的事件
		protocolCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String protocolName = protocolCombo.getText();
				ProtocolInService protocolInfo = protocolService.getInProtocolInfo(protocolName);
				disposeProtocolInfoComposite();
				createProtocolInfoComposite(protocolInfo);
				protocolInfoSection.layout();
				setDirty();
			}
		});
		// 点击事件，点击后触发刷新协议信息的事件
		protocolCombo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Collection<ProtocolInService> outProtocols = protocolService.getAllInProtocols();
				List<String> items = Arrays.asList(protocolCombo.getItems());
				for (ProtocolInService outProtocol : outProtocols) {
					if (!items.contains(outProtocol.getProtocolName()))
						protocolCombo.add(outProtocol.getProtocolName());
				}
			}
		});
		formToolkit.adapt(protocolCombo);
		formToolkit.paintBordersFor(protocolCombo);
	}

	private void disposeProtocolInfoComposite() {
		protocolComposite.dispose();
		protocolInfoSection.layout();
	}

	private void disposeWSSection() {
		if (null != wsSection) {
			wsSection.dispose();
		}
	}

	private void createTCPProtocolInfoComposite(ProtocolInService protocolInfo) {
		protocolComposite = formToolkit.createComposite(protocolInfoSection, SWT.NONE);
		formToolkit.paintBordersFor(protocolComposite);
		protocolInfoSection.setClient(protocolComposite);

		// 协议类型
		Label protocolTypeLabel = new Label(protocolComposite, SWT.NONE);
		protocolTypeLabel.setAlignment(SWT.RIGHT);
		protocolTypeLabel.setBounds(10, 32, 60, 17);
		protocolTypeLabel.setText("协议类型:");

		text_protocolType = new Text(protocolComposite, SWT.BORDER);
		text_protocolType.setEditable(false);
		text_protocolType.setText(protocolInfo.getProtocolType() == null ? "" : protocolInfo.getProtocolType());
		text_protocolType.setBounds(80, 30, 400, 23);

		Label ipLabel = new Label(protocolComposite, SWT.RIGHT);
		ipLabel.setBounds(10, 72, 60, 17);
		ipLabel.setText("IP:");

		ipText = new Text(protocolComposite, SWT.BORDER);
		ipText.setEditable(false);
		ipText.setBounds(80, 70, 400, 23);
		ipText.setText(protocolInfo.getIp() == null ? "" : protocolInfo.getIp());

		Label portLabel = new Label(protocolComposite, SWT.RIGHT);
		portLabel.setBounds(10, 112, 60, 17);
		portLabel.setText("端口:");

		portText = new Text(protocolComposite, SWT.BORDER);
		portText.setEditable(false);
		portText.setBounds(80, 110, 400, 23);
		portText.setText(protocolInfo.getPort() == null ? "" : protocolInfo.getPort());

		Label timeoutLabel = new Label(protocolComposite, SWT.RIGHT);
		timeoutLabel.setBounds(10, 152, 60, 17);
		timeoutLabel.setText("超时时间:");

		timeoutText = new Text(protocolComposite, SWT.BORDER);
		timeoutText.setEditable(false);
		timeoutText.setBounds(80, 150, 400, 23);
		timeoutText.setText(protocolInfo.getOverTime() == null ? "" : protocolInfo.getOverTime());

		Label readPolicyLabel = new Label(protocolComposite, SWT.RIGHT);
		readPolicyLabel.setBounds(10, 192, 60, 17);
		readPolicyLabel.setText("读策略:");

		readPolicyText = new Text(protocolComposite, SWT.BORDER);
		readPolicyText.setEditable(false);
		readPolicyText.setBounds(80, 190, 400, 23);
		readPolicyText.setText(protocolInfo.getReadMethod() == null ? "" : protocolInfo.getReadMethod());

		Label writePolicyLabel = new Label(protocolComposite, SWT.RIGHT);
		writePolicyLabel.setBounds(10, 232, 60, 17);
		writePolicyLabel.setText("写策略:");

		writePolicyText = new Text(protocolComposite, SWT.BORDER);
		writePolicyText.setEditable(false);
		writePolicyText.setBounds(80, 230, 400, 23);
		writePolicyText.setText(protocolInfo.getWriteMethod() == null ? "" : protocolInfo.getWriteMethod());

		Label maxThreadLabel = new Label(protocolComposite, SWT.RIGHT);
		maxThreadLabel.setBounds(10, 272, 60, 17);
		maxThreadLabel.setText("最大线程数:");

		maxThreadText = new Text(protocolComposite, SWT.BORDER);
		maxThreadText.setEditable(false);
		maxThreadText.setBounds(80, 270, 400, 23);
		maxThreadText.setText(protocolInfo.getMaxThreads() == null ? "" : protocolInfo.getMaxThreads());

		Label minThreadLabel = new Label(protocolComposite, SWT.RIGHT);
		minThreadLabel.setText("最小线程数:");
		minThreadLabel.setBounds(10, 312, 60, 17);

		minThreadText = new Text(protocolComposite, SWT.BORDER);
		minThreadText.setEditable(false);
		minThreadText.setBounds(80, 310, 400, 23);
		minThreadText.setText(protocolInfo.getMinThreads() == null ? "" : protocolInfo.getMinThreads());

		Label queueNumLabel = new Label(protocolComposite, SWT.RIGHT);
		queueNumLabel.setBounds(10, 352, 60, 17);
		queueNumLabel.setText("队列数:");

		queueNumText = new Text(protocolComposite, SWT.BORDER);
		queueNumText.setEditable(false);
		queueNumText.setBounds(80, 350, 400, 23);
		queueNumText.setText(protocolInfo.getAcceptSize() == null ? "" : protocolInfo.getAcceptSize());

		Label queueSizeLabel = new Label(protocolComposite, SWT.RIGHT);
		queueSizeLabel.setBounds(10, 392, 60, 17);
		queueSizeLabel.setText("队列长度:");

		queueSizeText = new Text(protocolComposite, SWT.BORDER);
		queueSizeText.setEditable(false);
		queueSizeText.setBounds(80, 390, 400, 23);
		queueSizeText.setText(protocolInfo.getAcceptQueueSize() == null ? "" : protocolInfo.getAcceptQueueSize());
	}

	private void createHTTPProtocolInfoComposite(ProtocolInService protocolInfo) {
		protocolComposite = formToolkit.createComposite(protocolInfoSection, SWT.NONE);
		formToolkit.paintBordersFor(protocolComposite);
		protocolInfoSection.setClient(protocolComposite);

		// 协议类型
		Label protocolTypeLabel = new Label(protocolComposite, SWT.NONE);
		protocolTypeLabel.setAlignment(SWT.RIGHT);
		protocolTypeLabel.setBounds(10, 32, 60, 17);
		protocolTypeLabel.setText("协议类型:");

		text_protocolType = new Text(protocolComposite, SWT.BORDER);
		text_protocolType.setEditable(false);
		text_protocolType.setText(protocolInfo.getProtocolType());
		text_protocolType.setBounds(80, 30, 400, 23);

		Label urlLabel = new Label(protocolComposite, SWT.RIGHT);
		urlLabel.setBounds(10, 72, 60, 17);
		urlLabel.setText("URL:");

		urlText = new Text(protocolComposite, SWT.BORDER);
		urlText.setEditable(false);
		urlText.setBounds(80, 70, 400, 23);
		urlText.setText(protocolInfo.getUrl());

		Label httpVersionLabel = new Label(protocolComposite, SWT.RIGHT);
		httpVersionLabel.setBounds(10, 112, 60, 17);
		httpVersionLabel.setText("HTTP版本:");

		httpVersionText = new Text(protocolComposite, SWT.BORDER);
		httpVersionText.setEditable(false);
		httpVersionText.setBounds(80, 110, 400, 23);
		httpVersionText.setText(protocolInfo.getContactProtocolType());

		Label timeoutLabel = new Label(protocolComposite, SWT.RIGHT);
		timeoutLabel.setBounds(10, 152, 60, 17);
		timeoutLabel.setText("超时时间:");

		timeoutText = new Text(protocolComposite, SWT.BORDER);
		timeoutText.setEditable(false);
		timeoutText.setBounds(80, 150, 400, 23);
		timeoutText.setText(protocolInfo.getOverTime() == null ? "" : protocolInfo.getOverTime());

		Label maxThreadLabel = new Label(protocolComposite, SWT.RIGHT);
		maxThreadLabel.setBounds(10, 192, 60, 17);
		maxThreadLabel.setText("最大线程数:");

		maxThreadText = new Text(protocolComposite, SWT.BORDER);
		maxThreadText.setEditable(false);
		maxThreadText.setBounds(80, 190, 400, 23);
		maxThreadText.setText(protocolInfo.getMaxThreads());

		Label minThreadLabel = new Label(protocolComposite, SWT.RIGHT);
		minThreadLabel.setText("最小线程数:");
		minThreadLabel.setBounds(10, 232, 60, 17);

		minThreadText = new Text(protocolComposite, SWT.BORDER);
		minThreadText.setEditable(false);
		minThreadText.setBounds(80, 230, 400, 23);
		minThreadText.setText(protocolInfo.getMinThreads());

		Label queueNumLabel = new Label(protocolComposite, SWT.RIGHT);
		queueNumLabel.setBounds(10, 272, 60, 17);
		queueNumLabel.setText("队列数:");

		queueNumText = new Text(protocolComposite, SWT.BORDER);
		queueNumText.setEditable(false);
		queueNumText.setBounds(80, 270, 400, 23);
		queueNumText.setText(protocolInfo.getAcceptSize());

		Label queueSizeLabel = new Label(protocolComposite, SWT.RIGHT);
		queueSizeLabel.setBounds(10, 312, 60, 17);
		queueSizeLabel.setText("队列长度:");

		queueSizeText = new Text(protocolComposite, SWT.BORDER);
		queueSizeText.setEditable(false);
		queueSizeText.setBounds(80, 310, 400, 23);
		queueSizeText.setText(protocolInfo.getAcceptSize());
	}

	private void createProtocolInfoComposite(ProtocolInService protocolInfo) {
		if (null != protocolInfo) {
			String protocolType = protocolInfo.getProtocolType();
			if (ProtocolConstants.TCP_PROTOCOL.equalsIgnoreCase(protocolType)) {
				disposeWSSection();
				createTCPProtocolInfoComposite(protocolInfo);
			} else if (ProtocolConstants.WS_PROTOCOL.equalsIgnoreCase(protocolType)) {
				disposeWSSection();
				createHTTPProtocolInfoComposite(protocolInfo);
				createWSSection();
			} else if (ProtocolConstants.HTTP_PROTOCOL.equalsIgnoreCase(protocolType)) {
				disposeWSSection();
				createHTTPProtocolInfoComposite(protocolInfo);
			}
		}
	}

	private void createWSSection() {
	}

	@Override
	protected void checkSubclass() {
	}

	@Override
	public boolean setFocus() {
		return true;
	}

	public boolean saveComposite() {
		String projectName = compositeService.getProject().getName();
		String compositeFilePath = (new StringBuilder("/")).append(projectName).append(BipConstantUtil.CompositePath)
				.append(compositeService.getBaseinfo().getServiceId()).append(".composite").toString();
		IPath compositePath = new Path(compositeFilePath);
		final IFile compositeFile = ResourcesPlugin.getWorkspace().getRoot().getFile(compositePath);
		try {
			if (!compositeFile.exists()) {
				compositeFile.create(null, true, null);
			}
			// 设置组合服务的属性
			// 设置服务ID
			compositeService.getBaseinfo().setServiceId(serviceIdText.getText());
			// 设置服务名
			compositeService.getBaseinfo().setServiceName(serviceNameText.getText());
			// 设置服务描述
			compositeService.getBaseinfo().setDescrition(serviceDescText.getText());
			// 设置协议信息
			compositeService.setProtocolName(protocolCombo.getText());
			// 设置拆组包模式
			compositeService.setPackUnPackMode(packUnPackModeCombo.getText());
			// 设置配置文件
//			compositeService.set
			HelpUtil.writeObject(compositeService, compositeFile);
			editor.setDirty(false);
			return true;
		} catch (CoreException e2) {
			e2.printStackTrace();
			return false;
		}

	}
}
