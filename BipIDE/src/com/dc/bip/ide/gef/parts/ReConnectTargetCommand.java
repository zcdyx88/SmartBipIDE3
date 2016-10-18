package com.dc.bip.ide.gef.parts;

import org.eclipse.gef.commands.Command;

import com.dc.bip.ide.gef.model.AbstractConnectionModel;
import com.dc.bip.ide.gef.model.IConnectionNode;
import com.dc.bip.ide.gef.model.Param;

public class ReConnectTargetCommand extends Command {
    private AbstractConnectionModel connection;
    private  IConnectionNode target;
    private  IConnectionNode oldTarget;
    
    public void execute()
    {    	
    	connection.detachTarget();    	
    	connection.setTarget(target);
    	connection.attachTarget();
    }
    
    public void undo()
    { 
    	connection.detachTarget();    	
    	connection.setTarget(oldTarget);
    	connection.attachTarget();
    }
    
	public AbstractConnectionModel getConnection() {
		return connection;
	}
	public IConnectionNode getTarget() {
		return target;
	}
	public void setConnection(Object connection) {
		this.connection = (AbstractConnectionModel)connection;
		oldTarget = this.connection.getTarget();
	}
	public void setTarget(Object target) {
		this.target = (IConnectionNode)target;
	}    
}
