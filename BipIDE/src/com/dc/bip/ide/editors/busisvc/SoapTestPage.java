package com.dc.bip.ide.editors.busisvc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import com.dc.bip.ide.editors.util.postData.PostDataUtil;
import com.dc.bip.ide.objects.BusiSvcInfo;
/**
 * 
 * Copyright：2016-DCITS  
 * Project name：BipIDE     
 *  
 * Class decription：  组合服务调用测试页面
 * Class name：com.dc.bip.ide.editors.busisvc.SoapTestPage       
 * Author：Sure-xujian  
 * Create date：2016年7月7日 上午9:18:52
 */
public class SoapTestPage extends Composite{
	
	private BusiSvcInfo busiInfo;
	private Text textTop;	
	private Text textReq;
	private Text textResp;
	private String reqInit;//初始自动生成的请求参数
	private String reqContent;//最终发送的请求参数内容
	private String respData;//向指定webservice地址发送数据后获取的返回
	private String endpoint;//初始url
	private String endUrl;//最终要发送数据的webservice接口地址
	
	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	
	public SoapTestPage(Composite parent, int style,BusiSvcInfo busiSvcInfo){
		super(parent, SWT.NONE);
		this.busiInfo = busiSvcInfo;
		reqInit = busiInfo.getReqMsg();
		busiInfo.getResMsg();
		endpoint = busiInfo.getEndpoint();		
        setLayout(new FillLayout(SWT.HORIZONTAL));
       
        ScrolledForm scrldfrmNewScrolledform = formToolkit.createScrolledForm(this);
		scrldfrmNewScrolledform.setAlwaysShowScrollBars(true);
		scrldfrmNewScrolledform.setExpandVertical(true);
		scrldfrmNewScrolledform.setExpandHorizontal(true);
		scrldfrmNewScrolledform.setMinHeight(400);
		scrldfrmNewScrolledform.setMinWidth(400);
		formToolkit.paintBordersFor(scrldfrmNewScrolledform);
		
		//顶部可编辑地址栏的设置
		Section sctTopSection = formToolkit.createSection(scrldfrmNewScrolledform.getBody(), Section.TITLE_BAR);
		sctTopSection.setBounds(10, 10, 1100, 65);
		formToolkit.paintBordersFor(sctTopSection);
		sctTopSection.setText("请求地址");
		
		Composite composite_top = formToolkit.createComposite(sctTopSection, SWT.None);
		formToolkit.paintBordersFor(composite_top);
		sctTopSection.setClient(composite_top);
		
		textTop = formToolkit.createText(composite_top, "",SWT.BORDER |  SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_cmdIntro_top = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_cmdIntro_top.widthHint = 10; // 必须得设置宽度，否则自动换行不好使
		gd_cmdIntro_top.heightHint = 60;
		textTop.setLayoutData(gd_cmdIntro_top);
		textTop.setBounds(10,10,980,25);
		textTop.setText(endpoint);
		
		//顶部发送按钮的设置
		Button btnPostButton = formToolkit.createButton(composite_top, "发送测试", SWT.NONE);
        btnPostButton.setBounds(1000, 10, 80, 25);
        btnPostButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {

			}
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			@Override
			public void mouseUp(MouseEvent e) {
				reqContent = textReq.getText();
				endUrl = textTop.getText();
				try {
					respData = PostDataUtil.postXmlToWebservice(endUrl, reqContent);
				} catch (Exception ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				textResp.setText(respData);
			}
		});
		
		//请求报文部分的设置
		Section sctReqSection = formToolkit.createSection(scrldfrmNewScrolledform.getBody(), Section.TITLE_BAR);
		sctReqSection.setBounds(10, 90, 500, 500);
		formToolkit.paintBordersFor(sctReqSection);
		sctReqSection.setText("请求报文");
		
		Composite composite_req = formToolkit.createComposite(sctReqSection, SWT.NONE);
		formToolkit.paintBordersFor(composite_req);
		sctReqSection.setClient(composite_req);
		
		textReq = formToolkit.createText(composite_req,"", SWT.BORDER |  SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_cmdIntro_req = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
		        1);
		gd_cmdIntro_req.widthHint = 10; // 必须得设置宽度，否则自动换行不好使
		gd_cmdIntro_req.heightHint = 60;
		textReq.setLayoutData(gd_cmdIntro_req);
        textReq.setBounds(10, 10, 473, 400);
        textReq.setText(reqInit == null ? "" : reqInit);
        
        //响应报文
        Section sctRespSection = formToolkit.createSection(scrldfrmNewScrolledform.getBody(), Section.TITLE_BAR);
		sctRespSection.setBounds(600, 90, 500, 500);
		formToolkit.paintBordersFor(sctRespSection);
		sctRespSection.setText("响应报文");
		
		Composite composite_resp = formToolkit.createComposite(sctRespSection, SWT.NONE);
		formToolkit.paintBordersFor(composite_resp);
		sctRespSection.setClient(composite_resp);
		textResp = formToolkit.createText(composite_resp,"", SWT.BORDER |  SWT.WRAP
		        | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_cmdIntro_resp = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
		        1);
		gd_cmdIntro_resp.widthHint = 10; // 必须得设置宽度，否则自动换行不好使
		gd_cmdIntro_resp.heightHint = 60;
		textResp.setLayoutData(gd_cmdIntro_resp);
		textResp.setBounds(10, 10, 473, 400);
		
	}
}
