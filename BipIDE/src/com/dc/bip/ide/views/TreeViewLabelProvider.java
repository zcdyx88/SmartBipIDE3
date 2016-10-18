package com.dc.bip.ide.views;

import java.io.IOException;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

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
import com.dc.bip.ide.views.objects.ProtocolNode;
import com.dc.bip.ide.views.objects.ProtocolOutFolder;
import com.dc.bip.ide.views.objects.ProtocolOutNode;
import com.dc.bip.ide.views.objects.TreeNode;

public class TreeViewLabelProvider extends LabelProvider {
	
	@Override
	public Image getImage(Object element) {
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(); ;  
		
		String imageKey=ISharedImages.IMG_OBJ_ELEMENT;
		
		if (element instanceof BaseFolder || element instanceof  BusiFolder || element instanceof  CompositeFolder
				|| element instanceof BipServerFolder|| element instanceof FlowFolder ||element instanceof ProtocolInFolder
				|| element instanceof ProtocolOutFolder || element instanceof ProtocolFolder
				|| element instanceof ExpressionFolder)
		{
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
		else if(element instanceof BSNode){
			Image image = null;
			try {
				image = new Image(Display.getDefault(),this.getClass().getResource("/icons/2.png").openStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return image;
		}
		else if(element instanceof BSOperationNode){
			Image image=null;
			try {
				image = new Image(Display.getDefault(), this.getClass().getResource("/icons/3.png").openStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return image;
		}
		else if(element instanceof FlowNode){
			Image image=null;
			try {
				image = new Image(Display.getDefault(), this.getClass().getResource("/icons/branch.gif").openStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return image;
		}
		else if(element instanceof CompositeNode){
			Image image=null;
			try {
				image = new Image(Display.getDefault(), this.getClass().getResource("/icons/0.png").openStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return image;		
		}
		else if(element instanceof BipServerNode){
			Image image=null;
			try {
				image = new Image(Display.getDefault(), this.getClass().getResource("/icons/serviceSystem.gif").openStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return image;
		}
		else if (element instanceof ProjectNode)
		{
			imageKey = ISharedImages.IMG_OBJ_PROJECT;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
		else if(element instanceof ProtocolInNode){
			Image image=null;
			try {
				image = new Image(Display.getDefault(), this.getClass().getResource("/icons/serviceSystem.gif").openStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return image;
		}else if(element instanceof ProtocolOutNode){
			Image image=null;
			try {
				image = new Image(Display.getDefault(), this.getClass().getResource("/icons/serviceSystem.gif").openStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return image;
		}else if(element instanceof ProtocolNode){
			Image image=null;
			try {
				image = new Image(Display.getDefault(), this.getClass().getResource("/icons/serviceSystem.gif").openStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return image;
		}else if(element instanceof ExpressionNode){
			Image image=null;
			try {
				image = new Image(Display.getDefault(), this.getClass().getResource("/icons/serviceSystem.gif").openStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return image;
		}
		
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}


	@Override
	public String getText(Object element) {	
		return ((TreeNode)element).getNodeName();
	}
}
