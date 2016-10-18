package org.activiti.designer.bip.task;


import org.activiti.designer.integration.annotation.Help;
import org.activiti.designer.integration.annotation.Property;
import org.activiti.designer.integration.annotation.PropertyItems;
import org.activiti.designer.integration.annotation.TaskName;
import org.activiti.designer.integration.annotation.TaskNames;
import org.activiti.designer.integration.servicetask.AbstractCustomServiceTask;
import org.activiti.designer.integration.servicetask.PropertyType;

@Help(displayHelpShort = "新建一个业务服务", displayHelpLong = "")
@TaskNames({ @TaskName(locale = "en", name = "business task"), @TaskName(locale = "de", name = "service task") })
public class BusinessTask extends AbstractCustomServiceTask {

	@Property(type = PropertyType.BUSISVC_CHOICE, displayName = "serviceId", required = true, defaultValue = "")
	@Help(displayHelpShort = "choose a business task", displayHelpLong = "")
	@PropertyItems({ "4001000000101", "3001000000101" })
	private String serviceId;

	@Override
	public String contributeToPaletteDrawer() {
		return "服务";
	}

	@Override
	public String getName() {
		return "业务服务";
	}

	@Override
	public String getSmallIconPath() {
		return "icons/3.png";
	}
}
