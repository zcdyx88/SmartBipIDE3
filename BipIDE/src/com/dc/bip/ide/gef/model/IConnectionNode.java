package com.dc.bip.ide.gef.model;

import java.util.List;

public interface IConnectionNode {
	
    public void addSourceConnection(Object connection);
    
    public void removeSourceConnection(Object connection);
    
    public void addTargetConnection(Object connection);
    
    public void removeTargetConnection(Object connection);
    
	public List getSourceConnection();
	
	public void setSourceConnection(List sourceConnection) ;
	
	public List getTargetConnection() ;
	
	public void setTargetConnection(List targetConnection) ;
}
