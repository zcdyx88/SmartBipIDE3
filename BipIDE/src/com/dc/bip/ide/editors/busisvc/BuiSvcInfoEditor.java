package com.dc.bip.ide.editors.busisvc;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;

import com.dc.bip.ide.common.ProtocolConstants;
import com.dc.bip.ide.common.model.ConfigFile;
import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.objects.ProtocolInService;
import com.dc.bip.ide.objects.ReversalCondition;
import com.dc.bip.ide.repository.impl.ProtocolServiceRepository;
import com.dc.bip.ide.service.ProtocolService;
import com.dc.bip.ide.util.BusiSvcService;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.views.objects.BSOperationNode;
import com.dc.bip.ide.wizards.busi.BusiConfigAddWizard;
import com.dc.bip.ide.wizards.busi.ReversalConditionAddWizard;

public class BuiSvcInfoEditor extends EditorPart implements IEditorPart {

	// 主页面ScrolledForm
	private ScrolledForm scrldfrmNewScrolledform;
	// 业务服务的Node
	private BSOperationNode bsOperationNode = null;
	// 业务服务信息
	private BusiSvcInfo busiSvcInfo = null;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text text_protocolType;
	private Text text_URL;
	private Text text_nameSpace;
	private Text text_binding;
	private Text text_version;
	private Text text_endPoint;
	private Table table;
	private ComboViewer comboViewer;
	private boolean editorFlag = false;
	private FormToolkit toolkit;
	private Text text;
	private Text text_1;
	private Text text_2;
	private boolean dirty = false;
	private IFile file;
	private Table reversalConditionTable;
	private final String NAME = ".busi";
	private Text textServiceName;
	private Text textServiceDesc;
	private Table configFileTab;
	private TableCursor cursor;

	// 拆组包
	private Combo packerUnpackerCombo;

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

	// 协议信息的section
	private Section protocolInfoSection;
	// 协议信息section的composite
	private Composite protocolComposite;
	// 绑定协议Label
	private Label protocolLabel;
	// 协议的Combo
	private Combo protocolCombo;

	// 当前工程的协议操作服务
	private ProtocolService protocolService;

