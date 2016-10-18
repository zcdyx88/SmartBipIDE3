package com.dc.bip.ide.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.dc.bip.ide.common.model.Params;
import com.dc.bip.ide.objects.BaseService;
import com.dc.bip.ide.objects.CompositeService;
import com.dc.bip.ide.util.BipConstantUtil;
import com.dc.bip.ide.util.HelpUtil;
import com.dc.bip.ide.views.objects.BSNode;
import com.dc.bip.ide.views.objects.BSOperationNode;
import com.dc.bip.ide.views.objects.BaseFolder;
import com.dc.bip.ide.views.objects.BipServerFolder;
import com.dc.bip.ide.views.objects.BipServerNode;
import com.dc.bip.ide.views.objects.BusiFolder;
import com.dc.bip.ide.views.objects.CompositeFolder;
import com.dc.bip.ide.views.objects.CompositeNode;
import com.dc.bip.ide.views.objects.ExpressionFolder;
import com.dc.bip.ide.views.objects.ExpressionNode;
import com.dc.bip.ide.views.objects.FlowFolder;
import com.dc.bip.ide.views.objects.FlowNode;
import com.dc.bip.ide.views.objects.ProjectNode;
import com.dc.bip.ide.views.objects.ProtocolFolder;
import com.dc.bip.ide.views.objects.ProtocolInFolder;
import com.dc.bip.ide.views.objects.ProtocolInNode;
import com.dc.bip.ide.views.objects.ProtocolOutFolder;
import com.dc.bip.ide.views.objects.ProtocolOutNode;
import com.dc.bip.ide.views.objects.TreeNode;

public class TreeViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getChildren(Object parentElement) {

