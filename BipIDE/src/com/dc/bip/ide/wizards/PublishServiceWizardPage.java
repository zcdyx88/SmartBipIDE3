package com.dc.bip.ide.wizards;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.dc.bip.ide.objects.BipServer;
import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.views.objects.BipServerNode;

public class PublishServiceWizardPage extends WizardPage implements IWizardPage {
	private ComboViewer comboViewer;
	private BipServer serverInfo;
	
	//TODO 该服务器列表需要提供，还需要提供编辑页面
    private List<BipServer> serverList = new ArrayList<BipServer>();
    private Text text_IP;
    private Text text_jmxPort;
    private Text text_regPort;
    
	protected PublishServiceWizardPage(String pageName,String projectName) {
		super(pageName);
		setMessage("\u8BF7\u9009\u62E9\u53D1\u5E03\u670D\u52A1\u5668");
		setTitle("\u670D\u52A1\u53D1\u5E03");
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		root.getProject(projectName);
		String bipserverPath = root.getProject(projectName).getFolder(BipConstantUtil.ServerPath).getLocation().toString();
  		File bipserverFolder = new File(bipserverPath);
  		FilenameFilter bipserverfileNameFilter = new FilenameFilter() {			   
 	            @Override
 	            public boolean accept(File dir, String name) {
 	               if(name.lastIndexOf('.')>0)
 	               {
 	                  int lastIndex = name.lastIndexOf('.');
 	                  String str = name.substring(lastIndex);
 	                  if(str.equals(".bipserver"))
 	                  {
 	                     return true;
 	                  }
 	               }
 	               return false;
 	            }
 	    };	
 	    File[] bipservers = bipserverFolder.listFiles(bipserverfileNameFilter);
 	    /*add by wanming 20160808 start*/
 	    if (bipservers == null || bipservers.length==0){
 	    	setMessage("\u672a\u521b\u5efa\u53d1\u5e03\u670d\u52a1\u5668\uff01");
 	    	return;
 	    }
 	    /*add end*/
 	    for (File file:bipservers) {
 	    	BipServer bipServer = HelpUtil.readBipServerObject(file);
 	    	serverList.add(bipServer);
		}
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		
		Label lblJar = new Label(container, SWT.NONE);
		lblJar.setText("\u53D1\u5E03\u670D\u52A1\u5668\uFF1A");
		lblJar.setBounds(90, 28, 69, 17);
		
		comboViewer = new ComboViewer(container, SWT.NONE);
		Combo combo = comboViewer.getCombo();
		combo.setBounds(165, 25, 302, 25); 
		
		for(int i = 0;i<serverList.size();i++)
		{
			combo.add(serverList.get(i).getId(),i);
			combo.setData(String.valueOf(i), serverList.get(i));
		}
		combo.select(0);
		combo.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo tmpCombo = comboViewer.getCombo();
				serverInfo =(BipServer) tmpCombo.getData(String.valueOf(tmpCombo.getSelectionIndex()));
       			text_IP.setText(serverInfo.getIp());
       			text_jmxPort.setText(String.valueOf(serverInfo.getJmxport()));
       			text_regPort.setText(String.valueOf(serverInfo.getRegport()));       			
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		Group group = new Group(container, SWT.NONE);
		group.setText("\u670D\u52A1\u5668\u4FE1\u606F");
		group.setBounds(78, 74, 389, 146);
		
		Label lblIp = new Label(group, SWT.NONE);
		lblIp.setAlignment(SWT.CENTER);
		lblIp.setBounds(21, 28, 61, 17);
		lblIp.setText("IP:");
		
		Label lblJmxport = new Label(group, SWT.NONE);
		lblJmxport.setAlignment(SWT.CENTER);
		lblJmxport.setBounds(21, 61, 61, 17);
		lblJmxport.setText("JmxPort:");
		
		Label lblRegport = new Label(group, SWT.NONE);
		lblRegport.setAlignment(SWT.CENTER);
		lblRegport.setBounds(21, 98, 61, 17);
		lblRegport.setText("RegPort:");
		
		text_IP = new Text(group, SWT.BORDER);
		text_IP.setEditable(false);
		text_IP.setBounds(95, 25, 284, 23);
		
		text_jmxPort = new Text(group, SWT.BORDER);
		text_jmxPort.setEditable(false);
		text_jmxPort.setBounds(95, 58, 284, 23);
		
		text_regPort = new Text(group, SWT.BORDER);
		text_regPort.setEditable(false);
		text_regPort.setBounds(95, 95, 284, 23);
		
		/*add by wanming 20160808 start*/
		if (serverList.size()>0)
		/*add end*/
		initServerInfo();
	}
	
	public void initServerInfo(){
		Combo tmpCombo = comboViewer.getCombo();
		serverInfo =(BipServer) tmpCombo.getData(String.valueOf(tmpCombo.getSelectionIndex()));
			text_IP.setText(serverInfo.getIp());
			text_jmxPort.setText(String.valueOf(serverInfo.getJmxport()));
			text_regPort.setText(String.valueOf(serverInfo.getRegport()));       		
	}
	
	public BipServer getBipServer() {
		return serverInfo;
	}
	public String getIP() {
		return text_IP.getText();
	}
	public String getJmxPort() {
		return text_jmxPort.getText();
	}
	public String getRegPort() {
		return text_regPort.getText();
	}
	@Override  
    public boolean isPageComplete() { 
	  /*   if(null != comboViewer.getCombo().getText() && comboViewer.getCombo().getText().length()>0)
	     {
	    	 return true;
	     }
        return false;  */
		return true;
    }  
}
