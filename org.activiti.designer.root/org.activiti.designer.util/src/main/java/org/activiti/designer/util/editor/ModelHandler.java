package org.activiti.designer.util.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;

public class ModelHandler {

	private static Map<URI, BpmnMemoryModel> modelMap = new HashMap<URI, BpmnMemoryModel>();
	private static Map<URI, KickstartProcessMemoryModel> kickstartProcessModelMap = new HashMap<URI, KickstartProcessMemoryModel>();
	private static Map<URI, KickstartFormMemoryModel> kickstartFormModelMap = new HashMap<URI, KickstartFormMemoryModel>();

	public static void addModel(URI uri, Object model) {
	  if (model instanceof BpmnMemoryModel) {
	    modelMap.put(uri, (BpmnMemoryModel) model);
	  } else if (model instanceof KickstartProcessMemoryModel) {
	    kickstartProcessModelMap.put(uri, (KickstartProcessMemoryModel) model);
	  } else if(model instanceof KickstartFormMemoryModel) {
	    kickstartFormModelMap.put(uri, (KickstartFormMemoryModel) model);
	  }
	}

	public static BpmnMemoryModel getModel(URI uri) {
		return modelMap.get(uri);
	}
	
	public static KickstartProcessMemoryModel getKickstartProcessModel(URI uri) {
	  return kickstartProcessModelMap.get(uri);
    }
	
	public static KickstartFormMemoryModel getKickstartFormMemoryModel(URI uri) {
	  return kickstartFormModelMap.get(uri);
	}

	public static void removeModel(URI uri) {
		modelMap.remove(uri);
	}
	
	public static void removeKickstartProcessModel(URI uri) {
	  kickstartProcessModelMap.remove(uri);
    }
	
	public static void removeKickstartFormModel(URI uri) {
      kickstartFormModelMap.remove(uri);
    }
}
