package org.activiti.designer.bip.palette;


import java.util.ArrayList;
import java.util.List;

import org.activiti.designer.integration.palette.AbstractDefaultPaletteCustomizer;
import org.activiti.designer.integration.palette.PaletteEntry;

public class MyPaletteCustomizer extends AbstractDefaultPaletteCustomizer {

	public List<PaletteEntry> disablePaletteEntries() {
		List<PaletteEntry> result = new ArrayList<PaletteEntry>();
//		  result.add(PaletteEntry.ALL);
//		result.add(PaletteEntry.START_EVENT);
		result.add(PaletteEntry.TIMER_START_EVENT);
		result.add(PaletteEntry.MESSAGE_START_EVENT);
		result.add(PaletteEntry.ERROR_START_EVENT);
		result.add(PaletteEntry.SIGNAL_START_EVENT);
		result.add(PaletteEntry.ALFRESCO_START_EVENT);
//		result.add(PaletteEntry.END_EVENT);
		result.add(PaletteEntry.ERROR_END_EVENT);
		result.add(PaletteEntry.TERMINATE_END_EVENT);
		result.add(PaletteEntry.CANCEL_END_EVENT);
		result.add(PaletteEntry.EXCLUSIVE_GATEWAY);
		result.add(PaletteEntry.INCLUSIVE_GATEWAY);
		result.add(PaletteEntry.EVENT_GATEWAY);
		result.add(PaletteEntry.PARALLEL_GATEWAY);
		result.add(PaletteEntry.MAIL_TASK);
		result.add(PaletteEntry.MANUAL_TASK);
		result.add(PaletteEntry.RECEIVE_TASK);
		result.add(PaletteEntry.SCRIPT_TASK);
		result.add(PaletteEntry.SERVICE_TASK);
		result.add(PaletteEntry.SEND_TASK);
		result.add(PaletteEntry.BUSINESSRULE_TASK);
		result.add(PaletteEntry.USER_TASK);
		result.add(PaletteEntry.CALL_ACTIVITY);
//		result.add(PaletteEntry.SUBPROCESS);
//		result.add(PaletteEntry.POOL);
//		result.add(PaletteEntry.LANE);
//		result.add(PaletteEntry.EVENT_SUBPROCESS);
//		result.add(PaletteEntry.TRANSACTION);
		result.add(PaletteEntry.ALFRESCO_USER_TASK);
		result.add(PaletteEntry.ALFRESCO_SCRIPT_TASK);
		result.add(PaletteEntry.ALFRESCO_MAIL_TASK);
		result.add(PaletteEntry.BOUNDARY_TIMER);
		result.add(PaletteEntry.BOUNDARY_ERROR);
		result.add(PaletteEntry.BOUNDARY_MESSAGE);
		result.add(PaletteEntry.BOUNDARY_SIGNAL);
		result.add(PaletteEntry.BOUNDARY_CANCEL);
		result.add(PaletteEntry.BOUNDARY_COMPENSATION);
		result.add(PaletteEntry.CATCH_TIMER);
		result.add(PaletteEntry.CATCH_SIGNAL);
		result.add(PaletteEntry.CATCH_MESSAGE);
		result.add(PaletteEntry.THROW_SIGNAL);
		result.add(PaletteEntry.THROW_COMPENSATION);
		result.add(PaletteEntry.THROW_NONE);
		result.add(PaletteEntry.TEXT_ANNOTATION);
		  return result;
	}

}
