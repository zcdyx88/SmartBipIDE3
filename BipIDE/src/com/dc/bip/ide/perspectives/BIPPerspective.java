package com.dc.bip.ide.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.jdt.ui.JavaUI;


/**
 *  This class is meant to serve as an example for how various contributions 
 *  are made to a perspective. Note that some of the extension point id's are
 *  referred to as API constants while others are hardcoded and may be subject 
 *  to change. 
 */
public class BIPPerspective implements IPerspectiveFactory {

	private IPageLayout factory;

	public BIPPerspective() {
		super();
	}

	public void createInitialLayout(IPageLayout factory) {
		this.factory = factory;
		addViews();
		addViewShortcuts();
	}

	private void addViews() {
		// Creates the overall folder layout. 
		// Note that each new Folder uses a percentage of the remaining EditorArea.
		
		IFolderLayout bottom =
			factory.createFolder(
				"bottomRight", //NON-NLS-1
				IPageLayout.BOTTOM,
				0.75f,
				factory.getEditorArea());
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView("org.eclipse.team.ui.GenericHistoryView"); //NON-NLS-1
		bottom.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);

		IFolderLayout topLeft =
			factory.createFolder(
				"topLeft", //NON-NLS-1
				IPageLayout.LEFT,
				0.25f,
				factory.getEditorArea());		
		//topLeft.addView("org.eclipse.jdt.junit.ResultView"); //NON-NLS-1
		topLeft.addView("wizard.view1"); 
		topLeft.addView(IPageLayout.ID_RES_NAV);
		
		factory.addFastView("org.eclipse.team.ccvs.ui.RepositoriesView",0.50f); //NON-NLS-1
		factory.addFastView("org.eclipse.team.sync.views.SynchronizeView", 0.50f); //NON-NLS-1
	}

	private void addViewShortcuts() {
		factory.addShowViewShortcut("org.eclipse.ant.ui.views.AntView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.team.ccvs.ui.AnnotateView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.pde.ui.DependenciesView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.jdt.junit.ResultView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.team.ui.GenericHistoryView"); //NON-NLS-1
		factory.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		factory.addShowViewShortcut(JavaUI.ID_PACKAGES);
		factory.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		factory.addShowViewShortcut(IPageLayout.ID_OUTLINE);
	}

}
