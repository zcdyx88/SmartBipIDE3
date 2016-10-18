package com.dc.bip.ide.gef.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

import com.dc.bip.ide.objects.param.IParam;

public class Param extends AbstractConnectionNode implements IParam,Externalizable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name="";
	private ParamContainer container;  
    private Rectangle rectangle;
    private String path;
    /**
     * 所属的服务ID,可以是组合服务也可以是业务服务
     */
    private String serviceId;
    private ParamScope scope;
    private String operationId;
    private String type;
	private String value;
    private String desc;
	
	
    public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
   public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Param(String name) {
		super();
		this.name = name;
		setContainer(null);
	}
    
    public Param() {
    	
	}
    
	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	
	
	@Override
	public String getName() {
		return name;
	}
	
/*	@Override
	public void setName(String name) {
		this.name = name;
	}*/

	@Override
	public void setContainer(ParamContainer container) {
		this.container = container;
	}

	@Override
	public ParamContainer getContainer() {
		return container;
	}
	
	public String toString()
	{
		return getName();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(container);
		out.writeObject(name);
		out.writeObject(getSourceConnection());
		out.writeObject(getTargetConnection());
		out.writeObject(serviceId);
		out.writeObject(path);
		out.writeObject(scope);
		out.writeObject(operationId);
		out.writeObject(type);
		out.writeObject(value);
		out.writeObject(desc);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		container = (ParamContainer)in.readObject();
		name = (String)in.readObject();
		setSourceConnection((List)in.readObject());
		setTargetConnection((List)in.readObject());
		serviceId = (String)in.readObject();
		path = (String)in.readObject();
		scope =(ParamScope)in.readObject();
		operationId = (String)in.readObject();
		type = (String)in.readObject();
		value = (String)in.readObject();
		desc = (String)in.readObject();		
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
	
	public String  getFullPath()
	{
		StringBuilder fullPath=new StringBuilder();
		if(null ==getPath() || getPath().length() ==0)
		{
			fullPath.append(getOperationId()).append(".").append(getName());
		}
		else
		{
			fullPath.append(getOperationId()).append(".").append(getPath()).append("/").append(getName());
		}
		return fullPath.toString();
	}
}
