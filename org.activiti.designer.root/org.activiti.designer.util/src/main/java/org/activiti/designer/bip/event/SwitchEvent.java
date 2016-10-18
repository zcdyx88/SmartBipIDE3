package org.activiti.designer.bip.event;


import org.activiti.designer.integration.DiagramBaseShape;
import org.activiti.designer.integration.annotation.Property;
import org.activiti.designer.integration.servicetask.AbstractCustomServiceTask;
import org.activiti.designer.integration.servicetask.PropertyType;

public class SwitchEvent  extends AbstractCustomServiceTask {
	
	@Property(type = PropertyType.TEXT, displayName = "switch type=''", required = true, defaultValue = "operation")
	  private String type;
	
	 @Override
	  public String contributeToPaletteDrawer() {
	    return "逻辑单元";
	  }
	
	@Override
	public String getName() {
		return "switch";
	}

	public DiagramBaseShape getDiagramBaseShape() {
		return DiagramBaseShape.GATEWAY;
	}
	
	  @Override
	  public String getSmallIconPath() {
	    return "icons/switch.gif";
	  }
}
