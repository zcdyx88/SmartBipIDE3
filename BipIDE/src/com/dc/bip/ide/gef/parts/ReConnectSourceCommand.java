package com.dc.bip.ide.gef.parts;

import org.eclipse.gef.commands.Command;

import com.dc.bip.ide.gef.model.AbstractConnectionModel;
import com.dc.bip.ide.gef.model.IConnectionNode;
import com.dc.bip.ide.gef.model.Param;

public class ReConnectSourceCommand extends Command {
    private AbstractConnectionModel connection;
    private  IConnectionNode source;
    private  IConnectionNode oldSource;
    
    public void execute()
    {    	
    	connection.detachSource();
    	connection.setSource(source);
    	connection.attachSource();
    }    
    public void undo()
    { 
    	connection.detachSource();
    	connection.setSource(oldSource);
    	connection.attachSource();
    }    
	public AbstractConnectionModel getConnection() {
		return connection;
	}
	public IConnectionNode getSource() {
		return source;
	}
	public void setConnection(Object connection) {
		this.connection = (AbstractConnectionModel)connection;
		oldSource = this.connection.getSource();
	}
	public void setSource(Object source) {
		this.source = (IConnectionNode)source;
	}    
}
