package com.dc.bip.ide.popup.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.views.objects.BSNode;

public class ExportBaseSvcAction implements IObjectActionDelegate {
	
	private BSNode bsNode;
	private final int BUFFER=1024; 

	public ExportBaseSvcAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		
		IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getProject(bsNode.getProjectName()).getFolder(new Path("/ServiceExprtport/base"));
		try {
			HelpUtil.mkdirs(folder, true, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		String outputDir =folder.getLocation().toOSString();
		System.out.println(outputDir);
		
		/*ContainerSelectionDialog dialog = new ContainerSelectionDialog(null, folder, true,"请选择导出目录");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1)
			{
				outputDir=ResourcesPlugin.getWorkspace().getRoot().getFolder((IPath) result[0]).getLocation().toOSString();
			}
		}*/
		JFileChooser fd = new JFileChooser(outputDir);  
		fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  
		fd.setFileFilter(new FileFilter(){
			@Override
			public boolean accept(File f) {
				if(f.isDirectory())
				{
					return true;
				}else
				{
					return false;
				}			
			}
			@Override
			public String getDescription() {
				return "导出文件夹";
			}			
		});
		
		if (JFileChooser.APPROVE_OPTION ==fd.showOpenDialog(null) )
		{
			File f = fd.getSelectedFile();  
			if(f != null)
			{
		      outputDir = new StringBuilder(fd.getSelectedFile().getAbsolutePath()).append("/").append(bsNode.getBaseService().getName()).append(".zip").toString();
			   try {
					   List<File> fileList= new ArrayList<File>();
					   fileList.add(bsNode.getBaseService().getBaseFile().getLocation().toFile());
					   fileList.add(bsNode.getBaseService().getCodeFile().getLocation().toFile());
					   zipBaseSvcFile(fileList,outputDir);
				} catch (CoreException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection)
		{
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if( structuredSelection.getFirstElement() instanceof  BSNode)
			{			
				    bsNode = (BSNode)(structuredSelection.getFirstElement());
			}			
		}
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}
	
	  private  void   zipBaseSvcFile(List<File> fileList,String fileName) throws Exception{          
	        ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(fileName));  
	        ZipEntry ze=null;  
	        byte[] buf=new byte[BUFFER];  
	        int readLen=0;
	        String zipDir="";
	        for(int i = 0; i <fileList.size(); i++) {  
	            File f=(File)fileList.get(i);	
	            if(f.getName().endsWith(".base1"))
	            {
	            	zipDir = "xml";
	            }else if(f.getName().endsWith(".java"))
	            {
	            	zipDir = "code";
	            }
	            else
	            {
	            	System.out.println(f.getName());
	            	continue;
	            }
	            
	            ze=new ZipEntry(zipDir+"/"+f.getName());  
	            ze.setSize(f.length());  
	            ze.setTime(f.lastModified());     
	            zos.putNextEntry(ze);  
	            InputStream is=new BufferedInputStream(new FileInputStream(f));  
	            while ((readLen=is.read(buf, 0, BUFFER))!=-1) {  
	                zos.write(buf, 0, readLen);  
	            }  
	            is.close();  
	        }  
	        zos.close();  
	    }  
	    

}
