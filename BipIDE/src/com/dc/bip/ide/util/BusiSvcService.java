package com.dc.bip.ide.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.views.objects.BSOperationNode;

public class BusiSvcService {
	
	List<BusiSvcInfo> list = null;
	 
	public static List<BusiSvcInfo> getAll(String projectName){
		List<BusiSvcInfo> list = new ArrayList<BusiSvcInfo>();
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		
		String busiPath = project.getFolder(BipConstantUtil.BusiSvcPath).getLocation().toString();
		// 杩囨护涓氬姟鏈嶅姟鏂囦欢
 		File busiFileFolder = new File(busiPath);
 		FilenameFilter busiFileNameFilter = new FilenameFilter() {
 			@Override
 			public boolean accept(File dir, String name) {
 				if (name.lastIndexOf('.') > 0) {
 					int lastIndex = name.lastIndexOf('.');
 					String str = name.substring(lastIndex);
 					if (str.equals(".busi")) {
 						return true;
 					}
 				}
 				return false;
 			}
 		};
 		
 		File[] busiFiles = busiFileFolder.listFiles(busiFileNameFilter);
		if (busiFiles != null && busiFiles.length > 0) {
			for (int i = 0; i < busiFiles.length; i++) {
				try {
					IFile resource = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(busiFiles[i].toURI())[0];
					File file = resource.getLocation().toFile();
					FileInputStream in = new FileInputStream(file);
					ObjectInputStream objectInputStream = new ObjectInputStream(in);
					BSOperationNode node = (BSOperationNode) objectInputStream.readObject();
					list.add(node.getBusiSvcInfo());					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	public static void main(String[] args){
		String projectPath = BusiSvcService.class.getClass().getResource("/").getFile().toString();
		System.out.println(projectPath);
		File file = new File(projectPath);
		System.out.println(file.getParentFile().getPath());
	}
}
