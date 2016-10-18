package com.dc.bip.ide.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.util.HelpUtil;

public class BaseSvcService {
	public static List<BaseService> getBaseSVCList(IProject project){
		List<BaseService> list = new ArrayList<BaseService>();
		
		String folderPath = project.getFolder("/dev/services/base/").getLocation().toString();
		File baseFolder = new File(folderPath);
		 FilenameFilter fileNameFilter = new FilenameFilter() {			   
	            @Override
	            public boolean accept(File dir, String name) {
	               if(name.lastIndexOf('.')>0)
	               {
	                  int lastIndex = name.lastIndexOf('.');
	                  String str = name.substring(lastIndex);
	                  if(str.equals(".base1"))
	                  {
	                     return true;
	                  }
	               }
	               return false;
	            }
	         };
		
		File[] baseFiles = baseFolder.listFiles(fileNameFilter);
		if (baseFiles != null && baseFiles.length>0)
		{
			for(int i = 0 ;i<baseFiles.length;i++)
			{
				try {
					BaseService bs = HelpUtil.parseBaseSvcXml(new FileInputStream(baseFiles[i]));
					list.add(bs);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
}
