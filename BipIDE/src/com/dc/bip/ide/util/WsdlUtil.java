package com.dc.bip.ide.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.PlatformUI;
import org.reficio.ws.builder.SoapBuilder;
import org.reficio.ws.builder.SoapOperation;
import org.reficio.ws.builder.core.Wsdl;

import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.objects.CompServiceBaseInfo;
import com.dc.bip.ide.objects.CompServiceSDAInfo;
import com.dc.bip.ide.objects.CompServiceWsdlInfo;
import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BSOperationNode;
import com.dc.bip.ide.views.objects.CompositeNode;
import com.ibm.wsdl.BindingOperationImpl;

public class WsdlUtil {
	private final static Log log = LogFactory.getLog(WsdlUtil.class);
	
	/**
	 * 根据url生成业务服务节点，并生成wsdl文件，sda文件，配置文件
	 * 
	 * @param url
	 * @return
	 */
	public static List<BSOperationNode> generateOperationNodes(String url, String projectName, 
			String basePath,String folderPath) {
		System.out.println("basePath = " + basePath);
		System.out.println("folderPath = " + folderPath);
		List<BSOperationNode> list = new ArrayList<BSOperationNode>();
		try {
			WSDLFactory factory = WSDLFactory.newInstance();
			WSDLReader reader = factory.newWSDLReader();
			reader.setFeature("javax.wsdl.verbose", true);
			reader.setFeature("javax.wsdl.importDocuments", true);
			Definition def = reader.readWSDL(url);
			Map services = def.getServices();
			String serviceName = null;
			for (Object key : services.keySet()) {
				Service service = (Service) services.get(key);
				System.out.println("serviceName:" + service.getQName().getLocalPart());
				serviceName = service.getQName().getLocalPart();
				
				//保存wsdl文件到本地
				Wsdl parser = Wsdl.parse(url);
				File file = generateWsdlFile(url, basePath, serviceName,projectName,"/dev/services/busi/");
				
				Map ports = service.getPorts();
				for (Object portkey : ports.keySet()) {
					Port port = (Port) ports.get(portkey);
					String bindingName = port.getBinding().getQName().getLocalPart();
					for(Object obj : port.getBinding().getBindingOperations()){
						BindingOperationImpl operation = (BindingOperationImpl)obj;
						String operationName = operation.getName();
						String operationId = operationName.length()>2 ? operationName.substring(operationName.length()-2) : operationName;
						
						BSOperationNode node = new BSOperationNode(serviceName + operationId );//生成一个新的业务节点
						list.add(node);
						node.setProjectName(projectName);
						BusiSvcInfo busiSvcInfo = new BusiSvcInfo();
						node.setBusiSvcInfo(busiSvcInfo);
						
						busiSvcInfo.setId(serviceName+operationId);
						busiSvcInfo.setProjectName(projectName);
						busiSvcInfo.setProtocolType("WebService");//协议类型
						busiSvcInfo.setWsdlUrl(url);//wsdlURL
						busiSvcInfo.setNameSpace(def.getTargetNamespace());//命名空间
						
						busiSvcInfo.setBindingName(bindingName);//bindingname
						busiSvcInfo.setOperationName(operationName);
						
						SoapOperation soapOperation = getSoapOperation(parser, port.getBinding().getQName(), operationName);
						busiSvcInfo.setActionName(soapOperation.getSoapAction());//soapaction
						List l = port.getExtensibilityElements();
						ExtensibilityElement element = (ExtensibilityElement) l.get(0);
						String s = element.toString();
						String endpoint = s.substring(s.indexOf("location"));
						String[] strs = endpoint.split("=");
						if (strs.length > 1) {
							endpoint = strs[1];
						}
						busiSvcInfo.setEndpoint(endpoint);//endpoint
						
						busiSvcInfo.setLocalUri(file.getPath());//localUri
						//TODO 删除测试
						if(file.getParentFile().exists()){
							File parentFile = file.getParentFile();
							if(parentFile.getParentFile().exists()){
								String resFileStr = parentFile.getParentFile().getPath();
								busiSvcInfo.setFileFolderStr(resFileStr);
							}
						}
						//
						File[] files = file.getParentFile().listFiles();
						for(File childFile : files){
							if(!childFile.getName().endsWith(".xml"))
							{
								busiSvcInfo.getLocalUris().add(childFile.getPath());
							}
							
						}
						//生成测试报文
						String reqMsg = generateReqMsg(parser, basePath, port.getBinding().getQName(), operationName);
						busiSvcInfo.setReqMsg(reqMsg);
						String resMsg = generateResMsg(parser, basePath, port.getBinding().getQName(), operationName);
						busiSvcInfo.setResMsg(resMsg);
						//生成服务定义文件
						String serDefPath = SoapWsUtil.getInstance().generateSerDefFile(basePath, serviceName, 
								operationId, reqMsg, resMsg , folderPath);
						busiSvcInfo.setSdaFilePath(serDefPath);
						
					}
				}
				
			}
			
		} catch (Exception e) {
			log.error("导入wsdl失败！", e);
		}
		TreeView view = (TreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("wizard.view1");
		if(null != view)
		{
			view.reload();
		}		
		return list;
	}
	
	/**
	 * 根据url生成业务服务节点，并生成wsdl文件，sda文件，配置文件
	 * 
	 * @param url
	 * @return
	 */
	public static List<CompositeNode> generateCompositeNodes(String url, String projectName, 
			String basePath,String folderPath) {
		System.out.println("basePath = " + basePath);
		System.out.println("folderPath = " + folderPath);
		List<CompositeNode> list = new ArrayList<CompositeNode>();
		try {
			WSDLFactory factory = WSDLFactory.newInstance();
			WSDLReader reader = factory.newWSDLReader();
			reader.setFeature("javax.wsdl.verbose", true);
			reader.setFeature("javax.wsdl.importDocuments", true);
			Definition def = reader.readWSDL(url);
			Map services = def.getServices();
			String serviceName = null;
			for (Object key : services.keySet()) {
				Service service = (Service) services.get(key);
				serviceName = service.getQName().getLocalPart();
				
				//保存wsdl文件到本地
				Wsdl parser = Wsdl.parse(url);
				File file = generateWsdlFile(url, basePath, serviceName,projectName,"/dev/services/composite/");
				
				Map ports = service.getPorts();
				for (Object portkey : ports.keySet()) {
					Port port = (Port) ports.get(portkey);
					String bindingName = port.getBinding().getQName().getLocalPart();
					for(Object obj : port.getBinding().getBindingOperations()){
						BindingOperationImpl operation = (BindingOperationImpl)obj;
						String operationName = operation.getName();
						String operationId = operationName.length()>2 ? operationName.substring(operationName.length()-2) : operationName;
						
						CompositeNode node = new CompositeNode(serviceName + operationId );//生成一个新的业务节点
						list.add(node);
						node.setProjectName(projectName);
						
						CompositeService compositeService = new CompositeService();
						compositeService.setServiceFolderName(serviceName);
						node.setCompSvc(compositeService);
						
						CompServiceBaseInfo  baseinfo = new CompServiceBaseInfo();
						baseinfo.setServiceId(node.getNodeName());
						baseinfo.setDescrition("");
						compositeService.setBaseinfo(baseinfo);
						
						
						
						CompServiceWsdlInfo compServiceWsdlInfo = new CompServiceWsdlInfo();
						compositeService.setWsdlinfo(compServiceWsdlInfo);
						//compServiceWsdlInfo.set.setId(serviceName+operationId);
						//compServiceWsdlInfo.setProjectName(projectName);
						compServiceWsdlInfo.setProtocoltype("WebService");//协议类型
						compServiceWsdlInfo.setUrl(url);//wsdlURL
						compServiceWsdlInfo.setNamespace(def.getTargetNamespace());//命名空间
						
						compServiceWsdlInfo.setBind(bindingName);//bindingname
						compServiceWsdlInfo.setOperation(operationName);
						
						SoapOperation soapOperation = getSoapOperation(parser, port.getBinding().getQName(), operationName);
						compServiceWsdlInfo.setActionName(soapOperation.getSoapAction());//soapaction
						List l = port.getExtensibilityElements();
						ExtensibilityElement element = (ExtensibilityElement) l.get(0);
						String s = element.toString();
						String endpoint = s.substring(s.indexOf("location"));
						String[] strs = endpoint.split("=");
						if (strs.length > 1) {
							endpoint = strs[1];
						}
						compServiceWsdlInfo.setEndpoint(endpoint);//endpoint
						


						//生成测试报文
						String reqMsg = generateReqMsg(parser, basePath, port.getBinding().getQName(), operationName);
						compositeService.setReqMsg(reqMsg);
						String resMsg = generateResMsg(parser, basePath, port.getBinding().getQName(), operationName);
						compositeService.setRspMsg(resMsg);
						//生成服务定义文件 .xml文件
						String serDefPath = SoapWsUtil.getInstance().generateSerDefFile(basePath, serviceName, 
								operationId, reqMsg, resMsg , folderPath);
						
						CompServiceSDAInfo compServiceSDAInfo = new CompServiceSDAInfo();
						compServiceSDAInfo.setFilepath(serDefPath);
						compositeService.setSdafo(compServiceSDAInfo);
						
					}
				}
				
			}
			
		} catch (Exception e) {
			log.error("导入wsdl失败！", e);
		}
		TreeView view = (TreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("wizard.view1");
		if(null != view)
		{
			view.reload();
		}		
		return list;
	}
	
	public static File generateWsdlFile(String url, String basePath, String serviceName,String projectName,String foldPath){
		try{
		URL url1 = new URL(url);
		String wsdlFileName = new File(url1.getFile()).getName();
		String filePath = basePath + File.separator + serviceName + File.separator + serviceName;
		String parentFilePath = basePath + File.separator + serviceName;
		File file = new File(filePath);
		File parentFile = new File(parentFilePath);
		
		String fileFolderStr = new StringBuilder("/").append(projectName).append(foldPath).append(serviceName)
				.toString();
		String fileStr = new StringBuilder("/").append(projectName).append(foldPath)
				.append(serviceName).append("/").append(serviceName)
				.toString();
		
		IWorkspaceRoot wRoot = ResourcesPlugin.getWorkspace().getRoot();
		IFolder parentFolder = wRoot.getFolder(new Path(fileFolderStr));
		IFolder folder = wRoot.getFolder(new Path(fileStr));
		
		if(!file.getParentFile().exists()){
			HelpUtil.mkdirs(parentFolder, true, true, null);
			parentFile.mkdirs();
			HelpUtil.mkdirs(folder, true, true, null);
			file.mkdirs();
		}else{
			folder.delete(true, null);
			parentFolder.delete(true, null);
			file.delete();
			parentFile.delete();
			HelpUtil.mkdirs(parentFolder, true, true, null);
			parentFile.mkdirs();
			HelpUtil.mkdirs(folder, true, true, null);
			file.mkdirs();
		}
		Wsdl.saveWsdl(url1, file);
		parentFolder.refreshLocal(0, null);
		folder.refreshLocal(0, null);
		return file;
		}catch(Exception e){
			log.error("保存wsdl文件失败！", e);
		}
		
		return null;
	}
	/**
	 *生成请求报文 
	 * @param parser
	 * @param basePath
	 * @param bindingName
	 * @param opeartionName
	 * @return
	 */
	public static String generateReqMsg(Wsdl parser, String basePath, QName bindingName, String opeartionName){
		SoapBuilder soapBuilder = parser.getBuilder(bindingName);
		SoapOperation soapOperation = soapBuilder.operation().name(opeartionName).find();
		String msg = soapBuilder.buildInputMessage(soapOperation);
		System.out.println(msg);
		return msg;
		
	}
	/**
	 * 生成响应报文
	 * @param parser
	 * @param basePath
	 * @param bindingName
	 * @param opeartionName
	 * @return
	 */
	public static String generateResMsg(Wsdl parser, String basePath, QName bindingName, String opeartionName){
		SoapBuilder soapBuilder = parser.getBuilder(bindingName);
		SoapOperation soapOperation = soapBuilder.operation().name(opeartionName).find();
		String msg = soapBuilder.buildOutputMessage(soapOperation);
		System.out.println(msg);
		return msg;
		
	}
	
	public static SoapOperation getSoapOperation(Wsdl parser, QName bindingName, String opeartionName)
	{
		SoapBuilder soapBuilder = parser.getBuilder(bindingName);
		SoapOperation soapOperation = soapBuilder.operation().name(opeartionName).find();
		return soapOperation;
	}
}
