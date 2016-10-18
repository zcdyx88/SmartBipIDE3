package com.dc.bip.ide.wizards.busi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.SoapWsUtil;





//业务服务向导
public class BusiSvcW extends Wizard implements INewWizard{
	
	private String projectName;
	
	public BusiSvcW(String projectName) {  
        this.setDialogSettings(new DialogSettings("导入工程")); 
        this.projectName= projectName;
    }  
    @Override  
    public void addPages() { 
        this.addPage(new BusiSvcWizardP());  
        //this.addPage(new BaseSvcWizardP2());	        
    }  
    @Override  
    public boolean performFinish() {  
        IDialogSettings dialogSettings2 = this.getDialogSettings();  
        final String opName = dialogSettings2.get("opName"); 
        final String  url = dialogSettings2.get("url");
        final String  serviceName = dialogSettings2.get("serviceName");
        if(opName.length() > 0)
        {
        	try {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);	
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				
				 //创建文件
				String  fileFolderStr = new StringBuilder("/").append(projectName).append("/dev/services/busi/").toString();
		        String  fileStr = (new StringBuilder(fileFolderStr)).append(opName+".wsdl").toString();
		        
		        IFolder folder = root.getFolder(new Path(fileFolderStr));
		        if(folder != null && !folder.exists())
		            HelpUtil.mkdirs(folder, true, true, null);
		        Path basePPath = new Path(fileStr);
		        IResource baseResource = root.findMember(basePPath);
		        if(baseResource != null && baseResource.exists())
		        {
		            return false;
		        }
		        
		        final IFile basefile = root.getFile(basePPath);		        
//			       IFile java_file = project.getFile(new Path("/"+opName+".wsdl"));
		       SoapWsUtil soapUtil = new SoapWsUtil();
		       String str = soapUtil.getRequstStr(url, serviceName, opName);
		       InputStream inputStreamJava = new ByteArrayInputStream((str).getBytes());			     
		       basefile.create(inputStreamJava, false, null);			       
				IDE.openEditor(page,basefile, "wizard.editor.busiEditor");
				
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
   
        return true;  
    }  
    
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
	}
    
    class LongRunningOperation implements IRunnableWithProgress {  
        // The total sleep time  
        private static final int TOTAL_TIME = 10000;  
        // The increment sleep time  
        private static final int INCREMENT = 500;  
        private boolean indeterminate;  
         
        public LongRunningOperation(boolean indeterminate) {  
            this.indeterminate = indeterminate;  
        }  
         
        public void run(IProgressMonitor monitor) throws InvocationTargetException,  
                InterruptedException {  
            monitor.beginTask("Running long running operation",  
                    indeterminate ? IProgressMonitor.UNKNOWN : TOTAL_TIME);  
            // monitor.subTask("Doing first half");  
            for (int total = 0; total < TOTAL_TIME && !monitor.isCanceled(); total += INCREMENT) {  
                Thread.sleep(INCREMENT);  
                monitor.worked(INCREMENT);  
                if (total == TOTAL_TIME / 2)  
                    monitor.subTask("Doing second half");  
            }  
            monitor.done();  
            if (monitor.isCanceled())  
                throw new InterruptedException(  
                        "The long running operation was cancelled");  
        } 
     }

}