		return ((TreeNode) parentElement).getChildren().toArray();

	}

	@Override
	public Object getParent(Object element) {
		return ((TreeNode) element).getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		if (((TreeNode) element).getChildren().size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public Object[] getElements(Object inputElement) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject projs[] = root.getProjects();
		List<ProjectNode> projects = new ArrayList<ProjectNode>();
		for (int i = 0; i < projs.length; i++) {
			IProject project = projs[i];
			try {
				if (project.isOpen() && project.hasNature("SmartBranchIDE.brNature1")) {
					projects.add(createBipProject(project));
				}
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
		return projects.toArray();
	}

	private ProjectNode createBipProject(IProject project) {
		ProjectNode bp = new ProjectNode(project.getName());
		String folderPath = project.getFolder(BipConstantUtil.BasePath).getLocation().toString();
		String busiPath = project.getFolder(BipConstantUtil.BusiSvcPath).getLocation().toString();
		String flowPath = project.getFolder(BipConstantUtil.FlowPath).getLocation().toString();
		String compPath = project.getFolder(BipConstantUtil.CompositePath).getLocation().toString();
		String bipserverPath = project.getFolder(BipConstantUtil.ServerPath).getLocation().toString();
		String protocolPath = project.getFolder(BipConstantUtil.ProtocolPath).getLocation().toString();
		String expressionPath = project.getFolder(BipConstantUtil.ExpressionPath).getLocation().toString();
		
		File expressionFolder = new File(expressionPath);
		FilenameFilter expressionFileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if (str.equals(BipConstantUtil.ExpressionServiceExtension)) {
						return true;
					}
				}
				return false;
			}
		};
		
		File protclInFolder = new File(protocolPath);
		FilenameFilter protocolInFileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if (str.equals(BipConstantUtil.ProtocolInExtension)) {
						return true;
					}
				}
				return false;
			}
		};
		FilenameFilter protocolOutFileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if (str.equals(BipConstantUtil.ProtocolOutExtension)) {
						return true;
					}
				}
				return false;
			}
		};
		File baseFolder = new File(folderPath);
		FilenameFilter fileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if (str.equals(BipConstantUtil.BaseServiceExtension)) {
						return true;
					}
				}
				return false;
			}
		};

		// 过滤业务服务文件
		File busiFileFolder = new File(busiPath);
		FilenameFilter busiFileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if (str.equals(BipConstantUtil.BusiServiceExtension)) {
						return true;
					}
				}
				return false;
			}
		};
		// 流程管理文件
		File flowFileFolder = new File(flowPath);
		FilenameFilter flowFileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if (str.equals(BipConstantUtil.FlowExtension)) {
						return true;
					}
				}
				return false;
			}
		};
		// 过滤组合服务文件
		File compFolder = new File(compPath);
		FilenameFilter compfileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if (str.equals(BipConstantUtil.CompositeServiceExtension)) {
						return true;
					}
				}
				return false;
			}
		};
		// 过滤bipserver文件
		File bipserverFolder = new File(bipserverPath);
		FilenameFilter bipserverfileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if (str.equals(BipConstantUtil.ServerExtension)) {
						return true;
					}
				}
				return false;
			}
		};
		BaseFolder bsFolder = new BaseFolder("基础服务");
		BusiFolder busiFolder = new BusiFolder("业务服务");
		FlowFolder flowRoot = new FlowFolder("流程管理");
		CompositeFolder compsiteFolder = new CompositeFolder("组合服务");
		BipServerFolder bipServerFolder = new BipServerFolder("服务端");
		ProtocolFolder protocolFolder = new ProtocolFolder("协议管理");
		ProtocolInFolder protocolInF = new ProtocolInFolder("接入协议");
		ProtocolOutFolder protocolOutF= new ProtocolOutFolder("接出协议");
		protocolFolder.addChild(protocolInF);
		protocolFolder.addChild(protocolOutF);
		ExpressionFolder expFolder = new ExpressionFolder("表达式管理");
		bp.addChild(compsiteFolder);
		bp.addChild(bsFolder);
		bp.addChild(busiFolder);
		bp.addChild(flowRoot);
		bp.addChild(bipServerFolder);
		bp.addChild(protocolFolder);
		bp.addChild(expFolder);

		File[] baseFiles = baseFolder.listFiles(fileNameFilter);
		if (baseFiles != null && baseFiles.length > 0) {
			for (int i = 0; i < baseFiles.length; i++) {
				try {
					BaseService bs = HelpUtil.parseBaseSvcXml(new FileInputStream(baseFiles[i]));
					BSNode bsNode = new BSNode(bs.getName());
					// 设置配置文件路径
					StringBuilder sb = new StringBuilder().append("/").append(project.getName())
							.append(BipConstantUtil.BasePath).append(baseFiles[i].getName());
					Path filePath = new Path(sb.toString());
					bs.setBaseFile(ResourcesPlugin.getWorkspace().getRoot().getFile(filePath));
					// 设置代码路径
					String javaPath = (new StringBuilder("/")).append(project.getName()).append("/src/")
							.append(bs.getImpls().replaceAll("\\.", "/")).append(".java").toString();
					bs.setCodeFile(ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(javaPath)));

					bsNode.setBaseService(bs);
					bsFolder.addChild(bsNode);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		// 读取业务服务子节点
		File[] busiFiles = busiFileFolder.listFiles(busiFileNameFilter);
		if (busiFiles != null && busiFiles.length > 0) {
			for (int i = 0; i < busiFiles.length; i++) {
				try {
					StringBuilder sb = new StringBuilder().append("/").append(project.getName())
							.append(BipConstantUtil.BusiSvcPath).append(busiFiles[i].getName());
					Path filePath = new Path(sb.toString());
					IFile resource = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
					File file = resource.getLocation().toFile();
					FileInputStream in = new FileInputStream(file);
					ObjectInputStream objectInputStream = new ObjectInputStream(in);
					BSOperationNode node = (BSOperationNode) objectInputStream.readObject();
//					node.setResource(ResourcesPlugin.getWorkspace().getRoot().getFile(filePath));
					busiFolder.addChild(node);
					in.close();
					objectInputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// 读取流程子节点
		File[] flowFiles = flowFileFolder.listFiles(flowFileNameFilter);
		if (flowFiles != null && flowFiles.length > 0) {
			for (int i = 0; i < flowFiles.length; i++) {
				try {
					StringBuilder sb = new StringBuilder().append("/").append(project.getName())
							.append(BipConstantUtil.FlowPath).append(flowFiles[i].getName());
					Path filePath = new Path(sb.toString());
					IFile resource = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
					FlowNode node = new FlowNode(resource.getName());
					node.setResource(resource);
					flowRoot.addChild(node);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		// 读取表達式
				File[] expressFiles = expressionFolder.listFiles(expressionFileNameFilter);
				if (expressFiles != null && expressFiles.length > 0) {
					for (int i = 0; i < expressFiles.length; i++) {
						try {
							StringBuilder sb = new StringBuilder().append("/").append(project.getName())
									.append(BipConstantUtil.ExpressionPath).append(expressFiles[i].getName());
							Path filePath = new Path(sb.toString());
							IFile resource = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
							ExpressionNode node = new ExpressionNode(resource.getName());
							Params params = new Params(resource.getLocation().toOSString());
							params.unPersist();
							node.setParams(params);
							node.setResource(resource);
							node.setProjectName(project.getName());
							expFolder.addChild(node);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
		
		// 读取输入协议
				File[] inProtocol = protclInFolder.listFiles(protocolInFileNameFilter);
				if (inProtocol != null && inProtocol.length > 0) {
					for (int i = 0; i < inProtocol.length; i++) {
						try {
							StringBuilder sb = new StringBuilder().append("/").append(project.getName())
									.append(BipConstantUtil.ProtocolPath).append(inProtocol[i].getName());
							Path filePath = new Path(sb.toString());
							IFile resource = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
							File file = resource.getLocation().toFile();
							FileInputStream in = new FileInputStream(file);
							ObjectInputStream objectInputStream = new ObjectInputStream(in);
							ProtocolInNode node = (ProtocolInNode) objectInputStream.readObject();
							node.setResource(ResourcesPlugin.getWorkspace().getRoot().getFile(filePath));
							node.setNodeName(node.getProtocolInService().getProtocolName());
							protocolInF.addChild(node);
							in.close();
							objectInputStream.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				// 读取输出协议
				File[] outProtocol = protclInFolder.listFiles(protocolOutFileNameFilter);
				if (outProtocol != null && outProtocol.length > 0) {
					for (int i = 0; i < outProtocol.length; i++) {
						try {
							StringBuilder sb = new StringBuilder().append("/").append(project.getName())
									.append(BipConstantUtil.ProtocolPath).append(outProtocol[i].getName());
							Path filePath = new Path(sb.toString());
							IFile resource = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
							File file = resource.getLocation().toFile();
							FileInputStream in = new FileInputStream(file);
							ObjectInputStream objectInputStream = new ObjectInputStream(in);
							ProtocolOutNode node = (ProtocolOutNode) objectInputStream.readObject();
							node.setResource(ResourcesPlugin.getWorkspace().getRoot().getFile(filePath));
							node.setProjectName(outProtocol[i].getName());
							node.setNodeName(node.getProtocolInService().getProtocolName());
							protocolOutF.addChild(node);
							in.close();
							objectInputStream.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

		// 组合服务管理
		File[] compFiles = compFolder.listFiles(compfileNameFilter);

		if (compFiles != null && compFiles.length > 0) {
			for (int i = 0; i < compFiles.length; i++) {
				try {
					StringBuilder sb = new StringBuilder().append("/").append(project.getName())
							.append(BipConstantUtil.CompositePath).append(compFiles[i].getName());
					Path filePath = new Path(sb.toString());
					IFile resource = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
					File file = resource.getLocation().toFile();
					FileInputStream in = new FileInputStream(file);
					ObjectInputStream objectInputStream = new ObjectInputStream(in);
					CompositeService compSvc = (CompositeService) objectInputStream.readObject();
					String nodeName = compFiles[i].getName().substring(0, compFiles[i].getName().lastIndexOf("."));
					CompositeNode compNode = new CompositeNode(nodeName);
					compNode.setProjectName(project.getName());
					compNode.setCompSvc(compSvc);
					compsiteFolder.addChild(compNode);
					in.close();
					objectInputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
		}

		// bip服务端管理
		File[] bipserverFiles = bipserverFolder.listFiles(bipserverfileNameFilter);
		if (bipserverFiles != null && bipserverFiles.length > 0) {
			for (int j = 0; j < bipserverFiles.length; j++) {
				String nodeName = bipserverFiles[j].getName().substring(0,
						bipserverFiles[j].getName().lastIndexOf("."));
				BipServerNode bipserverNode = new BipServerNode(nodeName);
				bipserverNode.setProjectName(project.getName());
				bipServerFolder.addChild(bipserverNode);
			}
		} else {
		}
		return bp;
	}
}
