package com.dc.bip.ide.popup.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.util.PlatformUtils;
import com.dc.bip.ide.views.objects.FlowNode;

public class FlowConvertAction implements IObjectActionDelegate {

	private static final String SWITCH_EVENT = "org.activiti.designer.bip.event.SwitchEvent";
	private static final String BASE_SERVICE = "org.activiti.designer.bip.task.BaseTask";
	private static final String BUZZ_SERVICE = "org.activiti.designer.bip.task.BusinessTask";

	private FlowNode flowNode;
	private Shell shell;
	private Element mainProcessElement = null;
	private List<Element> subProcessElements = new ArrayList<Element>();
	private Map<String, String[]> serviceCollProcessMap = new HashMap<String, String[]>();
	private Map<String, Element> barrierElementMap = new HashMap<String, Element>();
	private Map<String, Element> runnableElementMap = new HashMap<String, Element>();
	private String processId;
	private String serviceId;
	private Element reversalElement;

	public FlowConvertAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		IFile ifile = flowNode.getResource();
		File file = ifile.getLocation().toFile();
		convertBpmnToXml(shell, file);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object first = ((IStructuredSelection) selection).getFirstElement();
			if (first instanceof FlowNode) {
				flowNode = (FlowNode) first;
			}
		}
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * 处理流程节点
	 * 
	 * @param rootElement
	 */
	private void parseProcessNodes(Element rootElement) {
		subProcessElements = new ArrayList<Element>();
		// 获取流程根节点
		List<Element> processElements = rootElement.elements("process");

		List<String> collaborationElements = getCollaborationProcesses(rootElement);
		for (Element processElement : processElements) {
			String id = processElement.attributeValue("id");
			if (null != id) {
				if (!collaborationElements.contains(id)) {
					mainProcessElement = processElement;
				} else {
					subProcessElements.add(processElement);
				}
			}
		}
		parseCollaborationProcess(subProcessElements);
	}

	private void parseCollaborationProcess(List<Element> collaborationProcessElements) {
		serviceCollProcessMap = new HashMap<String, String[]>();
		for (Element collaborationProcessElement : collaborationProcessElements) {
			String processId = collaborationProcessElement.attributeValue("id");
			Element laneSetElement = collaborationProcessElement.element("laneSet");
			List<Element> laneElements = laneSetElement.elements("lane");
			for (Element laneElement : laneElements) {
				String laneId = laneElement.attributeValue("id");
				String[] laneSetLanePair = new String[2];
				Element flowNodeRef = laneElement.element("flowNodeRef");
				String serviceId = flowNodeRef.getText();
				laneSetLanePair[0] = processId;
				laneSetLanePair[1] = laneId;
				serviceCollProcessMap.put(serviceId, laneSetLanePair);
			}
		}
	}

	private Element getMainProcess(Element rootElement) {
		return mainProcessElement;
	}

	private Element getStartElement(Element processElement) {
		List<Element> startEvents = processElement.elements("startEvent");
		if (startEvents != null && startEvents.size() > 0) {
			if (startEvents.size() > 1) {
				MessageDialog.openError(shell, "Runntime error", "startEvent number must be one at root path!");
			}
		} else {
			MessageDialog.openError(shell, "Runntime error", "startEvent required!");
		}
		return startEvents.get(0);
	}

	/**
	 * 获取多流程配置
	 * 
	 * @param rootElement
	 * @return
	 */
	private List<String> getCollaborationProcesses(Element rootElement) {
		List<String> collaborationProcesses = new ArrayList<String>();
		Element collaborationElement = rootElement.element("collaboration");
		if (null != collaborationElement) {
			List<Element> participantElements = collaborationElement.elements("participant");
			for (Element participantElement : participantElements) {
				String collaborationProcessId = participantElement.attributeValue("processRef");
				collaborationProcesses.add(collaborationProcessId);
			}
		}
		return collaborationProcesses;
	}

	public void clear() {
		subProcessElements = new ArrayList<Element>();
		serviceCollProcessMap = new HashMap<String, String[]>();
		barrierElementMap = new HashMap<String, Element>();
		runnableElementMap = new HashMap<String, Element>();
	}

	public void convertBpmnToXml(Shell shell, File file) {
		clear();
		try {
			String filePath = file.getPath();
			String fileName = file.getName().substring(0, file.getName().indexOf(".bpmn"));
			SAXReader sr = new SAXReader();
			Document doc = sr.read(file);
			filePath = filePath.substring(0, filePath.indexOf(".bpmn")) + "/" + fileName + ".xml";
			Element root = doc.getRootElement();
			parseProcessNodes(root);
			Element process = getMainProcess(root);
			// 查找开始节点
			Element startEvent = getStartElement(process);
			String startEventId = startEvent.attributeValue("id");
			Document targetDoc = DocumentHelper.createDocument();
			Element targetRoot = targetDoc.addElement("root");
			targetRoot.addAttribute("main", fileName);
			// 根据开始节点查询下一步
			List<Element> seqFlows = getSequenceFlows(startEventId);
			for (Element seqFlow : seqFlows) {
				String targetRef = seqFlow.attributeValue("targetRef");
				if (StringUtils.isNotEmpty(targetRef)) {
					Element firstElement = getElementById(targetRef);
					if (firstElement != null) {
						// 添加主流程
						Element mainElement = targetRoot.addElement("processDefinition");
						processId = fileName;
						// 填充主流程
						mainElement.addAttribute("id", "P" + fileName);
						mainElement.addAttribute("isAsync", "true");
						// mainElement.addAttribute("main", "true");// 主流程标记
						mainElement.addAttribute("mapping", "M" + fileName);
						fillTargetContent(mainElement, seqFlow);
					}
				}
			}
			createFile(targetDoc, filePath);
			PlatformUtils.showInfo("提示", "生成配置成功！");
		} catch (Exception e) {
			e.printStackTrace();
			PlatformUtils.showInfo("提示", "生成配置失败！");
			return;
		}
	}

	public List<Element> getSequenceFlows(String sourceRef) {
		List<Element> sequenceFlows = new ArrayList<Element>();
		List<Element> sfs = mainProcessElement.elements("sequenceFlow");
		for (Element element : sfs) {
			if (element.attribute("sourceRef") != null) {
				if (sourceRef.equals(element.attributeValue("sourceRef"))) {
					sequenceFlows.add(element);
				}
			}
		}
		for (Element subProcessElement : subProcessElements) {
			sfs = subProcessElement.elements("sequenceFlow");
			for (Element element : sfs) {
				if (element.attribute("sourceRef") != null) {
					if (sourceRef.equals(element.attributeValue("sourceRef"))) {
						sequenceFlows.add(element);
					}
				}
			}
		}
		return sequenceFlows;
	}

	private void getNextElement() {

	}

	private Element processFlow(Element flowElement, Element targetElement) {
		Element caseElement = null;
		Element extensionsElement = flowElement.element("extensionElements");
		if (null != extensionsElement) {
			List<Element> elements = extensionsElement.elements();
			if (elements != null && elements.size() == 3) {
				caseElement = targetElement.addElement("case");
				caseElement.addAttribute("type", "operation");
				String value = elements.get(0).getText();
				caseElement.addAttribute("value", value);
				String key = elements.get(1).getText();
				caseElement.addAttribute("key", key);
			}
		}
		return caseElement;
	}

	private Element processServiceTask(Element element, Element flowElement, Element xmlParent) {
		Element targetElement = null;
		if (element.attribute("extensionId") != null) {
			String extensionId = element.attributeValue("extensionId");
			if (SWITCH_EVENT.equals(extensionId)) {
				targetElement = xmlParent.addElement("switch");
				fillSwitchAttr(targetElement, element);
				xmlParent = targetElement;
			} else {
				String serviceTaskId = element.attributeValue("id");
				String[] laneSetPair = serviceCollProcessMap.get(serviceTaskId);
				if (null != laneSetPair && laneSetPair.length == 2) {
					Element barrierElement = barrierElementMap.get(laneSetPair[0]);
					if (null == barrierElement) {
						barrierElement = xmlParent.addElement("barrier");
						barrierElement.addAttribute("id", laneSetPair[0]);
						barrierElementMap.put(laneSetPair[0], barrierElement);
					}

					Element runnableElement = runnableElementMap.get(laneSetPair[0] + laneSetPair[1]);
					if (null == runnableElement) {
						runnableElement = barrierElement.addElement("runnable");
						runnableElement.addAttribute("id", laneSetPair[0] + laneSetPair[1]);
						runnableElement.addAttribute("countDown", "");
						runnableElement.addAttribute("timeout", "");
						runnableElementMap.put(laneSetPair[0] + laneSetPair[1], runnableElement);
					}
					xmlParent = runnableElement;
				}
				Element caseElement = processFlow(flowElement, xmlParent);
				if (null != caseElement) {
					xmlParent = caseElement;
				}
				targetElement = xmlParent.addElement("service");
				Element extensionsElement = element.element("extensionElements");
				if (null != extensionsElement) {
					List<Element> elements = extensionsElement.elements();
					if (elements != null) {
						String serviceId = elements.get(0).getText();
						targetElement.addAttribute("id", serviceId);
						this.serviceId = serviceId;

					}
					Element reversalElement = null;
					for (Element attrElement : elements) {
						if ("reversalField".equals(attrElement.getName())) {
							if (null == reversalElement)
								reversalElement = targetElement.addElement("reversal");
							reversalElement.addElement("retErrorCode").addText(attrElement.getText());
						}
						if ("reversalValue".equals(attrElement.getName())) {
							if (null == reversalElement)
								reversalElement = targetElement.addElement("reversal");
							reversalElement.addElement("retErrorValue").addElement("value")
									.addText(attrElement.getText());
						}
						if ("reversalServiceId".equals(attrElement.getName())) {
							if (null == reversalElement)
								reversalElement = targetElement.addElement("reversal");
							reversalElement.addElement("reversalMapping")
									.addText("M" + processId + "_" + attrElement.getText());
							targetElement.addAttribute("persist", "true");
							targetElement.addAttribute("reversal", attrElement.getText());
							this.mainProcessElement.addAttribute("isReversal", "true");
						}
						if ("persist".equals(attrElement.getName())) {
							targetElement.addAttribute("persist", "true");
						}
							

					}
				}

				if (element.attribute("name") != null) {
					targetElement.addAttribute("serviceName", element.attributeValue("name"));
				}
				if (BASE_SERVICE.equals(extensionId)) {
					targetElement.addAttribute("type", "baseService");
				}
				if (BUZZ_SERVICE.equals(extensionId)) {
					targetElement.addAttribute("type", "buzzService");
					targetElement.addAttribute("mapping", "M" + processId + "_" + this.serviceId);
				}

			}
		}
		List<Element> sequenceFlows = getSequenceFlows(element.attributeValue("id"));
		for (Element sequenceFlow : sequenceFlows) {

			fillTargetContent(xmlParent, sequenceFlow);
		}
		return targetElement;
	}

	public void fillTargetContent(Element xmlParent, Element flowElement) {
		Element element = getElementById(flowElement.attributeValue("targetRef"));

		if (element != null) {
			Element parent = element.getParent();
			Element targetElement = null;
			if ("serviceTask".equalsIgnoreCase(element.getName())) {
				String extensionId = element.attributeValue("extensionId");
				processServiceTask(element, flowElement, xmlParent);
			}
			if (parent != null && "subProcess".equals(parent.getName())
					&& "true".equals(parent.attributeValue("triggeredByEvent"))) {// 判断是否loop中元素
				// 判断loop元素是否已存在
				Element xmlLoop = xmlParent.element("loop");
				if (!"case".equals(xmlParent.getName())) {// case节点对比bpmn降级
					if (xmlLoop == null || parent.attributeValue("id").equals(xmlLoop.attributeValue("id"))) {
						Element loopElement = xmlParent.addElement("loop");
						fillLoopAttr(loopElement, parent);
						xmlParent = loopElement;
					}
				}
			}
			// if ("subProcess".equals(element.getName()) &&
			// element.attribute("triggeredByEvent") == null) {
			// Element subProcess = xmlParent.addElement("processCall");
			// subProcess.addAttribute("target", element.attributeValue("id"));
			// subProcess.addAttribute("isAsync", "true");
			// // 创建并行子流程文件
			// createConcurrFile(filePath, element);
			// }

			// if ("transaction".equals(element.getName())) {
			// Element subProcess = xmlParent.addElement("processCall");
			// subProcess.addAttribute("target", element.attributeValue("id"));
			// subProcess.addAttribute("isAsync", "true");
			// // 创建串行子流程文件
			// createSerilFile(filePath, element);
			// }
		}
	}

	// 获取子流程第一个节点
	public Element getFirstElement(Element element) {
		if ("subProcess".equals(element.getName())) {
			if (element.attribute("triggeredByEvent") != null) {// loop判断
				if (element.elements() != null) {
					return (Element) element.elements().get(0);
				}
			} else {
				return element.element("startEvent");
			}
		}
		if ("transaction".equals(element.getName())) {
			return element.element("startEvent");
		}
		return null;
	}

	// 根据id查询对应元素
	public Element getElementById(String id) {
		List<Element> elements = mainProcessElement.elements();
		for (Element element : elements) {
			if (element.attribute("id") != null) {
				if (id.equals(element.attribute("id").getValue())) {
					return element;
				}
			}
		}
		for (Element subProcessElement : subProcessElements) {
			elements = subProcessElement.elements();
			for (Element element : elements) {
				if (element.attribute("id") != null) {
					if (id.equals(element.attribute("id").getValue())) {
						return element;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 填充loop节点属性
	 * 
	 * @param xmlElement
	 * @param bpmnElement
	 */
	public void fillLoopAttr(Element xmlElement, Element bpmnElement) {
		String id = bpmnElement.attributeValue("id");
		xmlElement.addAttribute("id", id);
		if (bpmnElement.element("extensionElements") != null) {
			List<Element> elements = bpmnElement.element("extensionElements").elements();
			if (elements != null && elements.size() == 3) {
				String set = elements.get(0).getText();
				xmlElement.addAttribute("set", set);
				String key = elements.get(1).getText();
				xmlElement.addAttribute("key", key);
				String type = elements.get(2).getText();
				xmlElement.addAttribute("type", type);
			}
		}

	}

	/**
	 * 填充switch属性
	 * 
	 * @param xmlElement
	 * @param bpmnElement
	 */
	public void fillSwitchAttr(Element xmlElement, Element bpmnElement) {
		String id = bpmnElement.attributeValue("id");
		xmlElement.addAttribute("id", id);
		if (bpmnElement.element("extensionElements") != null) {
			List<Element> elements = bpmnElement.element("extensionElements").elements();
			if (elements != null && elements.size() == 1) {
				String type = elements.get(0).getText();
				xmlElement.addAttribute("type", type);
			}
		}
	}

	public void createFile(Document doc, String fileName) throws Exception {
		File file = new File(fileName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (file.exists()) {
			file.delete();
		}
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		FileOutputStream fos = new FileOutputStream(fileName);
		XMLWriter writer = new XMLWriter(fos, format);
		writer.write(doc);
		writer.close();
	}

	/**
	 * 创建串行子流程文件
	 * 
	 * @param filePath
	 * @param element
	 */
	public void createConcurrFile(String filePath, Element process) {
		// Shell shell =
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		// try {
		// String fileName = process.attributeValue("id");
		// // 查找开始节点
		// List<Element> startEvents = process.elements("startEvent");
		// String startEventId;
		// if (startEvents != null && startEvents.size() > 0) {
		// if (startEvents.size() > 1) {
		// MessageDialog.openError(shell, "Runntime error", "startEvent number
		// must be one at root path!");
		// return;
		// }
		// startEventId = startEvents.get(0).attributeValue("id");
		// } else {
		// MessageDialog.openError(shell, "Runntime error", "startEvent required
		// in concurrent subprocess!");
		// return;
		// }
		// Document targetDoc = DocumentHelper.createDocument();
		// Element targetRoot = targetDoc.addElement("root");
		// targetRoot.addAttribute("main", fileName);
		// // 根据开始节点查询下一步
		// Element sequenceFlow = getSequenceFlow(process, startEventId);
		// if (sequenceFlow != null) {
		// String targetRef = sequenceFlow.attributeValue("targetRef");
		// if (StringUtils.isNotEmpty(targetRef)) {
		// Element firstElement = getElementById(process, targetRef);
		// if (firstElement != null) {
		// // 添加主流程
		// Element mainElement = targetRoot.addElement("processDefinition");
		// // 填充主流程
		// mainElement.addAttribute("id", "P" + fileName);
		// mainElement.addAttribute("main", "true");// 主流程标记
		// fillTargetContent(mainElement, firstElement, filePath);
		// }
		// }
		// }
		// File file = new File(filePath);
		// filePath = file.getParent() + "/" + fileName + ".xml";
		// createFile(targetDoc, filePath);
		// } catch (Exception e) {
		// e.printStackTrace();
		// MessageDialog.openError(shell, "Runntime error", "create concourrent
		// subprocess file error!");
		// }
	}

	/**
	 * 创建串行子流程文件
	 * 
	 * @param filePath
	 * @param element
	 */
	public void createSerilFile(String filePath, Element process) {
		// Shell shell =
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		// try {
		// String fileName = process.attributeValue("id");
		// // 查找开始节点
		// List<Element> startEvents = process.elements("startEvent");
		// String startEventId;
		// if (startEvents != null && startEvents.size() > 0) {
		// if (startEvents.size() > 1) {
		// MessageDialog.openError(shell, "Runntime error", "startEvent number
		// must be one at root path!");
		// return;
		// }
		// startEventId = startEvents.get(0).attributeValue("id");
		// } else {
		// MessageDialog.openError(shell, "Runntime error", "startEvent required
		// in concurrent subprocess!");
		// return;
		// }
		// Document targetDoc = DocumentHelper.createDocument();
		// Element targetRoot = targetDoc.addElement("root");
		// targetRoot.addAttribute("main", fileName);
		// // 根据开始节点查询下一步
		// Element sequenceFlow = getSequenceFlow(process, startEventId);
		// if (sequenceFlow != null) {
		// String targetRef = sequenceFlow.attributeValue("targetRef");
		// if (StringUtils.isNotEmpty(targetRef)) {
		// Element firstElement = getElementById(process, targetRef);
		// if (firstElement != null) {
		// // 添加主流程
		// Element mainElement = targetRoot.addElement("processDefinition");
		// // 填充主流程
		// mainElement.addAttribute("id", "P" + fileName);
		// mainElement.addAttribute("main", "true");// 主流程标记
		// fillTargetContent(mainElement, firstElement, filePath);
		// }
		// }
		// }
		// File file = new File(filePath);
		// filePath = file.getParent() + "/" + fileName + ".xml";
		// createFile(targetDoc, filePath);
		// } catch (Exception e) {
		// e.printStackTrace();
		// MessageDialog.openError(shell, "Runntime error", "create concourrent
		// subprocess file error!");
		// }
	}
}
