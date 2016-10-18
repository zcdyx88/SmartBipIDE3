package com.dc.bip.ide.editors.busisvc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import com.dc.bip.ide.editors.util.postData.PostDataUtil;
import com.dc.bip.ide.editors.util.server.JettyServer;
import com.dc.bip.ide.objects.BusiSvcInfo;
/**
 * 
 * Copyright：2016-DCITS  
 * Project name：BipIDE     
 *  
 * Class decription：  本地SOAP服务的相关服务页面
 * Class name：com.dc.bip.ide.editors.busisvc.LocalSoapPage       
 * Author：Sure-xujian  
 * Create date：2016年7月8日 下午4:51:40
 */
public class LocalSoapPage extends Composite{
	private BusiSvcInfo busiInfo;
	private Text textPort;
	private Text textMapping;
	private Text textLocalUrl;
	private Text textStandardContentResp;	
	private Text textReq;
	private Text textResp;
	private String serviceId;
	private String reqInit;//初始自动生成的请求参数
	private String reqContent;//最终发送的请求参数内容
	private String respData;//向指定webservice地址发送数据后获取的返回	
	private JettyServer server;
	private volatile Map<String,String> configMap;
	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	
	public LocalSoapPage(Composite parent, int style,BusiSvcInfo busiSvcInfo){
		super(parent, SWT.NONE);
		this.busiInfo = busiSvcInfo;
		serviceId = busiInfo.getId();
		reqInit = busiInfo.getReqMsg();
		final String destPath = System.getProperty("user.dir")+File.separator+"BipIDERespFiles";
		final String fileName = serviceId+"resp.txt";
        setLayout(new FillLayout(SWT.HORIZONTAL));
       
        ScrolledForm scrldfrmNewScrolledform = formToolkit.createScrolledForm(this);
		scrldfrmNewScrolledform.setAlwaysShowScrollBars(true);
		scrldfrmNewScrolledform.setExpandVertical(true);
		scrldfrmNewScrolledform.setExpandHorizontal(true);
		scrldfrmNewScrolledform.setMinHeight(400);
		scrldfrmNewScrolledform.setMinWidth(400);
		formToolkit.paintBordersFor(scrldfrmNewScrolledform);
		
		//上部本地SOAP服务的设置
        Section sctSoapSection = formToolkit.createSection(scrldfrmNewScrolledform.getBody(), Section.TITLE_BAR);
        sctSoapSection.setBounds(10,10,1140,65);
        formToolkit.paintBordersFor(sctSoapSection);
        sctSoapSection.setText("本地SOAP服务");
        
        Composite composite_soap = formToolkit.createComposite(sctSoapSection, SWT.NONE);
        formToolkit.paintBordersFor(composite_soap);
        sctSoapSection.setClient(composite_soap);
        
        Label labelPort = formToolkit.createLabel(composite_soap, "服务端口", SWT.NONE);
        labelPort.setBounds(10, 10, 50, 25);
        
        textPort = formToolkit.createText(composite_soap, "",SWT.BORDER |  SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		setTextContentStyle(textPort);
		textPort.setBounds(70,10,70,25);
        
		Label labelMapping = formToolkit.createLabel(composite_soap, "映射路径", SWT.NONE);
		labelMapping.setBounds(150,10,50,25);
		
		textMapping = formToolkit.createText(composite_soap,"", SWT.BORDER |  SWT.WRAP | SWT.V_SCROLL | SWT.MULTI );
		setTextContentStyle(textMapping);
		textMapping.setBounds(210,10,180,25);
		
		Label labelLocalUrl = formToolkit.createLabel(composite_soap, "本地地址", SWT.NONE);
		labelLocalUrl.setBounds(400,10,50,25);
		
		textLocalUrl = formToolkit.createText(composite_soap, "", SWT.BORDER |  SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		setTextContentStyle(textLocalUrl);
		textLocalUrl.setBounds(460,10,300,25);
		
		
		//启动本地SOAP服务的button
		Button btnStartButton = formToolkit.createButton(composite_soap, "启动SOAP服务", SWT.NONE);
		btnStartButton.setBounds(770,10,100,25);
		
		btnStartButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				String port = textPort.getText();
				String context = textMapping.getText();
				
				configMap = new HashMap<String,String>();
				//设置服务参数
				configMap.put("port", port);
				configMap.put("contextPath", context);//configuration.setConfig("contextPath", context);
				configMap.put("minThreads","10");//configuration.setConfig("minThreads","10");
				configMap.put("maxThreads","30");//configuration.setConfig("maxThreads","30");
				configMap.put("acceptQueueSize", "10");//configuration.setConfig("acceptQueueSize", "10");
				configMap.put("acceptSize", "2");
				
				String localUrl = "http://127.0.0.1:"+port+context;
				textLocalUrl.setText(localUrl);
				//启动服务
				server = new JettyServer(configMap);
				server.start();
				
			}
			
			});
		
