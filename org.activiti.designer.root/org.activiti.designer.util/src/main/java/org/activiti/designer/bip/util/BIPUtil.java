package org.activiti.designer.bip.util;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.designer.bip.event.SwitchEvent;
import org.activiti.designer.bip.palette.MyPaletteCustomizer;
import org.activiti.designer.bip.task.BaseTask;
import org.activiti.designer.bip.task.BusinessTask;
import org.activiti.designer.integration.palette.DefaultPaletteCustomizer;
import org.activiti.designer.integration.palette.PaletteEntry;
import org.activiti.designer.integration.servicetask.CustomServiceTask;
import org.activiti.designer.integration.servicetask.CustomServiceTaskDescriptor;
import org.activiti.designer.integration.usertask.CustomUserTask;
import org.activiti.designer.integration.usertask.CustomUserTaskDescriptor;
import org.activiti.designer.util.extension.CustomServiceTaskContext;
import org.activiti.designer.util.extension.CustomServiceTaskContextImpl;
import org.activiti.designer.util.extension.CustomUserTaskContext;
import org.activiti.designer.util.extension.CustomUserTaskContextImpl;
import org.eclipse.core.resources.IProject;

public class BIPUtil {

	private static final Class bipCustomizer = MyPaletteCustomizer.class;
	
	private static final List<Class> bipCustomServiceTasks = new ArrayList<Class>();
	
	static {
		bipCustomServiceTasks.add(BaseTask.class);
		bipCustomServiceTasks.add(BusinessTask.class);
		bipCustomServiceTasks.add(SwitchEvent.class);
	}

	public static List<CustomServiceTaskDescriptor> providedCustomServiceTaskDescriptors;

	public static List<CustomUserTaskDescriptor> providedCustomUserTaskDescriptors;
	
	public static void addProvidedCustomServiceTaskDescriptors(List<CustomServiceTaskDescriptor> descriptors) {
	    if (providedCustomServiceTaskDescriptors == null) {
	      providedCustomServiceTaskDescriptors = new ArrayList<CustomServiceTaskDescriptor>();
	    }
	    providedCustomServiceTaskDescriptors.addAll(descriptors);
	  }
	  
	  public static void addProvidedCustomUserTaskDescriptors(List<CustomUserTaskDescriptor> descriptors) {
	    if (providedCustomUserTaskDescriptors == null) {
	      providedCustomUserTaskDescriptors = new ArrayList<CustomUserTaskDescriptor>();
	    }
	    providedCustomUserTaskDescriptors.addAll(descriptors);
	  }

	public static final Set<PaletteEntry> getDisabledPaletteEntries(IProject project) {

		Set<PaletteEntry> result = new HashSet<PaletteEntry>();

		Class<DefaultPaletteCustomizer> clazz = bipCustomizer;
		DefaultPaletteCustomizer DefaultPaletteCustomizer;
		try {
			DefaultPaletteCustomizer = (DefaultPaletteCustomizer) clazz.newInstance();
			result.addAll(DefaultPaletteCustomizer.disablePaletteEntries());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static List<CustomUserTaskContext> getCustomUserTaskContexts(final IProject project) {

		List<CustomUserTaskContext> result = new ArrayList<CustomUserTaskContext>();

//		addToCustomUserTasks(result);
//		
//		String extensionName = "BIP";
//		
//		for(Class<CustomServiceTask> customServiceTaskClazz : bipCustomServiceTasks){
//			try {
//				CustomUserTask customUserTask = (CustomUserTask) customServiceTaskClazz
//						.newInstance();
//				// Add this
//				// CustomServiceTask
//				// to the result,
//				// wrapped in its
//				// context
//				result.add(new CustomUserTaskContextImpl(customUserTask,
//						extensionName, null));
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		return result;
	}
	
	
	public static List<CustomServiceTaskContext> getCustomServiceTaskContexts(final IProject project) {
		List<CustomServiceTaskContext> result = new ArrayList<CustomServiceTaskContext>();
		
		String extensionName = "BIP";
		
		for(Class<CustomServiceTask> customServiceTaskClazz : bipCustomServiceTasks){
			try {
                CustomServiceTask customServiceTask = (CustomServiceTask) customServiceTaskClazz.newInstance();
                // Add this CustomServiceTask to the result, wrapped in its context
//                String jarPath = BIPUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile(); 
                String jarPath = null;
                result.add(new CustomServiceTaskContextImpl(customServiceTask, extensionName, jarPath));
              } catch (Exception e) {
                e.printStackTrace();
              }
		}
		return result;
	}

	private static void addToCustomUserTasks(List<CustomUserTaskContext> result) {
		if (providedCustomUserTaskDescriptors != null) {
			for (CustomUserTaskDescriptor dscr : providedCustomUserTaskDescriptors) {
				Class<? extends CustomUserTask> clazz = dscr.getClazz();
				if (clazz != null && !Modifier.isAbstract(clazz.getModifiers())
						&& CustomUserTask.class.isAssignableFrom(clazz)) {
					try {
						CustomUserTask customUserTask = (CustomUserTask) clazz.newInstance();
						result.add(new CustomUserTaskContextImpl(customUserTask, dscr.getExtensionName(),
								dscr.getExtensionJarPath()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
