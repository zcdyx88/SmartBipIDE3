package com.dc.bip.ide.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.reficio.ws.builder.SoapBuilder;
import org.reficio.ws.builder.SoapOperation;
import org.reficio.ws.builder.core.Wsdl;
//import org.reficio.ws.client.core.SoapClient;

import com.dc.bip.ide.views.TreeView;

public class SoapWsUtil {
	private static Log log = LogFactory.getLog(SoapWsUtil.class);

	private static SoapWsUtil instance;

	// private static final String filePath = System.getProperty("user.dir");
	private static final String configPath = System.getProperty("user.dir");// 配置文件路径

	public static SoapWsUtil getInstance() {
		if (instance == null) {
			instance = new SoapWsUtil();
		}
		return instance;
	}

	/**
	 * 根据wsdl地址,服务码，场景码生成请求报文
	 * 
	 * @return
	 */
	public String getRequstStr(String url, String serviceId, String opId) {
		Wsdl parser = Wsdl.parse(url);
		String name = "{http://esb.spdbbiz.com/services/" + serviceId + "/wsdl}ESBServerSoapBinding";
		SoapBuilder soapBuilder = parser.binding().name(name).find();
		SoapOperation operation = soapBuilder.operation().name(opId).find();
		String request = soapBuilder.buildInputMessage(operation);
		return request;
	}

	public String getResponseStr(String url, String serviceId, String opId) {
		Wsdl parser = Wsdl.parse(url);
		String name = "{http://esb.spdbbiz.com/services/" + serviceId + "/wsdl}ESBServerSoapBinding";
		SoapBuilder soapBuilder = parser.binding().name(name).find();
		SoapOperation operation = soapBuilder.operation().name(opId).find();
		String str = soapBuilder.buildOutputMessage(operation);
		
		return str;
	}

	public void createXmlFile(String url) {
		// Wsdl parser = Wsdl.parse(url);
		// String name=
		// "{http://esb.spdbbiz.com/services//wsdl}ESBServerSoapBinding";
	}

	public Map<String, Map<String, String>> getSoapMessageList(String url, String serviceId) {

		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

		Wsdl parser = Wsdl.parse(url);
		String name = "{http://esb.spdbbiz.com/services/" + serviceId + "/wsdl}ESBServerSoapBinding";
		SoapBuilder soapBuilder = parser.binding().name(name).find();
		List<SoapOperation> opList = soapBuilder.getOperations();
		for (SoapOperation operation : opList) {
			String operationName = operation.getOperationName();
			Map<String, String> messages = new HashMap<String, String>();
			map.put(operationName, messages);

			String inStr = soapBuilder.buildInputMessage(operation);
			messages.put("request", inStr);

			String outStr = soapBuilder.buildOutputMessage(operation);
			messages.put("request", outStr);

			String operationId = operationName.length() > 2
					? operationName.substring(operationName.length() - 2, operationName.length()) : operationName;
			createConfigFile(serviceId, operationId, inStr, outStr);

		}

		return map;
	}

	/**
	 * 根据wsdl地址获取所有报文
	 * 
	 * @param url
	 */
	public Map<String, Map<String, String>> getSoapMessageList(String url) {
		String serviceId = "";
		String[] strs = url.split("/");
		String str = strs[strs.length - 1];
		if (StringUtils.isNotEmpty(str)) {
			if (str.contains(".")) {
				serviceId = str.substring(0, str.indexOf("."));
			}
		}

		return getSoapMessageList(url, serviceId);

	}
	
