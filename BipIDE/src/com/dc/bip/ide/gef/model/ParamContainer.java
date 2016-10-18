package com.dc.bip.ide.gef.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

import com.dc.bip.ide.objects.param.IParam;

public class ParamContainer extends AbstractModel implements IParam,Externalizable{
/**
	 * 
	 */
private static final long serialVersionUID = 1L;
private String name;
private Rectangle rectangle;
public static  final String p_constraint = "_constraint";
private List children = new ArrayList();
private ParamContainer container;
private String path;
private String serviceId;
private ParamScope scope;
private String operationId;


public ParamContainer()
{
	
}

public ParamContainer(String name)
{
	this.name = name;
	setContainer(null); 
}

public ParamContainer getContainer() {
	return container;
}

public void setContainer(ParamContainer container) {
	this.container = container;
}

public List getChildren() {
	return children;
}

/*public void setChildren(List children) {
	this.children = children;
}*/

public void  addChild(Object child)
{
	children.add(child);
	
	if(child instanceof Param || child instanceof ParamContainer)
	{
		((IParam)child).setContainer(this);
	}else if(child instanceof TitleModel)
	{
		((TitleModel)child).setContainer(this);
	}
}

public Rectangle getRectangle() {
	return rectangle;
}

public void setRectangle(Rectangle rectangle) {
	this.rectangle = rectangle;
	this.firePropertyChange(p_constraint, null, rectangle);
}

@Override
public String getName() {
	return name;
}

public String toString()
{
	StringBuilder sb = new StringBuilder();
	sb.append(name).append("包含如下参数:");
	for(Object tmpParam : children)
	{
		sb.append(tmpParam.toString());
		sb.append("\n");
	}
	return sb.toString();
}

@Override
public void writeExternal(ObjectOutput out) throws IOException {
	out.writeObject(name);
	out.writeObject(children);
	out.writeObject(container);
	out.writeObject(rectangle);
	out.writeObject(path);
	out.writeObject(serviceId);
	out.writeObject(scope);
	out.writeObject(operationId);
}


@Override
public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
	name = (String)in.readObject();
	children = (List)in.readObject();
	container = (ParamContainer)in.readObject();
	rectangle = (Rectangle)in.readObject();
	path = (String)in.readObject();
	serviceId =(String)in.readObject();
	scope = (ParamScope)in.readObject();
	operationId = (String)in.readObject();
}

public String getPath() {
	return path;
}

public void setPath(String path) {
	this.path = path;
}

public String getServiceId() {
	return serviceId;
}

public void setServiceId(String serviceId) {
	this.serviceId = serviceId;
}

public ParamScope getScope() {
	return scope;
}

public void setScope(ParamScope scope) {
	this.scope = scope;
}

public String getOperationId() {
	return operationId;
}

public void setOperationId(String operationId) {
	this.operationId = operationId;
}

/**
 * 在画图编辑器中通过children中的最外层的titleModel来表示参数容器，如果该TitleModel有连接，
 * 则表示该容器有连接，需要在映射文件中反应该连接
 * @return
 */
public List getConnectionSource()
{
	List sourceConnection = new ArrayList();
	for(Object tmpObject : children)
	{
		if(tmpObject instanceof TitleModel)
		{
			TitleModel model = (TitleModel) tmpObject;
			for(Object tmpModel : model.getTargetConnection())
			{
				LineConnectionModel connectionModel = (LineConnectionModel)tmpModel;
				sourceConnection.add(((TitleModel)connectionModel.getSource()).getContainer());
			}
			break;
		}
	}
	return sourceConnection;
}

public List getConnectionTarget()
{
	List targetConnection = new ArrayList();
	for(Object tmpObject : children)
	{
		if(tmpObject instanceof TitleModel)
		{
			TitleModel model = (TitleModel) tmpObject;
			for(Object tmpModel : model.getSourceConnection())
			{
				LineConnectionModel connectionModel = (LineConnectionModel)tmpModel;
				targetConnection.add(((TitleModel)connectionModel.getTarget()).getContainer());				
			}
			break;
		}
	}
	return targetConnection;
}

}
