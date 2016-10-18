package com.dc.bip.ide.editors.protocol;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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

import com.dc.bip.ide.objects.PramsInfo;
import com.dc.bip.ide.objects.ProtocolInService;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.PlatformUtils;
import com.dc.bip.ide.views.objects.ProtocolInNode;
import com.dc.bip.ide.views.objects.ProtocolOutNode;
import com.dc.bip.ide.wizards.protocol.ParamsAddWizard;

public class ProtocolInEditor extends EditorPart implements IEditorPart {
	public ProtocolInEditor() {
	}

	public static final String ID = "ProtocolInTcpEditor"; //$NON-NLS-1$
	private Text text_ptltype;
	private Text text_ptlName;
	private Text text_port;
	private Text text_address;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table table ;
	private ProtocolInService service =new ProtocolInService(); 
	private ProtocolInNode node ;
	private IFile file;
	private Text text_overTime;
	private Text text_maxThreads;
	private Text text_minThreads;
	private Text text_acceptSize;
	private Text text_acceptQueueSize;
	private Text text_readMethod;
	private Text text_writeMethod;
	private String protocolType = "TCP";
	private String titile;
	private List<PramsInfo> list;
	


	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		Section section = formToolkit.createSection(container, Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		section.setBounds(42, 10, 730, 264);
		formToolkit.paintBordersFor(section);
		section.setText("协议基本信息");
		if("TCP".equalsIgnoreCase(protocolType)){
			titile = "高级参数";
			list = service.getParamsModels();
			
			Composite composite = formToolkit.createComposite(section, SWT.NONE);
			composite.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 5, SWT.NORMAL));
			formToolkit.paintBordersFor(composite);
			section.setClient(composite);
			
			Label label = new Label(composite, SWT.NONE);
			label.setBounds(33, 23, 61, 17);
			formToolkit.adapt(label, true, true);
			label.setText("协议类型：");
			
			text_ptltype = new Text(composite, SWT.BORDER);
			text_ptltype.setEditable(false);
			text_ptltype.setBounds(130, 20, 140, 23);
			text_ptltype.setText(service.getProtocolType() == null ? "" : service.getProtocolType());
			formToolkit.adapt(text_ptltype, true, true);
			
			Label label_1 = new Label(composite, SWT.NONE);
			label_1.setBounds(32, 57, 61, 17);
			formToolkit.adapt(label_1, true, true);
			label_1.setText("协议名称：");
			
			text_ptlName = new Text(composite, SWT.BORDER);
			text_ptlName.setEditable(false);
			text_ptlName.setBounds(130, 51, 252, 23);
			text_ptlName.setText(service.getProtocolName() == null ? "" : service.getProtocolName());
			formToolkit.adapt(text_ptlName, true, true);
			
			Label lblIp = new Label(composite, SWT.NONE);
			lblIp.setBounds(33, 90, 61, 17);
			formToolkit.adapt(lblIp, true, true);
			lblIp.setText("端口：");
			
			text_port = new Text(composite, SWT.BORDER);
			text_port.setBounds(130, 80, 150, 23);
			text_port.setText(service.getPort() == null ? "" : service.getPort());
			formToolkit.adapt(text_port, true, true);
			
//			Label label_3 = new Label(composite, SWT.NONE);
//			label_3.setBounds(360, 80, 61, 17);
//			formToolkit.adapt(label_3, true, true);
//			label_3.setText("端口：");
//			
//			text_4 = new Text(composite, SWT.BORDER);
//			text_4.setBounds(432, 77, 167, 23);
//			formToolkit.adapt(text_4, true, true);
			
			Label label_4 = new Label(composite, SWT.NONE);
			label_4.setBounds(33, 113, 61, 17);
			formToolkit.adapt(label_4, true, true);
			label_4.setText("超时时间：");
			
			text_overTime = new Text(composite, SWT.BORDER);
			text_overTime.setBounds(130, 109, 186, 23);
			text_overTime.setText(service.getOverTime() == null ? "" : service.getOverTime());
			formToolkit.adapt(text_overTime, true, true);
			
