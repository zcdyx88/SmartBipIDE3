package com.dc.bip.ide.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class PlatformUtils {
	
	public static void showInfo(String title, String msg){
		Shell shell = PlatformUI.getWorkbench().getModalDialogShellProvider().getShell();
		MessageDialog.openInformation(shell, title, msg);
	}
	
	public static boolean showConfirm(String title, String msg){
		Shell shell = PlatformUI.getWorkbench().getModalDialogShellProvider().getShell();
		return MessageDialog.openConfirm(shell, title, msg);
	}

}
