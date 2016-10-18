package com.dc.bip.ide.wizards;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPage;

import com.dc.bip.ide.views.TreeView;

public class ProjectWizard extends Wizard implements INewWizard {

	private NewJavaProjectWizardPageOne fFirstPage;
	private NewJavaProjectWizardPageTwo fSecondPage;
	private IStructuredSelection selection;
	private String projectName;
	 

	public ProjectWizard() {
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {		
		projectName = fFirstPage.getProjectName();
		try {
			doFinish(projectName, null, null);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	 public boolean canFinish()
    {
        boolean flag = false;
        if(getContainer().getCurrentPage().getNextPage() == null)
            flag = true;
        return flag;
    }
	
	@SuppressWarnings("restriction")
	private void doFinish(String projectName, @SuppressWarnings("rawtypes") List list, IProgressMonitor monitor) throws CoreException
	    {
		try
		{		
	        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	        IProject proj = root.getProject(projectName);
	        
	        if(proj == null && !JavaProject.hasJavaNature(proj))
	        {
	        	System.out.println("new java project fail");
	        	return;
	        }

	        if(!proj.hasNature("SmartBranchIDE.brNature1"))
	        {
	            IProjectDescription description = proj.getDescription();
	            String prevNatures[] = description.getNatureIds();
	            String newNatures[] = new String[prevNatures.length + 1];
	            System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
	            newNatures[prevNatures.length] = "SmartBranchIDE.brNature1";
	            description.setNatureIds(newNatures);
	            System.out.println("Change the default Charset to UTF-8");
	            proj.setDefaultCharset("UTF-8", monitor);
	            proj.setDescription(description, monitor);
	        } 
		}
		catch (Exception e )
		{
			e.printStackTrace();
		}
  
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		 page = PlatformUI.getWorkbench().showPerspective("wizard.perspectives.BIPPerspective", PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		 
		 TreeView view = (TreeView)page.findView("wizard.view1");
		 view.reload();
        
	       /* getShell().getDisplay().asyncExec(new Runnable() {
	            public void run()
	            {
	                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	                String id = page.getPerspective().getId();
	                try
	                {
	                    if(!id.equals("com.dc.branch.ide.perspectives.SmartBranchPerspective"))
	                        page = PlatformUI.getWorkbench().showPerspective("com.dc.branch.ide.perspectives.SmartBranchPerspective", PlatformUI.getWorkbench().getActiveWorkbenchWindow());
	                    IViewPart view = (BrDefaultView)page.findView("com.dc.branch.ide.views.BrDefaultView");
	                    if(view instanceof BrDefaultView)
	                        ((BrDefaultView)view).reload(null);
	                }
	                catch(PartInitException partinitexception) { }
	                catch(WorkbenchException workbenchexception) { }
	            }
	          }
	        		);*/
	    }
	
	public void addPages() {
	    if(fFirstPage == null)
            fFirstPage = new NewJavaProjectWizardPageOne();
        addPage(fFirstPage);
        if(fSecondPage == null)
            fSecondPage = new NewJavaProjectWizardPageTwo(fFirstPage);
        addPage(fSecondPage);
        fFirstPage.init(selection, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart());
    }


}