			Label label_2 = new Label(composite, SWT.NONE);
			label_2.setBounds(33, 145, 61, 17);
			formToolkit.adapt(label_2, true, true);
			label_2.setText("读策略：");
			
			text_readMethod = new Text(composite, SWT.BORDER);
			text_readMethod.setBounds(130, 138, 469, 23);
			text_readMethod.setText(service.getReadMethod() == null ? "" : service.getReadMethod());
			formToolkit.adapt(text_readMethod, true, true);
			
			Label lblNewLabel = new Label(composite, SWT.NONE);
			lblNewLabel.setBounds(33, 193, 61, 17);
			formToolkit.adapt(lblNewLabel, true, true);
			lblNewLabel.setText("写策略:");
			
			Label lblNewLabel_1 = new Label(composite, SWT.NONE);
			lblNewLabel_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 8, SWT.NORMAL));
			lblNewLabel_1.setBounds(130, 167, 469, 17);
			formToolkit.adapt(lblNewLabel_1, true, true);
			lblNewLabel_1.setText("Format:(length:(unknow)|(s=num1,e=num2)|num))|(sign:string))");
			
			text_writeMethod = new Text(composite, SWT.BORDER);
			text_writeMethod.setBounds(130, 190, 469, 23);
			text_writeMethod.setText(service.getWriteMethod() == null ? "" : service.getWriteMethod());
			formToolkit.adapt(text_writeMethod, true, true);
			
			Label lblNewLabel_2 = new Label(composite, SWT.NONE);
			lblNewLabel_2.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 8, SWT.NORMAL));
			lblNewLabel_2.setBounds(130, 221, 469, 17);
			formToolkit.adapt(lblNewLabel_2, true, true);
			lblNewLabel_2.setText("Format:(length:(unknow)|(s=num1,e=num2)|num))|(sign:string))");
			
			Label lblNewLabel_3 = new Label(composite, SWT.NONE);
			lblNewLabel_3.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 8, SWT.NORMAL));
			lblNewLabel_3.setBounds(322, 113, 61, 17);
			formToolkit.adapt(lblNewLabel_3, true, true);
			lblNewLabel_3.setText("单位：毫秒");
		}else{
			titile = "自定义报文头";
			list = service.getHeadMsgDef();
			
			Composite composite = formToolkit.createComposite(section, SWT.NONE);
			composite.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 5, SWT.NORMAL));
			formToolkit.paintBordersFor(composite);
			section.setClient(composite);
			
			Label label = new Label(composite, SWT.NONE);
			label.setBounds(33, 23, 61, 17);
			formToolkit.adapt(label, true, true);
			label.setText("协议类型：");
			
			text_ptltype = new Text(composite, SWT.BORDER);
			text_ptltype.setEditable(false);
			text_ptltype.setBounds(130, 20, 140, 23);
			text_ptltype.setText(service.getProtocolType() == null ? "" : service.getProtocolType());
			formToolkit.adapt(text_ptltype, true, true);
			
			Label label_1 = new Label(composite, SWT.NONE);
			label_1.setBounds(32, 57, 61, 17);
			formToolkit.adapt(label_1, true, true);
			label_1.setText("协议名称：");
			
			text_ptlName = new Text(composite, SWT.BORDER);
			text_ptlName.setEditable(false);
			text_ptlName.setBounds(130, 51, 252, 23);
			text_ptlName.setText(service.getProtocolName() == null ? "" : service.getProtocolName());
			formToolkit.adapt(text_ptlName, true, true);
			
			Label lblIp = new Label(composite, SWT.NONE);
			lblIp.setBounds(33, 90, 61, 17);
			formToolkit.adapt(lblIp, true, true);
			lblIp.setText("地址：");
			
			text_address = new Text(composite, SWT.BORDER);
			text_address.setBounds(130, 80, 364, 23);
			text_address.setText(service.getUrl() == null ? "" : service.getUrl());
			formToolkit.adapt(text_address, true, true);
			
			Label lblFormathttpjmshostportcontext = new Label(composite, SWT.NONE);
			lblFormathttpjmshostportcontext.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 8, SWT.NORMAL));
			lblFormathttpjmshostportcontext.setText("Format:(http|jms)://host:port/context");
			lblFormathttpjmshostportcontext.setBounds(500, 84, 208, 17);
			formToolkit.adapt(lblFormathttpjmshostportcontext, true, true);
			
			Label label_4 = new Label(composite, SWT.NONE);
			label_4.setBounds(33, 113, 61, 17);
			formToolkit.adapt(label_4, true, true);
			label_4.setText("超时时间：");
			
			
			text_overTime = new Text(composite, SWT.BORDER);
			text_overTime.setBounds(130, 109, 186, 23);
			text_overTime.setText(service.getOverTime() == null ? "" : service.getOverTime());
			formToolkit.adapt(text_overTime, true, true);
			
			Label label_2 = new Label(composite, SWT.NONE);
			label_2.setBounds(33, 149, 61, 17);
			formToolkit.adapt(label_2, true, true);
			label_2.setText("通信协议：");
			
			Label lblNewLabel_3 = new Label(composite, SWT.NONE);
			lblNewLabel_3.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 8, SWT.NORMAL));
			lblNewLabel_3.setBounds(322, 113, 61, 17);
			formToolkit.adapt(lblNewLabel_3, true, true);
			lblNewLabel_3.setText("单位：毫秒");
			
			Combo combo = new Combo(composite, SWT.NONE);
			combo.setItems(new String[] {"HTTP1.0", "HTTP2.0"});
			combo.setBounds(130, 149, 113, 25);
			
			formToolkit.adapt(combo);
			formToolkit.paintBordersFor(combo);
			combo.setText(service.getContactProtocolType() == null ? "" : service.getContactProtocolType());
			combo.addModifyListener(new ModifyListener(){
				@Override
				public void modifyText(ModifyEvent arg0) {
					Combo combo = (Combo)arg0.widget;
					service.setContactProtocolType(combo.getText());
				}
					
				});
		}
		
		Section sctnNewSection = formToolkit.createSection(container, Section.TWISTIE | Section.TITLE_BAR);
		sctnNewSection.setBounds(41, 280, 328, 148);
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText("线程池信息");
		sctnNewSection.setExpanded(true);
		
		Composite composite_1 = formToolkit.createComposite(sctnNewSection, SWT.NONE);
		formToolkit.paintBordersFor(composite_1);
		sctnNewSection.setClient(composite_1);
		
		Label label_5 = new Label(composite_1, SWT.NONE);
		label_5.setBounds(33, 10, 77, 17);
		formToolkit.adapt(label_5, true, true);
		label_5.setText("最大线程数：");
		
		text_maxThreads = new Text(composite_1, SWT.BORDER);
		text_maxThreads.setBounds(139, 3, 98, 23);
		text_maxThreads.setText(service.getMaxThreads() == null ? "" : service.getMaxThreads());
		formToolkit.adapt(text_maxThreads, true, true);
		
		Label label_6 = new Label(composite_1, SWT.NONE);
		label_6.setBounds(32, 35, 69, 17);
		formToolkit.adapt(label_6, true, true);
		label_6.setText("最小线程数：");
		
		text_minThreads = new Text(composite_1, SWT.BORDER);
		text_minThreads.setBounds(139, 31, 99, 23);
		text_minThreads.setText(service.getMinThreads() == null ? "" : service.getMinThreads());
		formToolkit.adapt(text_minThreads, true, true);
		
		Label label_7 = new Label(composite_1, SWT.NONE);
		label_7.setBounds(33, 63, 84, 17);
		formToolkit.adapt(label_7, true, true);
		label_7.setText("处理队列个数：");
		
		text_acceptSize = new Text(composite_1, SWT.BORDER);
		text_acceptSize.setBounds(140, 60, 98, 23);
		text_acceptSize.setText(service.getAcceptSize() == null ? "" : service.getAcceptSize());
		formToolkit.adapt(text_acceptSize, true, true);
		
		Label label_8 = new Label(composite_1, SWT.NONE);
		label_8.setBounds(33, 94, 92, 17);
		formToolkit.adapt(label_8, true, true);
		label_8.setText("处理队列长度：");
		
		text_acceptQueueSize = new Text(composite_1, SWT.BORDER);
		text_acceptQueueSize.setBounds(140, 91, 98, 23);
		text_acceptQueueSize.setText(service.getAcceptQueueSize() == null ? "" : service.getAcceptQueueSize());
		formToolkit.adapt(text_acceptQueueSize, true, true);
		
		Section sctnNewSection_1 = formToolkit.createSection(container, Section.TWISTIE | Section.TITLE_BAR);
		sctnNewSection_1.setBounds(426, 280, 336, 142);
		formToolkit.paintBordersFor(sctnNewSection_1);
		sctnNewSection_1.setText(titile);
		sctnNewSection_1.setExpanded(true);
		
		Composite composite_2 = formToolkit.createComposite(sctnNewSection_1, SWT.NONE);
		formToolkit.paintBordersFor(composite_2);
		sctnNewSection_1.setClient(composite_2);
		
		table = formToolkit.createTable(composite_2, SWT.NONE);
		table.setBounds(10, 10, 214, 83);
		formToolkit.paintBordersFor(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn reversalCheckField = new TableColumn(table, SWT.NONE);
		reversalCheckField.setWidth(100);
		reversalCheckField.setText("KEY");

		TableColumn reversalCheckValue = new TableColumn(table, SWT.NONE);
		reversalCheckValue.setWidth(111);
		reversalCheckValue.setText("VALUE");
		
		for (PramsInfo paramInfo : list) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(0, paramInfo.getParamKey());
			tableItem.setText(1, paramInfo.getParamValue());
		}
		
		Button button_add = formToolkit.createButton(composite_2, "添加", SWT.NONE);
		button_add.setBounds(234, 10, 80, 27);
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
				ParamsAddWizard bsw = new ParamsAddWizard(table, service);
				WizardDialog dialog = new WizardDialog(shell, bsw);
				dialog.setTitle("增加"+titile);
				dialog.open();
				firePropertyChange(PROP_DIRTY);
			}
		});
		
		Button button_delete = new Button(composite_2, SWT.NONE);
		button_delete.setBounds(234, 66, 80, 27);
		formToolkit.adapt(button_delete, true, true);
		button_delete.setText("删除");
		button_delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
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
				if(index >= 0){
					table.remove(index);
					list.remove(list.get(index));
					firePropertyChange(PROP_DIRTY);
				}
			}
		});
		
		Button button_save = formToolkit.createButton(container, "保存", SWT.NONE);
		button_save.setBounds(289, 434, 80, 27);
		button_save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		button_save.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {

			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseUp(MouseEvent e) {
				String overTime = text_overTime.getText();
				String maxThread = text_maxThreads.getText();
				String minThread = text_minThreads.getText();
				String accptSize = text_acceptSize.getText();
				String accptQueueSize = text_acceptQueueSize.getText();
				if(service.getProtocolType().equalsIgnoreCase("TCP")){	
					String port = text_port.getText();				
					String readMethod = text_readMethod.getText();
					String writeMethod = text_writeMethod.getText();
					service.setReadMethod(readMethod);
					service.setWriteMethod(writeMethod);
					
					service.setPort(port);
				}else{
					String url = text_address.getText();
					service.setUrl(url);
				}
				service.setOverTime(overTime);
				service.setMaxThreads(maxThread);
				service.setMinThreads(minThread);
				service.setAcceptSize(accptSize);
				service.setAcceptQueueSize(accptQueueSize);
				ProtocolInNode node = new ProtocolInNode(service.getProtocolName());
				node.setProtocolInService(service);
				HelpUtil.writeObject(node, file);
				firePropertyChange(PROP_DIRTY);
				PlatformUtils.showInfo("保存", "保存成功!");
			}

		});
		
		
		
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
			//super.init(site, input);
			FileEditorInput i = (FileEditorInput) input;
			IFile resource = i.getFile();
			freader = new FileInputStream(resource.getLocation().toFile());
			objectInputStream = new ObjectInputStream(freader);
			node = (ProtocolInNode) objectInputStream.readObject();
			file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(node.getProtocolInService().getiFilePath()));
			this.setPartName(node.getNodeName());
			service = node.getProtocolInService();
			protocolType =node.getProtocolInService().getProtocolType();
			setPartName(service.getProtocolName());
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

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}
	

			
}
