package com.dc.bip.ide.gef.model;

import java.util.ArrayList;
import java.util.List;

public class AbstractConnectionNode extends AbstractModel  implements IConnectionNode {

    public static final String P_SOURCE_CONNECTION="_source_connection";
    public static final String P_TARGET_CONNECTION="_target_connection";
    private List sourceConnection = new ArrayList();
    private List targetConnection = new ArrayList();
    
    public void addSourceConnection(Object connection)
    {
    	sourceConnection.add(connection);
    	this.firePropertyChange(P_SOURCE_CONNECTION, null, null);
    }
    
    public void removeSourceConnection(Object connection)
    {
    	sourceConnection.remove(connection);
    	this.firePropertyChange(P_SOURCE_CONNECTION, null, null);
    }
    
    public void addTargetConnection(Object connection)
    {
    	targetConnection.add(connection);
    	this.firePropertyChange(P_TARGET_CONNECTION, null, null);
    }
    
    public void removeTargetConnection(Object connection)
    {
    	targetConnection.remove(connection);
    	this.firePropertyChange(P_TARGET_CONNECTION, null, null);
    }   
    
	public List getSourceConnection() {
		return sourceConnection;
	}
	
	public void setSourceConnection(List sourceConnection) {
		this.sourceConnection = sourceConnection;
	}

	public List getTargetConnection() {
		return targetConnection;
	}
	
	public void setTargetConnection(List targetConnection) {
		this.targetConnection = targetConnection;
	}
}