	/**
	 * 生成sda文件
	 * @param parser
	 * @param basePath
	 * @param bindingName
	 * @param opeartionName
	 */
	public String generateSerDefFile(String basePath, String serviceId, String opId, 
			String reqMsg, String resMsg,String folderPath){
		String filePath = basePath + File.separator + serviceId + opId + File.separator + "service_" + serviceId + opId + ".xml";
		String fileIpath = folderPath + "/" + serviceId + "/" + "service_" + serviceId + opId + ".xml";
		IWorkspaceRoot wRoot = ResourcesPlugin.getWorkspace().getRoot();
		Path basePPath = new Path(fileIpath);
		final IFile basefile = wRoot.getFile(basePPath);
		//TODO
		try {
			basefile.create(null, false, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		try {
			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("S" +  serviceId + opId);
			Element reqElement = root.addElement("request");
			Document reqDoc = DocumentHelper.parseText(reqMsg);
			reqElement.add(reqDoc.getRootElement());
			
			Element resElement = root.addElement("response");
			Document resDoc = DocumentHelper.parseText(resMsg);
			resElement.add(resDoc.getRootElement());
			
			elementAttrFilter(root);
			
			createFile(doc, filePath);
			
		} catch (Exception e) {

			log.error("生成sda文件失败！FILE=[" + filePath + "]", e);
		}
		
		IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		TreeView view = (TreeView) wp.findView("wizard.view1");
		if (null != view) {
			view.getViewer().getTree().forceFocus();
			view.reload();
		}
		try {
			basefile.refreshLocal(0, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return filePath;
		
	}
	
	public void createConfigFile(String serviceId, String operationId, String reqMsg, String resMsg) {
		String serviceCode = serviceId + operationId;
		String unPackPath = configPath + File.separator + "channel_all_service_" + serviceCode + ".xml";
		String packPath = configPath + File.separator + "service_" + serviceCode + "_system_all.xml";

		createUnpackFile(unPackPath, reqMsg);

		createPackFile(packPath, resMsg);

	}

	public void createUnpackFile(String filePath, String message) {

		try {
			// Document doc = DocumentHelper.createDocument();
			// 获取报文生成的doc
			Document mDoc = DocumentHelper.parseText(message);
			Element mRootDoc = mDoc.getRootElement();
			elementAttrFilter(mRootDoc);
			createFile(mDoc, filePath);

		} catch (Exception e) {
			log.error("生成拆包文件失败！FILE=[" + filePath + "]", e);
		}

	}

	/**
	 * 生成组包配置文件
	 * 
	 * @param filePath
	 * @param message
	 */
	public void createPackFile(String filePath, String message) {
		Document doc = DocumentHelper.createDocument();

		createFile(doc, filePath);
	}

	/**
	 * 将报文中测试数据过滤
	 * @param element
	 */
	public void elementAttrFilter(Element element) {

		List<Element> children = element.elements();
		if (null != children && 0 < children.size()) {
			for (Element child : children) {
				elementAttrFilter(child);
			}
		} else {
			element.clearContent();
//			element.setText("");
		}

	}

	/**
	 * 复制element结点
	 * 
	 * @param target
	 * @param source
	 */
	public void createElements(Element target, Element source) {
		List<Element> sourceChildren = source.elements();
		if (null != sourceChildren && sourceChildren.size() > 0) {
			// target.addAttribute("", "");
			for (Element sourceChild : sourceChildren) {
				Element targetChild = target.addElement(sourceChild.getName());
				createElements(targetChild, sourceChild);

			}
		} else {
			target.addAttribute("metadataId", source.getName());
		}
	}

	/**
	 * 将doc写入文件
	 * 
	 * @param doc
	 * @param fileName
	 */
	public void createFile(Document doc, String fileName) {
		try {
			File file = new File(fileName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileOutputStream out = new FileOutputStream(file, false);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			XMLWriter formatWriter = new XMLWriter(new OutputStreamWriter(out, "utf-8"), format);// 重新写回到原来的xml文件中

			formatWriter.write(doc);

			formatWriter.close();
			out.close();
		} catch (IOException e) {
			log.error(e, e);
		}
	}
	
	public void exportToESB(String serviceId, String operationId) {

	}
	/**
	 * 发送测试报文
	 * @param endpointUri
	 * @param message
	 * @param timeOut
	 * @return
	 */
//	public String postSoap(String endpointUri, String message, int timeOut){
//		SoapClient client = SoapClient.builder()
//                .endpointUri(endpointUri)
//                .connectTimeoutInMillis(timeOut)
//                .build();
//		String responseMsg = client.post(message);
//		return responseMsg;
//	}

	public static void main(String args[]) {
		String str = SoapWsUtil.getInstance().getRequstStr("http://localhost:8080/wsdl/30011000001.wsdl", "30011000001",
				"Op01");
		System.out.println(str);
	}

}
