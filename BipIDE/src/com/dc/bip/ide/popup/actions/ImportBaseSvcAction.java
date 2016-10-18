package com.dc.bip.ide.popup.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.views.TreeView;
import com.dc.bip.ide.views.objects.BaseFolder;

public class ImportBaseSvcAction implements IObjectActionDelegate {
	private BaseFolder folderNode;
	
	public ImportBaseSvcAction() {
	}

	@Override
	public void run(IAction action) {
		
	try{
		
	/*	FileDialog fileDialog = new FileDialog(new Frame(), "请选择基础服务压缩文件",FileDialog.LOAD);
		fileDialog.setFilenameFilter(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".zip"))
				{
					return true;
				}
				return false;
			}			
		});
	
		fileDialog.setVisible(true);
		
		if(null == fileDialog.getDirectory() && null == fileDialog.getFile())
		{
			return;
		}
		
		String choosedFile = new StringBuilder(fileDialog.getDirectory()).append(fileDialog.getFile()).toString();
		System.out.println(choosedFile);
		*/
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(folderNode.getProjectName());
		JFileChooser fd = new JFileChooser(project.getLocation().toOSString());  
		fd.setFileSelectionMode(JFileChooser.FILES_ONLY);  
		fd.setFileFilter(new FileFilter(){

			@Override
			public boolean accept(File f) {
				if(f.isDirectory())
				{
					return true;
				}else
				{
					if(f.getName().endsWith(".zip"))
					{
						return true;
					}
					return false;
				}
			
			}

			@Override
			public String getDescription() {
				return "压缩文件(*.zip)";
			}
			
		});
		if (JFileChooser.APPROVE_OPTION ==fd.showOpenDialog(null) )
		{
			File f = fd.getSelectedFile();  
			if(f != null)
			{
				System.out.println(f.getAbsolutePath());
				unZipFile(f.getAbsolutePath());
				TreeView view = (TreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("wizard.view1");
				if(null != view)
				{
					view.reload();
				}	
			}  	
		}			
	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection)
		{
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if( structuredSelection.getFirstElement() instanceof  BaseFolder)
			{			
				folderNode = (BaseFolder)(structuredSelection.getFirstElement());
			}			
		}		
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		
	}
	
	private void unZipFile(String zipFile)
	{
		InputStream is =null;
		ZipFile zfile = null;
		try {
			    zfile=new ZipFile(zipFile);  
		        Enumeration zList=zfile.entries();  
		        ZipEntry ze=null;  
		        while(zList.hasMoreElements())
		        {  
		        	ze=(ZipEntry)zList.nextElement();         
		            if(ze.isDirectory())
		            { 
		                continue;  
		            }
		             IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(folderNode.getProjectName());
		             IFile tmpFile = null;	
		             String[] dirAndNameArr = ze.getName().split("/");
		             String name = dirAndNameArr[dirAndNameArr.length-1];
		             String baseDirStr = "/dev/services/base/";
		             String codeDirStr = "src/com/dc/branch/impls/base/";
		             //创建base文件目录
		             IFolder tmpFolder = project.getFolder(new Path(baseDirStr));
		             HelpUtil.mkdirs(tmpFolder, true, true, null);
		           //创建Java文件目录
		             tmpFolder = project.getFolder(new Path(codeDirStr));
		             HelpUtil.mkdirs(tmpFolder, true, true, null);
		             
		            if(ze.getName().endsWith(".java"))
	        		{
		            	tmpFile = project.getFile(new  Path(codeDirStr+name));
	        		}
		            else if(ze.getName().endsWith(".base1"))
		            {
		            	tmpFile = project.getFile(new  Path(baseDirStr+name));
		            }
		            else
		            {
		            	System.out.println(ze.getName());
		            	continue;
		            }				
				  is = new BufferedInputStream(zfile.getInputStream(ze));
				  if (!tmpFile.exists())
		            {
		            	tmpFile.create(is, true, null);
		            }
		            else
		            {
		            	tmpFile.setContents(is, true, false, null);
		            }
					
		        } 
			}
	        catch (IOException | CoreException e) 
	        {
					e.printStackTrace();
				}  
				finally
				{
					try {
						if(is != null)
						{
							is.close();
						}
					
							if(zfile != null)
							{
								zfile.close();
							}					
						} catch (IOException e) {
							e.printStackTrace();
						} 
					}
	}
	   
	}
	      
	
	
