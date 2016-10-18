package com.dc.bip.ide.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.context.PublicCode;
import com.dc.bip.ide.gef.model.LayerModel;
import com.dc.bip.ide.gef.model.Param;
import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.objects.BipServer;
import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.objects.CompServiceWsdlInfo;
import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.views.objects.ProtocolInNode;

public class HelpUtil {

	public static void buildPackage(String projectName, String src, String packageName) throws CoreException {
		String ps[] = packageName.split("\\.");
		StringBuffer sb = new StringBuffer();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IFolder res = root
				.getFolder(new Path((new StringBuilder("/")).append(projectName).append("/").append(src).toString()));
		if (!res.exists())
			res.create(true, true, null);
		String as[];
		int j = (as = ps).length;
		for (int i = 0; i < j; i++) {
			String item = as[i];
			sb.append("/").append(item);
			res = root.getFolder(new Path((new StringBuilder("/")).append(projectName).append("/").append(src)
					.append(sb.toString()).toString()));
			if (!res.exists())
				res.create(true, true, null);
		}
	}

	public static void mkdirs(IFolder folder, boolean force, boolean local, IProgressMonitor monitor)
			throws CoreException {
		if (folder != null) {
			if (force)
				change2Writeable(folder);
			_mkdirs(folder, force, local, monitor);
		}
	}

	public static void change2Writeable(IContainer folder) {
		if (folder.isReadOnly())
			folder.setReadOnly(false);
		if (folder.getParent() != null)
			change2Writeable(folder.getParent());
	}

	private static void _mkdirs(IFolder folder, boolean force, boolean local, IProgressMonitor monitor)
			throws CoreException {
		if (folder != null) {
			IContainer parent = folder.getParent();
			if (parent instanceof IContainer)
				if (parent.exists()) {
					if (parent.isReadOnly())
						parent.setReadOnly(false);
					try {
						parent.refreshLocal(1, monitor);
						if (folder != null && !folder.exists())
							folder.create(force, local, monitor);
					} catch (Throwable th) {
						System.out.println(th);
					}
				} else {
					_mkdirs((IFolder) parent, force, local, monitor);
					_mkdirs(folder, force, local, monitor);
				}
		}
	}

