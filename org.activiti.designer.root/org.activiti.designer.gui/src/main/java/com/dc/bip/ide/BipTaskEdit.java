package com.dc.bip.ide;

import org.activiti.bpmn.model.EventSubProcess;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Pool;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.designer.diagram.ActivitiBPMNFeatureProvider;
import org.activiti.designer.util.CloneUtil;
import org.activiti.designer.util.editor.BpmnMemoryModel;
import org.activiti.designer.util.editor.ModelHandler;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class BipTaskEdit {
	public static String projectName;
	private static final String BASE_TASK = "org.activiti.designer.bip.task.BaseTask";
	private static final String BUSINESS_TASK = "org.activiti.designer.bip.task.BusinessTask";
	private static final String SWITCH = "org.activiti.designer.bip.event.SwitchEvent";

	public static void editTask(Diagram diagram, Object object) {
		// CloneUtil.clone((FlowElement)object, diagram);
		BpmnMemoryModel model = ModelHandler.getModel(EcoreUtil.getURI(diagram));
		if (model != null) {
			ActivitiBPMNFeatureProvider pro = (ActivitiBPMNFeatureProvider) model.getFeatureProvider();
		}
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (object instanceof ServiceTask) {
			ServiceTask task = (ServiceTask) object;
			String taskExtId = task.getExtensionId();
			if (BASE_TASK.equals(taskExtId)) {
				// 基础服务
				BaseSvcDialog editDialog = new BaseSvcDialog(diagram, shell, object);
				editDialog.open();
			}
			if (BUSINESS_TASK.equals(taskExtId)) {
				// 业务服务
				BusiSvcDialog editDialog = new BusiSvcDialog(diagram, shell, object);
				editDialog.open();
			}
			if (SWITCH.equals(taskExtId)) {
				SwitchDialog editDialog = new SwitchDialog(shell, object);
				editDialog.open();
			}
		} else if (object instanceof EventSubProcess) {
			LoopDialog editDialog = new LoopDialog(shell, object);
			editDialog.open();
		} else if (object instanceof EventSubProcess) {
			LoopDialog editDialog = new LoopDialog(shell, object);
			editDialog.open();
		} else if (object instanceof SequenceFlow) {
			SequenceFlow flow = (SequenceFlow) object;
			// BpmnMemoryModel model =
			// ModelHandler.getModel(EcoreUtil.getURI(diagram));
			// 如果前一个节点是switch
			FlowElement sourceElement = model.getFlowElement(flow.getSourceRef());
			if (sourceElement instanceof ServiceTask && SWITCH.equals(((ServiceTask) sourceElement).getExtensionId())) {
				SwitchCaseDialog dialog = new SwitchCaseDialog(shell, object);
				dialog.open();
			} else {
				CommonDialog dialog = new CommonDialog(shell, object);
				dialog.open();
			}
		}else if(object instanceof Pool){
			PoolDialog poolDialog = new PoolDialog(shell, object);
			poolDialog.open();
		} else {
			CommonDialog dialog = new CommonDialog(shell, object);
			dialog.open();
		}
	}
}
