package com.dc.bip.ide.gef.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

public class TitleModel  extends AbstractConnectionNode implements Externalizable{
private String name;
private int fontSize =12;
private ParamContainer container;


public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public TitleModel(String name) {
	super();
	this.name = name;
}

public TitleModel() {

}

public TitleModel(String name, int fontSize) {
	super();
	this.name = name;
	this.fontSize = fontSize;
}

public int getFontSize() {
	return fontSize;
}

public void setFontSize(int fontSize) {
	this.fontSize = fontSize;
}

public String toString()
{
	return "";
}

@Override
public void writeExternal(ObjectOutput out) throws IOException {
	out.writeInt(fontSize);
	out.writeObject(name);
	out.writeObject(getSourceConnection());
	out.writeObject(getTargetConnection());
	out.writeObject(container);
}

@Override
public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
	fontSize = in.readInt();
	name = (String)in.readObject();
	setSourceConnection((List)in.readObject());
	setTargetConnection((List)in.readObject());
	container = (ParamContainer)in.readObject();
}

public ParamContainer getContainer() {
	return container;
}

public void setContainer(ParamContainer paramContainer) {
	this.container = paramContainer;
}
}
