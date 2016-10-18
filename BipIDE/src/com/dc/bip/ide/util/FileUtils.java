package com.dc.bip.ide.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

public class FileUtils {
	public static  IFile getFileFromWorkSpace(String fileFolderPath){
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		Path basePPath = new Path(fileFolderPath);
		return root.getFile(basePPath);
	}
	
	public static List<File> getFilesFromWorkSpace(String dir){
		List<File> files = new ArrayList<File>();
		IFile dirFile = getFileFromWorkSpace(dir);  
		if(null != dirFile){
			File localFile = dirFile.getLocation().toFile();
			if(localFile.isDirectory()){
				files = Arrays.asList(localFile.listFiles()); 
			}
		}
		return files;
	}
	
	public static IFile getResource(String resourcePath) {
		IFile resource = null;
		try {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			Path basePPath = new Path(resourcePath);
			final IFile basefile = root.getFile(basePPath);
			resource = basefile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resource;
	}
	
	public static File getLocalResource(String resourcePath) {
		File resource = null;
		try {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			Path basePPath = new Path(resourcePath);
			final IFile basefile = root.getFile(basePPath);
			String abPath = basefile.getLocation().toOSString();
			resource = new File(abPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resource;
	}
}
