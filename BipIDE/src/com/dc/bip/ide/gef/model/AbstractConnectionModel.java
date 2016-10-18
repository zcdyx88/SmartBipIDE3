package com.dc.bip.ide.gef.model;

public class AbstractConnectionModel {

	private IConnectionNode source;
	private IConnectionNode target;
	
	public void  attachSource()
	{
		if(!source.getSourceConnection().contains(this))
		{
			source.addSourceConnection(this);
		}
	} 
	
	public void detachSource()
	{
		if(source.getSourceConnection().contains(this))
		{
			source.removeSourceConnection(this);
		}
	}
	
	public void  attachTarget()
	{
		if(!target.getTargetConnection().contains(this))
		{
			target.addTargetConnection(this);
		}
	} 
	
	public IConnectionNode getSource() {
		return source;
	}

	public IConnectionNode getTarget() {
		return target;
	}

	public void setSource(IConnectionNode source) {
		this.source = source;
	}

	public void setTarget(IConnectionNode target) {
		this.target = target;
	}

	public void detachTarget()
	{
		if(target.getTargetConnection().contains(this))
		{
			target.removeTargetConnection(this);
		}
	}
}
