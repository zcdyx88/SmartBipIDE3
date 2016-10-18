package com.dc.bip.ide.wizards.busi;

import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.dc.bip.ide.common.model.ConfigFile;
import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.views.objects.BSOperationNode;

public class CompConfigAddWizard extends Wizard {

	private CompositeService svcInfo;
//	private BSOperationNode operationNode;
	//保存需要更新table
	private Table table;

	public CompConfigAddWizard(CompositeService svcInfo, Table table) {
		this.svcInfo = svcInfo;
		this.table = table;
		setWindowTitle("新增服务配置");
	}

	@Override
	public void addPages() {
		this.addPage(new CompConfigAddWizardPage(this.svcInfo));
	}

	@Override
	public boolean performFinish() {
		IWizardPage[] pages = this.getPages();
		if (null != pages && pages.length > 0) {
			try {
				CompConfigAddWizardPage page = (CompConfigAddWizardPage) pages[0];
				String configName = page.getConfigName();
				String fileFolderStr = new StringBuilder("/").append(svcInfo.getProject().getName())
						.append("/dev/services/composite/").append(svcInfo.getBaseinfo().getServiceId()).append("/")
						.toString();
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IFolder folder = root.getFolder(new Path(fileFolderStr));
				if (folder != null && !folder.exists()) {
					HelpUtil.mkdirs(folder, true, true, null);
				}
				String fileStr = (new StringBuilder(fileFolderStr)).append(configName).toString();
				Path basePPath = new Path(fileStr);
				IResource baseResource = root.findMember(basePPath);
				if (baseResource != null && baseResource.exists()) {
					// show msg
				} else {
					final IFile basefile = root.getFile(basePPath);
					basefile.create(null, false, null);
					File file = new File(basefile.getLocation().toOSString());
					FileOutputStream outputStream = null;
					try {
						outputStream = new FileOutputStream(file);
						outputStream.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						if(null != outputStream){
							try {
								outputStream.close();
							} catch (Exception e2) {
								e2.printStackTrace();
							}
						}
					}
					ConfigFile configFile = new ConfigFile();
					configFile.setName(configName);
					configFile.setType(page.getConfigType());
					configFile.setPath(fileStr);
					svcInfo.addConfig(configFile);
					
//					operationNode.persist();
					TableItem tableItem = new TableItem(table, SWT.NONE);
					tableItem.setText(0, configFile.getName());
					tableItem.setText(1, configFile.getType());
					tableItem.setText(2, configFile.getPath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
