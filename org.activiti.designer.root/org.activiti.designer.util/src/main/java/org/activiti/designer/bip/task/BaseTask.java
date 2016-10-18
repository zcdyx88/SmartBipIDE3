package org.activiti.designer.bip.task;

/**
 * 
 */

import org.activiti.designer.integration.annotation.Help;
import org.activiti.designer.integration.annotation.Property;
import org.activiti.designer.integration.annotation.PropertyItems;
import org.activiti.designer.integration.annotation.TaskName;
import org.activiti.designer.integration.annotation.TaskNames;
import org.activiti.designer.integration.servicetask.AbstractCustomServiceTask;
import org.activiti.designer.integration.servicetask.PropertyType;

/**
 * Defines the Money Task node.
 * 
 * @author John Doe
 * @version 1
 * @since 1.0.0
 */
@Help(displayHelpShort = "创建一个新的基础服务任务", displayHelpLong = "从现有基础服务中选择一个添加到流程")
@TaskNames({ @TaskName(locale = "ben", name = "English account"), })
public class BaseTask extends AbstractCustomServiceTask {

	@Property(type = PropertyType.BASESVC_CHOICE, displayName = "serviceId", required = true, defaultValue = "")
	@Help(displayHelpShort = "choose a business task", displayHelpLong = "")
	@PropertyItems({ "4001000000101", "3001000000101" })
	private String serviceId;

	@Override
	public String contributeToPaletteDrawer() {
		return "服务";
	}

	public String getName() {
		return "基础服务";
	}

	@Override
	public String getSmallIconPath() {
		return "icons/2.png";
	}

}