		//停止本地SOAP服务的button
		Button btnStopButton = formToolkit.createButton(composite_soap, "停止SOAP服务", SWT.NONE);
        btnStopButton.setBounds(880,10,100,25);
        btnStopButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				server.stop();			
			}});
		
        //本地测试按钮
        Button btnTestButton = formToolkit.createButton(composite_soap, "本地测试", SWT.NONE);
        btnTestButton.setBounds(990,10,100,25);
        btnTestButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				reqContent = textStandardContentResp.getText();
				String localUrl = textLocalUrl.getText();
				try {
					respData = PostDataUtil.postXmlToWebservice(localUrl, reqContent);
					textResp.setText(respData);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
        	
        });
        
        //标准返回报文部分的设置
  		Section sctStandardRespSection = formToolkit.createSection(scrldfrmNewScrolledform.getBody(), Section.TITLE_BAR);
  		sctStandardRespSection.setBounds(10, 90, 360, 450);
  		formToolkit.paintBordersFor(sctStandardRespSection);
  		sctStandardRespSection.setText("标准返回");
  		
  		Composite composite_standard_resp = formToolkit.createComposite(sctStandardRespSection, SWT.NONE);
  		formToolkit.paintBordersFor(composite_standard_resp);
  		sctStandardRespSection.setClient(composite_standard_resp);
  		
  		textStandardContentResp = formToolkit.createText(composite_standard_resp,"", SWT.BORDER |  SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
  		setTextContentStyle(textStandardContentResp);
  		textStandardContentResp.setBounds(5, 10, 350, 360);
  		
  		//如果存在标准返回的保存文件，那么读取
  		StringBuilder stringBuilder = new StringBuilder();
  		File file = new File(destPath+File.separator+fileName);
  		if(file.exists()){
  			BufferedReader bufferedReader = null;
  			char[] buf = null;
  			try {
  				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
				int length = 1024*8;
				buf = new char[length];
				while(bufferedReader.read(buf,0,length)!=-1){
					stringBuilder.append(new String(buf));
					buf = null;
					buf = new char[length];
				}
				textStandardContentResp.setText(stringBuilder.toString());				
			} catch (Exception fileException) {
				fileException.printStackTrace();
			} finally{
				if(bufferedReader!=null){
					try {
						bufferedReader.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
  		}
  		
  		//保存标准返回到本地文件
  		Button btnSaveStandardRespButton = formToolkit.createButton(composite_standard_resp, "保存标准返回", SWT.NONE);
  		btnSaveStandardRespButton.setBounds(5,380,80,25);
  		btnSaveStandardRespButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				
				String standardContentResp = textStandardContentResp.getText();
				FileOutputStream fileOutputStream = null;
				File fileDir = new File(destPath);
				if(fileDir.exists()==false){
					fileDir.mkdirs();
				}
				try {
					File file = new File(destPath,fileName);
					if(file.exists()==false){
						file.createNewFile();
					}
					fileOutputStream = new FileOutputStream(file);//destPath+File.separator+fileName);
					fileOutputStream.write(standardContentResp.getBytes());
				} catch (Exception ioException) {
					ioException.printStackTrace();
					System.out.println(ioException.getMessage());
				} finally{
					if(fileOutputStream!=null){
						try {
							fileOutputStream.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
  			
  		});
  		
  		//请求报文的设置
  		Section sctReqSection = formToolkit.createSection(scrldfrmNewScrolledform.getBody(), Section.TITLE_BAR);
		sctReqSection.setBounds(380, 90, 360, 450);
		formToolkit.paintBordersFor(sctReqSection);
		sctReqSection.setText("请求报文");

		Composite composite_req = formToolkit.createComposite(sctReqSection, SWT.NONE);
		formToolkit.paintBordersFor(composite_req);
		sctReqSection.setClient(composite_req);	
		
		textReq = formToolkit.createText(composite_req,"", SWT.BORDER |  SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		setTextContentStyle(textReq);
        textReq.setBounds(5, 10, 350, 360);
        textReq.setText(reqInit==null ? "" : reqInit);
        
        //响应报文的设置
        Section sctRespSection = formToolkit.createSection(scrldfrmNewScrolledform.getBody(), Section.TITLE_BAR);
		sctRespSection.setBounds(750, 90, 360, 450);
		formToolkit.paintBordersFor(sctRespSection);
		sctRespSection.setText("响应报文");
		
		Composite composite_resp = formToolkit.createComposite(sctRespSection, SWT.NONE);
		formToolkit.paintBordersFor(composite_resp);
		sctRespSection.setClient(composite_resp);
		textResp = formToolkit.createText(composite_resp,"", SWT.BORDER |  SWT.WRAP| SWT.V_SCROLL | SWT.MULTI);
		setTextContentStyle(textResp);
		textResp.setBounds(5, 10, 350, 360);
		
	}
	
	private void setTextContentStyle(Text text){
		GridData gd_cmdIntro = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_cmdIntro.widthHint = 10; // 必须得设置宽度，否则自动换行不好使
		gd_cmdIntro.heightHint = 60;
		text.setLayoutData(gd_cmdIntro);
	}
	
}
