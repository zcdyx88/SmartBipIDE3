package com.dc.bip.ide.wizards.basesvc;

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
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.util.PlatformUtils;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BSNode;
import com.dc.bip.ide.views.objects.BaseFolder;
import com.dc.bip.ide.views.objects.ProjectNode;
import com.dc.bip.ide.views.objects.TreeNode;

public class BaseSvcW extends Wizard implements INewWizard {
	 private String projectName;
	  
	  public BaseSvcW(String projectName) {  
	        this.setDialogSettings(new DialogSettings("导入工程"));  
	        this.projectName = projectName;
	    }  
	    @Override  
	    public void addPages() { 
	        this.addPage(new BaseSvcWizardP());  
	    }  
	    @Override  
	    public boolean performFinish() {  
	    	BaseService bs =( (BaseSvcWizardP)this.getPage("baseSvcWizarfPage1")).getBaseService();
	    	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	        if(bs.getImpls().length() > 0)
	        {
	        	try {	        		
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
					IProject project = root.getProject(projectName);
					
					
					//创建.java文件
			        String packageName = bs.getImpls().substring(0, bs.getImpls().lastIndexOf("."));
			        HelpUtil.buildPackage(projectName, "src", packageName);
			        String path = (new StringBuilder("/")).append(projectName).append("/src/").append(bs.getImpls().replaceAll("\\.", "/")).append(".java").toString();
			        Path pPath = new Path(path);
			        IResource resource = root.findMember(pPath);
			        IFile javafile = root.getFile(pPath);
			        if(resource == null || !resource.exists())
			        {			             
			            try
			            {
			                InputStream stream = null;			             
			                if(stream == null)
			                    stream = HelpUtil.openContentStream(bs, "base_src.vm");			                	
			                if(javafile.exists())
			                {
			                    if(javafile.isReadOnly())
			                    	javafile.setReadOnly(false);
			                    javafile.setContents(stream, true, true, null);
			                } else
			                {
			                	javafile.create(stream, true, null);
			                }
			                bs.setCodeFile(javafile);
			                stream.close();
			            }
			            catch(Exception e)
			            {
			                System.out.println(e);
			            }
			        }else{
			        	boolean confirm = PlatformUtils.showConfirm("提示", "存在同名文件，请先手动删除。是否关闭创建页面？");
						if (confirm == true){
							return true;
						}else{
							return false;
						}
			        }
			        
			        //创建.base文件
			        String  baseFilePath = (new StringBuilder("/")).append(projectName).append("/dev/services/base/").append(bs.getName()).append(".base1").toString();
			        IFolder folder = root.getFolder(new Path((new StringBuilder("/")).append(projectName).append("/dev/services/base/").toString()));
			        if(folder != null && !folder.exists())
			            HelpUtil.mkdirs(folder, true, true, null);
			        Path basePPath = new Path(baseFilePath);
			        IResource baseResource = root.findMember(basePPath);
			        if(baseResource != null && baseResource.exists())
			        {
			            return false;
			        }
			        
			        final IFile basefile = root.getFile(basePPath);
			        try
			        {			        	
			        	 InputStream stream = HelpUtil.openContentStream(bs, "base.vm");
			            if(basefile.exists())
			            {
			            	basefile.setContents(stream, true, true, null);
			            } else
			            {			              
			            	basefile.create(stream, true, null);
			            }
			            bs.setBaseFile(basefile);
			            stream.close();
			        }
			        catch(Exception e)
			        {
			        	e.printStackTrace();
			        }
			        
					TreeView view = (TreeView)page.findView("wizard.view1");
					if(null != view)
					{
						BSNode bn = new BSNode(bs.getName());
						
						bn.setBaseService(bs);
						BaseFolder baseFolder  = null;
						for(TreeItem item: view.getViewer().getTree().getItems())
						{
							if(item.getText().equalsIgnoreCase(projectName))
							{
								ProjectNode tmpProjectNode = (ProjectNode)item.getData();
								for( TreeNode tmpNode : tmpProjectNode.getChildren())
								{
									if(tmpNode instanceof BaseFolder)
									{ 
										baseFolder = (BaseFolder)tmpNode;
										break;
									}
								}
								break;
							}							
						}
						if(baseFolder != null)
						{
							baseFolder.addChild(bn);	
							view.getViewer().add(baseFolder, bn);
						}						
					}					 
					 
					IDE.openEditor(page, basefile);	
					
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
	      /*  IRunnableWithProgress op = new IRunnableWithProgress() {  
	            public void run(IProgressMonitor monitor)  
	                    throws InvocationTargetException {  
	                try {  
	                    MessageDialog.openConfirm(BaseSvcW.this.getShell(),  
	                            "请确认以下信息", "username=" + implement);  
	                } catch (Exception e) {  
	                    throw new InvocationTargetException(e);  
	                } finally {  
	                    monitor.done();  
	                }  
	            }  
	        };  
	        try {  
	            getContainer().run(true, true, new LongRunningOperation(false));  
	        } catch (InterruptedException e) {  
	            return false;  
	        } catch (InvocationTargetException e) {  
	            Throwable realException = e.getTargetException();  
	            MessageDialog.openError(getShell(), "Error", realException  
	                    .getMessage());  
	            return false;  
	        }  */
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
