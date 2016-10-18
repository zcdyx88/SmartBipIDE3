package com.dc.bip.ide.gef.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class LineConnectionModel extends AbstractConnectionModel implements Externalizable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LineConnectionModel()
	{
		
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(getSource());
		out.writeObject(getTarget());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		setSource((IConnectionNode)in.readObject());
		setTarget((IConnectionNode)in.readObject());
	}
}
