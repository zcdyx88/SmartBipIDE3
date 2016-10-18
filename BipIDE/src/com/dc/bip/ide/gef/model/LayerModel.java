package com.dc.bip.ide.gef.model;

import java.beans.PropertyChangeListener;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

public class LayerModel extends AbstractModel implements Externalizable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Object> children = new ArrayList<Object>();
	private Rectangle rectangle;
	private PropertyChangeListener editListener;
	
	public final static String P_Delete_Child = "_delete_child";
	public final static String P_Add_Child = "_add_child";

	public LayerModel()
	{
		
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public List<Object> getChildren() {
		return children;
	}

/*	public void setChildren(List<Object> children) {
		this.children = children;
	}*/
	
	public void addChild(Object child)
	{
		children.add(child);
		this.firePropertyChange(P_Add_Child, null, null);
		
		if(child instanceof ParamContainer)
		 {
			addEditListener((ParamContainer)child);
		 }
	}
	
	public void removeChild(Object child)
	{
		children.remove(child);
		this.firePropertyChange(P_Delete_Child, null, null);
	}

	public void setEditListener(PropertyChangeListener editListener) {
		this.editListener = editListener;
		addPropertyChangeListener(editListener);
		for(Object tmpObject : children)
		{
			if(tmpObject instanceof ParamContainer)
			 {
				addEditListener((ParamContainer)tmpObject);
			 }
		}
	}

	public PropertyChangeListener getEditListener() {
		return editListener;
	}

	private void addEditListener(ParamContainer model)
	{
		model.addPropertyChangeListener(editListener);
		for( Object tmpModel  :  model.getChildren())
		{
			if( tmpModel instanceof Param )
			{				
				((Param)tmpModel).addPropertyChangeListener(editListener);
			}
			else if (tmpModel instanceof TitleModel)
			{
				((TitleModel)tmpModel).addPropertyChangeListener(editListener);
			}
			else if( tmpModel instanceof ParamContainer )
			{
				addEditListener((ParamContainer)tmpModel);				
			}
		}
	}
	
	

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(children);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		children = (List)in.readObject();
	}
}
