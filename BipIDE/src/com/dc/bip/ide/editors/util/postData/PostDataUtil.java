package com.dc.bip.ide.editors.util.postData;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 
 * Copyright：2016-DCITS  
 * Project name：BipIDE     
 *  
 * Class decription：  
 * Class name：com.dc.bip.ide.editors.util.postData.PostDataUtil       
 * Author：Sure-xujian  
 * Create date：2016年7月6日 下午3:37:44
 */
public class PostDataUtil {
	/**
	 * 
	 * @Title: postXmlToWebservice  
	 * @Description: 向webservice接口以post形式发送xml数据并获取数据返回  
	 * @param url
	 * @param postData
	 * @return
	 */
	public static String postXmlToWebservice(String url,String postData) throws Exception{
		String respData = null;
		//System.out.println(postData);
		HttpPost httpPost = new HttpPost(url);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		try{
			HttpEntity he = new StringEntity(postData,HTTP.UTF_8);
			httpPost.setHeader("Content-Type","application/soap+xml; charset=utf-8");
			httpPost.setEntity(he);
			HttpResponse hp = httpClient.execute(httpPost);
			respData = EntityUtils.toString(hp.getEntity());
		}catch(Exception e){
			respData = e.getMessage();//"Invoking exception,please review your url or code's problems";
		}
		/*PostMethod postMethod = new PostMethod(url);
		byte[] bt = postData.getBytes("utf-8");
		InputStream inputStream = new ByteArrayInputStream(bt,0,bt.length);
		RequestEntity re = new InputStreamRequestEntity(inputStream,bt.length,"application/xml;charset=utf-8");
		postMethod.setRequestEntity(re);
		
		//生成一个httpclient，发出postmethod请求
		HttpClient httpClient = new HttpClient();
		int statusCode = httpClient.executeMethod(postMethod);
		if(statusCode==200){
			System.out.println("调用成功");
			respData = postMethod.getResponseBodyAsString();
		}else{
			System.out.println("调用失败！ 错误码："+statusCode);
			respData = postMethod.getResponseBodyAsString();
		}*/
		
		return respData;
		
	}
	
}