	private void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public BuiSvcInfoEditor(BSOperationNode bsOperationNode) {
		this.bsOperationNode = bsOperationNode;
		this.busiSvcInfo = bsOperationNode.getBusiSvcInfo();
		String fileFolderStr = new StringBuilder("/").append(bsOperationNode.getProjectName())
				.append("/dev/services/busi/").append(bsOperationNode.getNodeName() + ".busi").toString();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		Path basePPath = new Path(fileFolderStr);
		file = root.getFile(basePPath);

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// 服务名称
		String serviceName = textServiceName.getText() == null ? "" : textServiceName.getText();
		// 服务描述
		String serviceDesc = textServiceDesc.getText() == null ? "" : textServiceDesc.getText();
		// 服务协议
		String serviceProtocolName = protocolCombo.getText() == null ? "" : protocolCombo.getText();
		// 拆组包
		String packUnpackMode = packerUnpackerCombo.getText() == null ? "" : packerUnpackerCombo.getText();
		busiSvcInfo.setName(serviceName);
		busiSvcInfo.setDecscription(serviceDesc);
		busiSvcInfo.setPackUnPackMode(packUnpackMode);
		busiSvcInfo.setProtocolName(serviceProtocolName);
		bsOperationNode.setBusiSvcInfo(busiSvcInfo);
		bsOperationNode.persist();
		setDirty(false);
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public void doSaveAs() {
		MessageDialog.openInformation(Display.getCurrent().getActiveShell(), null, "doSaveAs");
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.setSite(site);
		protocolService = ProtocolServiceRepository.getInstance().get(this.bsOperationNode.getProjectName());
		// 刷新文件
		List<String> fileList = busiSvcInfo.getLocalUris();
		for (int i = 0; i < fileList.size(); i++) {
			String localUri = fileList.get(i);
			IWorkspaceRoot wRoot = ResourcesPlugin.getWorkspace().getRoot();
			Path basePPath = new Path(localUri);
			IFile basefile = wRoot.getFile(basePPath);
			try {
				basefile.refreshLocal(0, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

		try {
			toolkit = new FormToolkit(parent.getDisplay());
			parent.setLayout(new FillLayout(SWT.HORIZONTAL));

			scrldfrmNewScrolledform = toolkit.createScrolledForm(parent);
			scrldfrmNewScrolledform.setAlwaysShowScrollBars(true);
			scrldfrmNewScrolledform.setExpandVertical(true);
			scrldfrmNewScrolledform.setExpandHorizontal(true);
			scrldfrmNewScrolledform.setMinHeight(600);
			scrldfrmNewScrolledform.setMinWidth(600);
			toolkit.paintBordersFor(scrldfrmNewScrolledform);
			// 业务服务Section
			Section section = toolkit.createSection(scrldfrmNewScrolledform.getBody(),
					Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
			section.setBounds(10, 10, 500, 391);
			toolkit.paintBordersFor(section);
			section.setText("业务服务信息");
			// 业务服务section的composite
			Composite composite = toolkit.createComposite(section, SWT.NONE);
			toolkit.paintBordersFor(composite);
			section.setClient(composite);

			Label lblNewLabel_1 = new Label(composite, SWT.NONE);
			lblNewLabel_1.setAlignment(SWT.RIGHT);
			lblNewLabel_1.setBounds(0, 27, 61, 17);
			toolkit.adapt(lblNewLabel_1, true, true);
			lblNewLabel_1.setText("\u670D\u52A1ID:");

			Text textServiceId = new Text(composite, SWT.BORDER);
			textServiceId.setEditable(false);
			textServiceId.setBounds(67, 25, 411, 23);
			textServiceId.setText(busiSvcInfo.getId());
			toolkit.adapt(textServiceId, true, true);

			Label lblNewLabel_2 = new Label(composite, SWT.NONE);
			lblNewLabel_2.setAlignment(SWT.RIGHT);
			lblNewLabel_2.setBounds(0, 67, 61, 17);
			toolkit.adapt(lblNewLabel_2, true, true);
			lblNewLabel_2.setText("\u670D\u52A1\u540D\u79F0:");

			textServiceName = new Text(composite, SWT.BORDER);
			textServiceName.setBounds(67, 65, 411, 23);
			textServiceName.setText(busiSvcInfo.getName());
			toolkit.adapt(textServiceName, true, true);

			Label lblNewLabel_3 = new Label(composite, SWT.NONE);
			lblNewLabel_3.setAlignment(SWT.RIGHT);
			lblNewLabel_3.setBounds(0, 107, 61, 17);
			toolkit.adapt(lblNewLabel_3, true, true);
			lblNewLabel_3.setText("\u670D\u52A1\u63CF\u8FF0:");

			textServiceDesc = new Text(composite, SWT.BORDER);
			textServiceDesc.setBounds(67, 105, 411, 23);
			textServiceDesc.setText(busiSvcInfo.getDecscription());
			toolkit.adapt(textServiceDesc, true, true);

			textServiceDesc.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
					firePropertyChange(PROP_DIRTY);
				}
			});
			textServiceName.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
					firePropertyChange(PROP_DIRTY);
				}
			});

			// 协议信息的section
			protocolInfoSection = toolkit.createSection(scrldfrmNewScrolledform.getBody(),
					Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
			protocolInfoSection.setBounds(537, 10, 500, 391);

			toolkit.paintBordersFor(protocolInfoSection);
			protocolInfoSection.setText("协议信息");
			// 协议信息section的composite
			// 构建协议下拉选项
			createProtocolCombo(composite);
			// 拆组包类型
			createPackUnPackModeCombo(composite);
			// 创建冲正服务修改
			createReversalServiceCombo(composite);
			// 创建冲正条件table
			createReversalConditionTable(composite);
			// 配置文件管理Section
			Section configFileSection = toolkit.createSection(scrldfrmNewScrolledform.getBody(),
					Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
			configFileSection.setBounds(10, 407, 500, 303);
			toolkit.paintBordersFor(configFileSection);
			configFileSection.setText("配置文件管理");
			Composite configFileComposite = toolkit.createComposite(configFileSection, SWT.NONE);
			configFileSection.setClient(configFileComposite);
			configFileTab = toolkit.createTable(configFileComposite, SWT.NONE);
			configFileTab.setBounds(0, 10, 477, 109);
			toolkit.paintBordersFor(configFileTab);
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
			tblclmnNewColumn_2.setWidth(320);
			tblclmnNewColumn_2.setText("路径");
			List<ConfigFile> configFiles = busiSvcInfo.getConfigs();
			for (ConfigFile configFile : configFiles) {
				TableItem tableItem = new TableItem(configFileTab, SWT.NONE);
				tableItem.setText(0, configFile.getName());
				tableItem.setText(1, configFile.getType());
				tableItem.setText(2, configFile.getPath());
			}
			ImageHyperlink imageHyperlink = toolkit.createImageHyperlink(configFileSection, SWT.CENTER);
			imageHyperlink.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					BusiConfigAddWizard busiConfigAddWizard = new BusiConfigAddWizard(bsOperationNode, configFileTab);
					WizardDialog dialog = new WizardDialog(shell, busiConfigAddWizard);
					dialog.setTitle("新增配置文件");
					dialog.open();
				}
			});
			// 设置超链接的图标
			imageHyperlink.setImage(PlatformUI.getWorkbench().getSharedImages()
					.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD).createImage());
			// 将该图标超链接添加到内容区的工具栏中
			configFileSection.setTextClient(imageHyperlink);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setDirty(false);
		}

	}

	// 构建拆组包Combo
	private void createPackUnPackModeCombo(Composite composite) {
		Label packerUnpackerLabel = new Label(composite, SWT.NONE);
		packerUnpackerLabel.setText("数据适配:");
		packerUnpackerLabel.setAlignment(SWT.RIGHT);
		packerUnpackerLabel.setBounds(2, 184, 68, 28);
		toolkit.adapt(packerUnpackerLabel, true, true);
		formToolkit.adapt(packerUnpackerLabel, true, true);

		packerUnpackerCombo = new Combo(composite, SWT.NONE);
		packerUnpackerCombo.setBounds(86, 181, 392, 28);
		toolkit.adapt(packerUnpackerCombo);
		toolkit.paintBordersFor(packerUnpackerCombo);
		formToolkit.adapt(packerUnpackerCombo);
		formToolkit.paintBordersFor(packerUnpackerCombo);
		packerUnpackerCombo.add("XML");
		packerUnpackerCombo.add("SOAP");
		packerUnpackerCombo.add("CD");
		String packUnPackMode = busiSvcInfo.getPackUnPackMode() == null ? "" : busiSvcInfo.getPackUnPackMode();
		packerUnpackerCombo.setText(packUnPackMode);
		// TODO拆组包类型应该在配置中读取
		packerUnpackerCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setDirty(true);
				firePropertyChange(PROP_DIRTY);
			}
		});
	}

	// 构建协议Combo
	private void createProtocolCombo(Composite composite) {
		// 默认的协议面板
		protocolComposite = toolkit.createComposite(protocolInfoSection, SWT.NONE);

		protocolLabel = new Label(composite, SWT.NONE);
		protocolLabel.setBounds(0, 147, 70, 23);
		protocolLabel.setAlignment(SWT.RIGHT);
		toolkit.adapt(protocolLabel, true, true);
		formToolkit.adapt(protocolLabel, true, true);
		protocolLabel.setText("绑定协议:");

		protocolCombo = new Combo(composite, SWT.NONE);
		protocolCombo.setBounds(76, 144, 402, 28);
		protocolCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String protocolName = protocolCombo.getText();
				ProtocolInService protocolInfo = protocolService.getOutProtocolInfo(protocolName);
				disposeProtocolInfoComposite();
				createProtocolInfoComposite(protocolInfo);
				protocolInfoSection.layout();
				setDirty(true);
				firePropertyChange(PROP_DIRTY);
			}
		});
		// 点击事件，点击后触发刷新协议信息的事件
		protocolCombo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Collection<ProtocolInService> outProtocols = protocolService.getAllOutProtocols();
				List<String> items = Arrays.asList(protocolCombo.getItems());
				for (ProtocolInService outProtocol : outProtocols) {
					if (!items.contains(outProtocol.getProtocolName()))
						protocolCombo.add(outProtocol.getProtocolName());
				}
			}
		});

		// 初始化协议信息
		String protocolName = busiSvcInfo.getProtocolName();
		if (null != protocolName || "".equalsIgnoreCase(protocolName)) {
			protocolCombo.setText(protocolName);
			ProtocolInService protocolInfo = protocolService.getOutProtocolInfo(protocolName);
			disposeProtocolInfoComposite();
			createProtocolInfoComposite(protocolInfo);
			protocolInfoSection.layout();
		}
		toolkit.adapt(protocolCombo);
		toolkit.paintBordersFor(protocolCombo);
		formToolkit.adapt(protocolCombo);
		formToolkit.paintBordersFor(protocolCombo);
	}

	// 构建反交易
	private void createReversalServiceCombo(Composite composite) {
		Label lblid = new Label(composite, SWT.NONE);
		lblid.setAlignment(SWT.RIGHT);
		lblid.setBounds(0, 226, 61, 17);
		lblid.setText("反交易ID:");
		comboViewer = new ComboViewer(composite, SWT.NONE);
		Combo combo = comboViewer.getCombo();
		combo.setBounds(67, 224, 320, 22);
		/*modify by wanming 20160810 start*/
		refreshReversalServiceCombo(combo);
		/*modify end*/
		combo.setEnabled(editorFlag);
		/*add by wanming 20160810 start*/
		combo.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseUp(MouseEvent arg0) {
				Combo combo = comboViewer.getCombo();
				refreshReversalServiceCombo(combo);
			}
			
		});
		/*add end*/
		Button modifyReversalBtn = new Button(composite, SWT.NONE);
		modifyReversalBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		modifyReversalBtn.setBounds(407, 220, 60, 27);
		{
			modifyReversalBtn.setText("修改");
		}
		modifyReversalBtn.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {

			}

			@Override
			public void mouseDown(MouseEvent e) {

			}

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO 保存反交易
				Combo tmpCombo = comboViewer.getCombo();
				editorFlag = !editorFlag;
				tmpCombo.setEnabled(editorFlag);
				if (editorFlag) {
					refreshReversalServiceCombo(tmpCombo);
					((Button) (e.getSource())).setText("保存");
				} else {
					((Button) (e.getSource())).setText("修改");
					busiSvcInfo.setOppositeBusi(
							(BusiSvcInfo) tmpCombo.getData(String.valueOf(tmpCombo.getSelectionIndex())));
					bsOperationNode.setBusiSvcInfo(busiSvcInfo);
					HelpUtil.writeObject(bsOperationNode, file);
				}
			}
		});
		if (editorFlag) {
			modifyReversalBtn.setText("保存");
		}
	}

	/**
	 * 刷新反交易列表
	 * @param combo
	 */
	private void refreshReversalServiceCombo(Combo combo) {
		combo.removeAll();
		List<BusiSvcInfo> busiSvcInfoList = BusiSvcService.getAll(busiSvcInfo.getProjectName());
		int selectItem = 0;
		for (int i = 0; i < busiSvcInfoList.size(); i++) {
			combo.add(busiSvcInfoList.get(i).getId(), i);
			combo.setData(String.valueOf(i), busiSvcInfoList.get(i));
			if (null != busiSvcInfo.getOppositeBusi()) {
				if (busiSvcInfoList.get(i).getId().equalsIgnoreCase(busiSvcInfo.getOppositeBusi().getId())) {
					selectItem = i;
				}
			}

		}
		combo.select(selectItem);
	}
	
	private void disposeProtocolInfoComposite() {
		protocolComposite.dispose();
		protocolInfoSection.layout();
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
		wsSection = toolkit.createSection(scrldfrmNewScrolledform.getBody(),
				Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		wsSection.setBounds(530, 720, 500, 400);
		toolkit.paintBordersFor(wsSection);
		wsSection.setText("WebService属性");
		scrldfrmNewScrolledform.layout();
	}

	private void disposeWSSection() {
		if (null != wsSection) {
			wsSection.dispose();
		}
	}

	private void createTCPProtocolInfoComposite(ProtocolInService protocolInfo) {
		protocolComposite = toolkit.createComposite(protocolInfoSection, SWT.NONE);
		toolkit.paintBordersFor(protocolComposite);
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

		Label ipLabel = new Label(protocolComposite, SWT.RIGHT);
		ipLabel.setBounds(10, 72, 60, 17);
		ipLabel.setText("IP:");

		ipText = new Text(protocolComposite, SWT.BORDER);
		ipText.setEditable(false);
		ipText.setBounds(80, 70, 400, 23);
		ipText.setText(protocolInfo.getIp());

		Label portLabel = new Label(protocolComposite, SWT.RIGHT);
		portLabel.setBounds(10, 112, 60, 17);
		portLabel.setText("端口:");

		portText = new Text(protocolComposite, SWT.BORDER);
		portText.setEditable(false);
		portText.setBounds(80, 110, 400, 23);
		portText.setText(protocolInfo.getPort());

		Label timeoutLabel = new Label(protocolComposite, SWT.RIGHT);
		timeoutLabel.setBounds(10, 152, 60, 17);
		timeoutLabel.setText("超时时间:");

		timeoutText = new Text(protocolComposite, SWT.BORDER);
		timeoutText.setEditable(false);
		timeoutText.setBounds(80, 150, 400, 23);
		timeoutText.setText(protocolInfo.getOverTime());

		Label readPolicyLabel = new Label(protocolComposite, SWT.RIGHT);
		readPolicyLabel.setBounds(10, 192, 60, 17);
		readPolicyLabel.setText("读策略:");

		readPolicyText = new Text(protocolComposite, SWT.BORDER);
		readPolicyText.setEditable(false);
		readPolicyText.setBounds(80, 190, 400, 23);
		readPolicyText.setText(protocolInfo.getReadMethod());

		Label writePolicyLabel = new Label(protocolComposite, SWT.RIGHT);
		writePolicyLabel.setBounds(10, 232, 60, 17);
		writePolicyLabel.setText("写策略:");

		writePolicyText = new Text(protocolComposite, SWT.BORDER);
		writePolicyText.setEditable(false);
		writePolicyText.setBounds(80, 230, 400, 23);
		writePolicyText.setText(protocolInfo.getWriteMethod());

		Label maxThreadLabel = new Label(protocolComposite, SWT.RIGHT);
		maxThreadLabel.setBounds(10, 272, 60, 17);
		maxThreadLabel.setText("最大线程数:");

		maxThreadText = new Text(protocolComposite, SWT.BORDER);
		maxThreadText.setEditable(false);
		maxThreadText.setBounds(80, 270, 400, 23);
		maxThreadText.setText(protocolInfo.getMaxThreads());

		Label minThreadLabel = new Label(protocolComposite, SWT.RIGHT);
		minThreadLabel.setText("最小线程数:");
		minThreadLabel.setBounds(10, 312, 60, 17);

		minThreadText = new Text(protocolComposite, SWT.BORDER);
		minThreadText.setEditable(false);
		minThreadText.setBounds(80, 310, 400, 23);
		minThreadText.setText(protocolInfo.getMinThreads());

		Label queueNumLabel = new Label(protocolComposite, SWT.RIGHT);
		queueNumLabel.setBounds(10, 352, 60, 17);
		queueNumLabel.setText("队列数:");

		queueNumText = new Text(protocolComposite, SWT.BORDER);
		queueNumText.setEditable(false);
		queueNumText.setBounds(80, 350, 400, 23);
		queueNumText.setText(protocolInfo.getAcceptSize());

		Label queueSizeLabel = new Label(protocolComposite, SWT.RIGHT);
		queueSizeLabel.setBounds(10, 392, 60, 17);
		queueSizeLabel.setText("队列长度:");

		queueSizeText = new Text(protocolComposite, SWT.BORDER);
		queueSizeText.setEditable(false);
		queueSizeText.setBounds(80, 390, 400, 23);
		queueSizeText.setText(protocolInfo.getAcceptSize());
	}

	private void createWSProtocolInfoComposite_x(ProtocolInService protocolInfo) {
		protocolComposite = toolkit.createComposite(protocolInfoSection, SWT.NONE);
		toolkit.paintBordersFor(protocolComposite);
		protocolInfoSection.setClient(protocolComposite);

		// 服务协议
		// 协议类型
		Label protocolTypeLabel = new Label(protocolComposite, SWT.NONE);
		protocolTypeLabel.setAlignment(SWT.RIGHT);
		protocolTypeLabel.setBounds(10, 32, 60, 17);
		protocolTypeLabel.setText("协议类型:");

		text_protocolType = new Text(protocolComposite, SWT.BORDER);
		text_protocolType.setEditable(false);
		text_protocolType.setText(protocolInfo.getProtocolType());
		text_protocolType.setBounds(80, 30, 400, 23);

		Label lblNewLabel = new Label(protocolComposite, SWT.RIGHT);
		lblNewLabel.setBounds(37, 85, 61, 17);
		lblNewLabel.setText("URL:");

		text_URL = new Text(protocolComposite, SWT.BORDER);
		text_URL.setEditable(false);
		text_URL.setBounds(104, 83, 425, 23);
		text_URL.setText(this.busiSvcInfo.getWsdlUrl() == null ? "" : this.busiSvcInfo.getWsdlUrl());

		Label lblNamespace = new Label(protocolComposite, SWT.RIGHT);
		lblNamespace.setBounds(10, 125, 88, 17);
		lblNamespace.setText("NameSpace:");

		text_nameSpace = new Text(protocolComposite, SWT.BORDER);
		text_nameSpace.setEditable(false);
		text_nameSpace.setBounds(104, 123, 425, 23);
		text_nameSpace.setText(this.busiSvcInfo.getNameSpace() == null ? "" : this.busiSvcInfo.getNameSpace());

		Label lblVersion = new Label(protocolComposite, SWT.RIGHT);
		lblVersion.setBounds(37, 205, 61, 17);
		lblVersion.setText("Version:");

		text_binding = new Text(protocolComposite, SWT.BORDER);
		text_binding.setEditable(false);
		text_binding.setBounds(104, 203, 425, 23);
		text_binding.setText(this.busiSvcInfo.getVersion() == null ? "" : this.busiSvcInfo.getVersion());

		Label lblBinding = new Label(protocolComposite, SWT.RIGHT);
		lblBinding.setBounds(37, 165, 61, 17);
		lblBinding.setText("Binding:");

		Label lblEndPoint = new Label(protocolComposite, SWT.RIGHT);
		lblEndPoint.setBounds(37, 245, 61, 17);
		lblEndPoint.setText("EndPoint:");

		text_version = new Text(protocolComposite, SWT.BORDER);
		text_version.setEditable(false);
		text_version.setBounds(104, 162, 425, 23);
		text_version.setText(this.busiSvcInfo.getBindingName() == null ? "" : this.busiSvcInfo.getBindingName());

		text_endPoint = new Text(protocolComposite, SWT.BORDER);
		// text_endPoint.setEditable(false);
		text_endPoint.setBounds(104, 243, 425, 23);
		text_endPoint.setText(this.busiSvcInfo.getEndpoint() == null ? "" : this.busiSvcInfo.getEndpoint());
		text_endPoint.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
				firePropertyChange(PROP_DIRTY);
			}
		});

		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(10));
		tableLayout.addColumnData(new ColumnWeightData(10));
		tableLayout.addColumnData(new ColumnWeightData(10));
		tableLayout.addColumnData(new ColumnWeightData(10));
		Group grpOperations = new Group(protocolComposite, SWT.NONE);
		// grpOperations.setBounds(10, 352, 560, 119);
		grpOperations.setBounds(10, 300, 560, 119);
		grpOperations.setText("Operations");
		grpOperations.setLayout(new FormLayout());

		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(grpOperations,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		FormData fd_scrolledComposite_1 = new FormData();
		fd_scrolledComposite_1.bottom = new FormAttachment(100, -5);
		fd_scrolledComposite_1.right = new FormAttachment(100, -5);
		fd_scrolledComposite_1.top = new FormAttachment(0, 5);
		fd_scrolledComposite_1.left = new FormAttachment(0, 5);
		scrolledComposite_1.setLayoutData(fd_scrolledComposite_1);
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);

		TableViewer tableViewer = new TableViewer(scrolledComposite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayout(tableLayout);
		scrolledComposite_1.setContent(table);
		scrolledComposite_1.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		TableColumn column_Name = new TableColumn(table, SWT.None);
		column_Name.setText("Name");
		TableColumn column_Use = new TableColumn(table, SWT.None);
		column_Use.setText("Use");
		TableColumn column_OneWay = new TableColumn(table, SWT.None);
		column_OneWay.setText("One-Way");
		TableColumn column_Action = new TableColumn(table, SWT.None);
		column_Action.setText("Action");

		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new TableViewerLabelProvider());
		tableViewer.setInput(busiSvcInfo);
	}

	private void createHTTPProtocolInfoComposite(ProtocolInService protocolInfo) {
		protocolComposite = toolkit.createComposite(protocolInfoSection, SWT.NONE);
		toolkit.paintBordersFor(protocolComposite);
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

	// 创建冲正条件table
	private void createReversalConditionTable(Composite composite) {
		Label reversalConditionLabel = new Label(composite, SWT.NONE);
		reversalConditionLabel.setText("冲正条件:");
		reversalConditionLabel.setAlignment(SWT.RIGHT);
		reversalConditionLabel.setBounds(0, 266, 61, 17);
		toolkit.adapt(reversalConditionLabel, true, true);

		reversalConditionTable = formToolkit.createTable(composite, SWT.NONE);
		reversalConditionTable.setLinesVisible(true);
		reversalConditionTable.setHeaderVisible(true);
		reversalConditionTable.setBounds(67, 264, 320, 80);
		toolkit.paintBordersFor(reversalConditionTable);

		TableColumn reversalCheckField = new TableColumn(reversalConditionTable, SWT.NONE);
		reversalCheckField.setWidth(118);
		reversalCheckField.setText("冲正检查域");

		TableColumn reversalCheckValue = new TableColumn(reversalConditionTable, SWT.NONE);
		reversalCheckValue.setWidth(118);
		reversalCheckValue.setText("冲正检查值");

		TableColumn reversalConditionDesc = new TableColumn(reversalConditionTable, SWT.NONE);
		reversalConditionDesc.setWidth(120);

		reversalConditionDesc.setText("描述");

		for (ReversalCondition reversalCondition : busiSvcInfo.getReversalConditions()) {
			TableItem tableItem = new TableItem(reversalConditionTable, SWT.NONE);
			tableItem.setText(0, reversalCondition.getRetCode());
			tableItem.setText(1, reversalCondition.getRetValue());
			tableItem.setText(2, reversalCondition.getDesc());
		}

		Button modifyReversalConditionBtn = new Button(composite, SWT.NONE);
		modifyReversalConditionBtn.setText("添加");
		modifyReversalConditionBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		modifyReversalConditionBtn.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseUp(MouseEvent e) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				ReversalConditionAddWizard bsw = new ReversalConditionAddWizard(reversalConditionTable, busiSvcInfo);
				WizardDialog dialog = new WizardDialog(shell, bsw);
				dialog.setTitle("增加冲正条件");
				dialog.open();
				setDirty(true);
				firePropertyChange(PROP_DIRTY);
			}
		});
		modifyReversalConditionBtn.setBounds(407, 260, 60, 27);
		toolkit.adapt(modifyReversalConditionBtn, true, true);

		Button deleteReversalConditionBtn = new Button(composite, SWT.NONE);
		deleteReversalConditionBtn.setText("删除");
		deleteReversalConditionBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		deleteReversalConditionBtn.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseUp(MouseEvent e) {
				reversalConditionTable.remove(reversalConditionTable.getSelectionIndex());
				busiSvcInfo.getReversalConditions().remove(reversalConditionTable.getSelectionIndex());
				setDirty(true);
				firePropertyChange(PROP_DIRTY);
			}
		});
		deleteReversalConditionBtn.setBounds(407, 297, 60, 27);
		toolkit.adapt(deleteReversalConditionBtn, true, true);
	}

	private class ContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof BusiSvcInfo) {
				Operation operation = new Operation(((BusiSvcInfo) inputElement).getOperationName());
				operation.setAction(((BusiSvcInfo) inputElement).getActionName());
				return new Operation[] { operation };
			}
			return new Object[0];
		}
	}

	private class TableViewerLabelProvider implements ITableLabelProvider {
		@Override
		public void addListener(ILabelProviderListener listener) {
		}

		@Override
		public void dispose() {

		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {

		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			String columnText = null;
			if (element instanceof Operation) {
				switch (columnIndex) {
				case 0:
					columnText = ((Operation) element).getName();
					break;
				case 1:
					columnText = ((Operation) element).getUse();
					break;
				case 2:
					columnText = ((Operation) element).getOneWay();
					break;
				case 3:
					columnText = ((Operation) element).getAction();
					break;
				}
			}
			return columnText;
		}

	}

	private class Operation {
		private String name;
		private String use;
		private String oneWay;
		private String action;

		public Operation(String name) {
			super();
			this.name = name;
		}

		@SuppressWarnings("unused")
		public Operation() {
		}

		public String getName() {
			return name;
		}

		public String getUse() {
			return use;
		}

		public String getOneWay() {
			return oneWay;
		}

		public String getAction() {
			return action;
		}

		@SuppressWarnings("unused")
		public void setName(String name) {
			this.name = name;
		}

		@SuppressWarnings("unused")
		public void setUse(String use) {
			this.use = use;
		}

		@SuppressWarnings("unused")
		public void setOneWay(String oneWay) {
			this.oneWay = oneWay;
		}

		public void setAction(String action) {
			this.action = action;
		}

	}

	@Override
	public void setFocus() {

	}

	public String getLocalFileName(String str) {
		str = str.replaceAll("\\\\", "/");
		String[] arr = str.split("/");
		String localFileName = arr[arr.length - 1];
		System.out.println(localFileName);
		return localFileName;
	}

	public String getFileName(String str) {
		String fileName = null;
		String[] arr = str.split(".");
		if (arr.length > 1) {
			fileName = arr[0];
			System.out.println(fileName);
		}
		return fileName;
	}

	public String getFileType(String str) {

		String fileType = "";
		int i = str.lastIndexOf(".");
		String s1 = str.substring(i + 1);
		if (s1.length() != 0) {
			if ("xsd".equals(s1)) {
				fileType = "schema文件";
			}
			if ("wsdl".equals(s1)) {
				fileType = "wsdl文件";
			}
		}
		return fileType;
	}
}
