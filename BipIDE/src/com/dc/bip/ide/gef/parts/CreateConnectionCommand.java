package com.dc.bip.ide.gef.parts;

import org.eclipse.gef.commands.Command;

import com.dc.bip.ide.gef.model.AbstractConnectionModel;
import com.dc.bip.ide.gef.model.IConnectionNode;

public class CreateConnectionCommand extends Command {
  private IConnectionNode source;
  private IConnectionNode target;
  private AbstractConnectionModel connection;
  
	public boolean canExecute() {
		if(null != source  && null != target)
		{			
			if(!source.getClass().getSimpleName().equalsIgnoreCase(target.getClass().getSimpleName()))
			{
				return false;
			}
		}
		return true;
	}
	
	public IConnectionNode getSource() {
		return source;
	}
	
	public IConnectionNode getTarget() {
		return target;
	}
	
	public AbstractConnectionModel getConnection() {
		return connection;
	}
	
	public void setSource(Object source) {
		this.source = (IConnectionNode)source;
		this.connection.setSource((IConnectionNode)source);
	}
	
	public void setTarget(Object target) {
		this.target = (IConnectionNode)target;
		this.connection.setTarget( (IConnectionNode)target);
	}
	
	public void setConnection(Object connection) {
		this.connection = (AbstractConnectionModel)connection;
	}
	
	public void execute() {
		connection.attachSource();
		connection.attachTarget();
	}

	public void undo() {
		connection.detachSource();
		connection.detachTarget();
	}  
}