	public static InputStream openContentStream(BaseService userObject, String vm) {
		try {
			VelocityEngine ve;
			String tmpDir;
			OutputStreamWriter writer;
			if (userObject == null)
				return null;
			ve = new VelocityEngine();
			tmpDir = "com/dc/bip/ide/vm/";
			writer = null;
			ByteArrayInputStream bytearrayinputstream;
			ve.setProperty("resource.loader", "class");
			ve.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			ve.setProperty("file.resource.loader.path", tmpDir);
			ve.setProperty("input.encoding", "UTF-8");
			ve.setProperty("output.encoding", "UTF-8");
			ve.init();

			Template tmpl = ve.getTemplate((new StringBuilder(String.valueOf(tmpDir))).append(vm).toString());
			Context context = new VelocityContext();
			context.put("service", userObject);
			String purePath = userObject.getImpls().trim();
			String packageName = purePath.substring(0, purePath.lastIndexOf("."));
			String className = purePath.substring(purePath.lastIndexOf(".") + 1);
			context.put("packageName", packageName);
			context.put("className", className);
			context.put("Date", SimpleDateFormat.getDateTimeInstance().format(new Date()));
			context.put("lastModified", System.currentTimeMillis());

			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			writer = new OutputStreamWriter(bos, "UTF-8");
			tmpl.merge(context, writer);
			writer.flush();
			byte data[] = bos.toByteArray();
			System.err.println(((new StringBuilder(String.valueOf(userObject.getName()))).append(">>")
					.append(new String(data, "UTF-8")).toString()));
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			bytearrayinputstream = bis;
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			return bytearrayinputstream;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (ParseErrorException e) {
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	// 保存业务服务
	public static InputStream openContentStream(BusiSvcInfo userObject, String vm) {
		try {
			VelocityEngine ve;
			String tmpDir;
			OutputStreamWriter writer;
			if (userObject == null)
				return null;
			ve = new VelocityEngine();
			tmpDir = "com/dc/bip/ide/vm/";
			writer = null;
			ByteArrayInputStream bytearrayinputstream;
			ve.setProperty("resource.loader", "class");
			ve.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			ve.setProperty("file.resource.loader.path", tmpDir);
			ve.setProperty("input.encoding", "UTF-8");
			ve.setProperty("output.encoding", "UTF-8");
			ve.init();

			Template tmpl = ve.getTemplate((new StringBuilder(String.valueOf(tmpDir))).append(vm).toString());
			Context context = new VelocityContext();
			context.put("service", userObject);
			// String purePath = userObject.getImpls().trim();
			// String packageName = purePath.substring(0,
			// purePath.lastIndexOf("."));
			// String className = purePath.substring(purePath.lastIndexOf(".") +
			// 1);
			// context.put("packageName", packageName);
			// context.put("className", className);
			context.put("Date", SimpleDateFormat.getDateTimeInstance().format(new Date()));
			context.put("lastModified", System.currentTimeMillis());

			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			writer = new OutputStreamWriter(bos, "UTF-8");
			tmpl.merge(context, writer);
			writer.flush();
			byte data[] = bos.toByteArray();
			System.err.println(((new StringBuilder(String.valueOf(userObject.getName()))).append(">>")
					.append(new String(data, "UTF-8")).toString()));
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			bytearrayinputstream = bis;
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			return bytearrayinputstream;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (ParseErrorException e) {
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	static public BaseService parseBaseSvcXml(InputStream input) {
		Document doc;
		BaseService bs = new BaseService();
		try {
			SAXReader reader = new SAXReader();
			doc = reader.read(input);
			bs = new BaseService(getNodeText(doc, "/root/id", true));
			bs.setImpls(getNodeText(doc, "/root/impl", true));
			bs.setDescription(getNodeText(doc, "/root/description", true));
			bs.setLocation(getNodeText(doc, "/root/location", true));
			bs.setNickName(getNodeText(doc, "/root/nickName", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bs;
	}

	static private String getNodeText(Document doc, String idPath, boolean trim) {
		Node node = doc.selectSingleNode(idPath);
		if (node != null) {
			if (trim) {
				if (node.getText() != null)
					return node.getText().trim();
				else
					return "";
			} else {
				return node.getText();
			}
		} else {
			return null;
		}
	}

	
	
	public static void writeObject(Serializable obj , IFile file){
		FileOutputStream fileOutputStream  = null;
		ObjectOutputStream objectOutStream  = null;
		try{
			//fileOutputStream = new FileOutputStream(file.getLocation().toFile());
			fileOutputStream = new FileOutputStream(new File(file.getLocation().toOSString()));
			objectOutStream = new ObjectOutputStream(fileOutputStream);
			objectOutStream.writeObject(obj);
			objectOutStream.flush();
//			PlatformUtils.showInfo("保存", "保存成功!");
		}catch(Exception e){
			e.printStackTrace();
//			PlatformUtils.showInfo("保存", "保存失败!");
		}finally{
			if(null != fileOutputStream){
				try{
					fileOutputStream.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != objectOutStream){
				try{
					objectOutStream.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void writeObject2(Serializable obj , IFile file){
		FileOutputStream fileOutputStream  = null;
		ObjectOutputStream objectOutStream  = null;
		try{
			//fileOutputStream = new FileOutputStream(file.getLocation().toFile());
			fileOutputStream = new FileOutputStream(new File(file.getLocation().toOSString()));
			objectOutStream = new ObjectOutputStream(fileOutputStream);
			objectOutStream.writeObject(obj);
			objectOutStream.flush();
//			PlatformUtils.showInfo("保存", "保存成功!");
		}catch(Exception e){
			e.printStackTrace();
//			PlatformUtils.showInfo("保存", "保存失败!");
		}finally{
			if(null != fileOutputStream){
				try{
					fileOutputStream.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != objectOutStream){
				try{
					objectOutStream.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void writeObject(List<PublicCode> codes, IFile file) {
		try {
			FileOutputStream outStream = new FileOutputStream(file.getLocation().toFile());
			ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);
			objectOutStream.writeObject(codes);
			outStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	public static void writeObject(LayerModel context, IFile file) {
		FileOutputStream outStream = null;
		ObjectOutputStream objectOutStream = null;

		try {
			File ioFile = file.getLocation().toFile();
			if (!ioFile.exists()) {
				ioFile.getParentFile().mkdirs();
				ioFile.createNewFile();
			}
			outStream = new FileOutputStream(ioFile);
			objectOutStream = new ObjectOutputStream(outStream);
			objectOutStream.writeObject(context);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != objectOutStream) {
				try {
					objectOutStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != outStream) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static LayerModel readContextObject(IFile file) {
		LayerModel context = null;
		FileInputStream freader = null;
		ObjectInputStream objectInputStream = null;
		try {
			File ioFile = file.getLocation().toFile();
			if (!ioFile.exists()) {
				return new LayerModel();
			}
			freader = new FileInputStream(ioFile);
			objectInputStream = new ObjectInputStream(freader);
			context = (LayerModel) objectInputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != objectInputStream) {
				try {
					objectInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != freader) {
				try {
					freader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return context;
	}
	
	private static  void writeObject(List<Param> params, File ioFile) {
		FileOutputStream outStream = null;
		ObjectOutputStream objectOutStream = null;

		try {
			if (!ioFile.exists()) {
				ioFile.getParentFile().mkdirs();
				ioFile.createNewFile();
			}
			outStream = new FileOutputStream(ioFile);
			objectOutStream = new ObjectOutputStream(outStream);
			objectOutStream.writeObject(params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != objectOutStream) {
				try {
					objectOutStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != outStream) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static List<Param> readParams(File ioFile) {
		List<Param> params = null;
		FileInputStream freader = null;
		ObjectInputStream objectInputStream = null;
		try {
			if (!ioFile.exists()) {
				return new ArrayList();
			}
			freader = new FileInputStream(ioFile);
			objectInputStream = new ObjectInputStream(freader);
			params = (List<Param>) objectInputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != objectInputStream) {
				try {
					objectInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != freader) {
				try {
					freader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return params;
	}

	public static CompositeService readObject(IFile file) {
		CompositeService compSvc = null;
		try {
			FileInputStream freader = new FileInputStream(file.getLocation().toFile());
			ObjectInputStream objectInputStream = new ObjectInputStream(freader);
			compSvc = (CompositeService) objectInputStream.readObject();
			return compSvc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return compSvc;
	}

	public static BipServer readBipServerObject(File file) {
		BipServer bipServer = null;
		try {
			FileInputStream freader = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(freader);
			bipServer = (BipServer) objectInputStream.readObject();
			return bipServer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bipServer;
	}
	
	public static Object readProtocolNode(File file) {
		Object inNode = null;
		try {
			FileInputStream freader = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(freader);
			inNode = (ProtocolInNode) objectInputStream.readObject();
			return inNode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inNode;
	}

	public static List<PublicCode> readPublicCodeObject(IFile file) {
		List<PublicCode> publicCodes = null;
		try {
			FileInputStream freader = new FileInputStream(file.getLocation().toFile());
			ObjectInputStream objectInputStream = new ObjectInputStream(freader);
			publicCodes = (List<PublicCode>) objectInputStream.readObject();
			return publicCodes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return publicCodes;
	}

	public static void writeText(String text, IFile file) {
		try {
			FileOutputStream outStream = new FileOutputStream(file.getLocation().toFile());
			OutputStreamWriter osw = new OutputStreamWriter(outStream);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(text);
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String generateWSDL(CompServiceWsdlInfo wsdlInfo) {
		String protocolType = wsdlInfo.getProtocoltype();
		String url = wsdlInfo.getUrl();
		String namespace = wsdlInfo.getNamespace();
		String binding = wsdlInfo.getBind();
		String endpoint = wsdlInfo.getEndpoint();
		String operation = wsdlInfo.getOperation();
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("wsdl:definitions");
		rootElement.addNamespace("wsdl", "http://schemas.xmlsoap.org/wsdl/");
		rootElement.addNamespace("d", "http://esb.dcitsbiz.com/services/10032000001/metadata");
		rootElement.addNamespace("s", "http://esb.dcitsbiz.com/services/10032000001");
		rootElement.addNamespace("tns", "http://esb.dcitsbiz.com/services/10032000001/wsdl");

		rootElement.addAttribute("targetNamespace", "http://esb.dcitsbiz.com/services/10032000001/wsdl");
		rootElement.addAttribute("xmlns:wsdl", "http://schemas.xmlsoap.org/wsdl/");
		rootElement.addAttribute("xmlns:soapnc", "http://schemas.xmlsoap.org/soap/encoding/");
		rootElement.addAttribute("xmlns:http", "http://schemas.xmlsoap.org/wsdl/http/");
		rootElement.addAttribute("xmlns:tns", "");
		rootElement.addAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		rootElement.addAttribute("xmlns:soap", "http://schemas.xmlsoap.org/wsdl/soap/");
		rootElement.addAttribute("xmlns:mime", "http://schemas.xmlsoap.org/wsdl/mime/");
		Element typesElement = rootElement.addElement("wsdl:types");
		//
		Element reqmessageElement = rootElement.addElement("wsdl:message");
		reqmessageElement.addAttribute("name", "Req1003200000101");
		//
		Element rspmessageElement = rootElement.addElement("wsdl:message");
		rspmessageElement.addAttribute("name", "Rsp1003200000101");

		Element portTypeElement = rootElement.addElement("wsdl:portType");
		portTypeElement.addAttribute("name", "ESBServerPortType");

		Element bindingElement = rootElement.addElement("wsdl:binding");
		bindingElement.addAttribute("name", "ESBServerSoapBinding");

		Element serviceElement = rootElement.addElement("wsdl:service");
		serviceElement.addAttribute("name", "S10032000001");

		return document.asXML();
	}

	public static String generateSDA(String serviceId) {
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("x:schema");
		// rootElement.addNamespace("xmlns:x",
		// "http://www.w3.org/2001/XMLSchema");
		rootElement.addAttribute("xmlns:x", "http://www.w3.org/2001/XMLSchema");
		// Element reqElement = rootElement.addElement("request");
		// Element reqsdorootElement = reqElement.addElement("sdoroot");
		// Element reqappheadElement = reqsdorootElement.addElement("APP_HEAD");
		// Element reqbodyElement = reqsdorootElement.addElement("BODY");
		// Element reqsysheadElement = reqsdorootElement.addElement("SYS_HEAD");
		// Element reqlocalheadElement =
		// reqsdorootElement.addElement("LOCAL_HEAD");
		//
		// Element rspElement = rootElement.addElement("response");
		// Element rspsdorootElement = rspElement.addElement("sdoroot");
		// Element rspappheadElement = rspsdorootElement.addElement("APP_HEAD");
		// Element rspbodyElement = rspsdorootElement.addElement("BODY");
		// Element rspsysheadElement = rspsdorootElement.addElement("SYS_HEAD");
		// Element rsplocalheadElement =
		// rspsdorootElement.addElement("LOCAL_HEAD");
		return document.asXML();
	}

	public static String getTextFromFile(IFile file) {
		Document doc = null;
		System.out.println(file.getFullPath().toOSString());
		try {
			BufferedReader bufferReader = new BufferedReader(new FileReader(file.getLocation().toFile()));
			//BufferedReader bufferReader = new BufferedReader(new FileReader(new File("D:\\runtime-EclipseApplication\\test\\dev\\services\\composite\\S11002000007\\service_S1100200000702.xml")));
			SAXReader saxReader = new SAXReader();
			doc = saxReader.read(bufferReader);
			return doc.asXML();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc.asXML();
	}
	
	public static String getTextFromIFile(IFile file) {
		Document doc = null;
		System.out.println(file.getFullPath().toOSString());
		try {
			BufferedReader bufferReader = new BufferedReader(new FileReader(new File(file.getFullPath().toOSString())));
			//BufferedReader bufferReader = new BufferedReader(new FileReader(new File("D:\\runtime-EclipseApplication\\test\\dev\\services\\composite\\S11002000007\\service_S1100200000702.xml")));
			SAXReader saxReader = new SAXReader();
			doc = saxReader.read(bufferReader);
			return doc.asXML();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc.asXML();
	}

	public static File[] getFlowFiles(String projectName, String bpmnName) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);

		String folderPath = project.getFolder("/dev/services/flow/" + bpmnName.substring(0, bpmnName.indexOf(".bpmn")))
				.getLocation().toString();
		File folder = new File(folderPath);
		if (folder.exists()) {
			return folder.listFiles();
		}
		return null;
	}
	
	static public List<Param> readSessionParams(CompositeService service)
	{
		List<Param> sessionParams = new ArrayList<Param>();
		StringBuilder sb = new StringBuilder();
		sb.append(BipConstantUtil.CompositePath).append(service.getBaseinfo().getServiceId());
		sb.append("/").append(service.getBaseinfo().getServiceId()).append(BipConstantUtil.SessionParamFileExtension);
		IFile file = service.getProject().getFile(new Path(sb.toString()));
/*		try {
			file.getParent().refreshLocal(IResource.DEPTH_ONE, null);*/
			if(file.exists())
			{
				sessionParams =  HelpUtil.readParams(file.getLocation().toFile());
			}
		/*} catch (CoreException e) {
			e.printStackTrace();
		}	*/
		return sessionParams;
	}
	
	static public List<Param> readGlobalParams(CompositeService service)
	{
		List<Param> globalParams = new ArrayList<Param>();
		IFile file = service.getProject().getFile(new Path(BipConstantUtil.GlobalParamFile));
/*		try {
		file.getParent().refreshLocal(IResource.DEPTH_ONE, null);*/
			if(file.exists())
			{				
				globalParams=  HelpUtil.readParams (file.getLocation().toFile());
			}
/*		} catch (CoreException e) {
			e.printStackTrace();
		}*/
		return globalParams;		
	}
	
	static public void wirteGlobalParams(CompositeService service,List<Param> globalParams)
	{
		IFile resourceFile =service.getProject().getFile(new Path(BipConstantUtil.GlobalParamFile));
		HelpUtil.writeObject(globalParams, resourceFile.getLocation().toFile());
		try {
			resourceFile.getParent().refreshLocal(IResource.DEPTH_ONE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	static public void wirteSessionParams(CompositeService service,List<Param> sessionParams)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(BipConstantUtil.CompositePath).append(service.getBaseinfo().getServiceId());
		sb.append("/").append(service.getBaseinfo().getServiceId()).append(BipConstantUtil.SessionParamFileExtension);
		IFile resourceFile = service.getProject().getFile(new Path(sb.toString()));
		HelpUtil.writeObject(sessionParams, resourceFile.getLocation().toFile());
		try {
			resourceFile.getParent().refreshLocal(IResource.DEPTH_ONE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}		
	}
	
	public static  String readIFile(String filePath) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IPath sdaPath = new Path(filePath);
		IFile sdafile = root.getFile(sdaPath);
		File file = sdafile.getLocation().toFile();
		StringBuilder fileContentStr = new StringBuilder();
		byte[] buffer = new byte[1024];

		if (file.exists() && file.isFile()) {
			try {
				FileInputStream is = new FileInputStream(file);
				int hasRead = 0;
				while ((hasRead = is.read(buffer)) > 0) {
					fileContentStr.append(new String(buffer, 0, hasRead));
				}
				is.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileContentStr.toString();
	}
	public static String readFile(String filePath) {
		File file = new File(filePath);
		StringBuilder fileContentStr = new StringBuilder();
		byte[] buffer = new byte[1024];

		if (file.exists() && file.isFile()) {
			try {
				FileInputStream is = new FileInputStream(file);
				int hasRead = 0;
				while ((hasRead = is.read(buffer)) > 0) {
					fileContentStr.append(new String(buffer, 0, hasRead,"UTF-8"));
				}
				is.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileContentStr.toString();
	}

	public static void main(String[] args) {
	}
	

}
