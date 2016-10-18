package com.dc.bip.ide;

import java.util.List;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.FlowElement;

public class FlowElementUtil {
	
	public static void saveAttr(BaseElement esp, String key, String value){
		List<ExtensionElement> ees = esp.getExtensionElements().get(key);
		if(ees != null && ees.size() > 0){
			ExtensionElement ee = ees.get(0);
			if(ee != null){
				ee.setElementText(value);
			}
		}else{
			ExtensionElement ee = new ExtensionElement();
			ee.setName(key);
			ee.setElementText(value);
			esp.addExtensionElement(ee);
		}

	}
	
	public static String getAttrValue(BaseElement esp, String key){
		List<ExtensionElement> ees = esp.getExtensionElements().get(key);
		if(ees != null && ees.size() > 0){
			ExtensionElement ee = ees.get(0);
			if(ee != null){
				return ee.getElementText();
			}
		}
		return "";
	}

}
