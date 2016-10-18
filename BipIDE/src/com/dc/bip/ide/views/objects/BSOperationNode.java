package com.dc.bip.ide.views.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.PlatformUI;

import com.dc.bip.ide.common.model.ConfigFile;
import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.views.TreeView;

public class BSOperationNode extends TreeNode implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nodeName;
	private BusiSvcInfo busiSvcInfo;
	private String resourcePath;

	@Override
	public void addChild(TreeNode treeNode) {
		super.addChild(treeNode);
	}

	public String getPersistFolder() {
		String fileFolderStr = new StringBuilder("/").append(getProjectName()).append("/dev/services/busi/").toString();
		return fileFolderStr;
	}

	public String getPersistPath() {
		String fileStr = new StringBuilder(getPersistFolder()).append(getNodeName() + ".busi").toString();
		return fileStr;
	}

	public String getPersistLocalFile() {
		return getResource().getLocation().toOSString();
	}

	public void persist() {
		ObjectOutputStream outputStream = null;
		try {
			final IFile busiSvcNodeFile = getResource();
			if (!busiSvcNodeFile.exists()) {
				busiSvcNodeFile.create(null, false, null);
			}
			File file = new File(getPersistLocalFile());
			outputStream = new ObjectOutputStream(new FileOutputStream(file));
			outputStream.writeObject(this);
			outputStream.flush();
		} catch (Exception e) {
			// TODO show error msg
			e.printStackTrace();
		} finally {
			if (null != outputStream) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean dispose(){
		TreeView view = (TreeView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView("wizard.view1");
		if (null != view) {
			this.getParent().removeChild(this);
			view.getViewer().remove(this);
		}
		List<ConfigFile> configFiles = this.busiSvcInfo.getConfigs();
		for(ConfigFile configFile : configFiles){
			configFile.delete();
		}
		File file = new File(getPersistLocalFile());
		return file.getAbsoluteFile().delete();
	}

	public IFile getResource() {
		IFile resource = null;
		try {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IFolder folder = root.getFolder(new Path(getPersistFolder()));
			if (folder != null && !folder.exists())
				HelpUtil.mkdirs(folder, true, true, null);
			Path basePPath = new Path(getPersistPath());
			IResource baseResource = root.findMember(basePPath);
			final IFile basefile = root.getFile(basePPath);
			resource = basefile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resource;
	}

	public BSOperationNode(String nodeName) {
		this.nodeName = nodeName;
	}

	public BSOperationNode() {
	}

	public BusiSvcInfo getBusiSvcInfo() {
		return busiSvcInfo;
	}

	public void setBusiSvcInfo(BusiSvcInfo busiSvcInfo) {
		this.busiSvcInfo = busiSvcInfo;
	}

	public String getNodeName() {
		return nodeName;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourePath) {
		this.resourcePath = resourePath;
	}
}
