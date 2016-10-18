package com.dc.bip.ide.gef.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import com.dc.bip.ide.gef.model.AbstractConnectionModel;
import com.dc.bip.ide.gef.model.LayerModel;
import com.dc.bip.ide.gef.model.Param;
import com.dc.bip.ide.gef.model.ParamContainer;
import com.dc.bip.ide.gef.model.TitleModel;

public class DeleteServiceCommand extends Command {
	private LayerModel layer;
	private ParamContainer service;

/*	private List targetConnections = new ArrayList();
	private List sourceConnections = new ArrayList();*/
	private List connections = new ArrayList();
	private Map<Param,List> connectionMap = new HashMap<Param,List>();
	
	public boolean canExecute() {
		// TODO  被映射的不能删除
		return true;
	}
	
	public void setLayerModel(Object layer){
		this.layer = (LayerModel)layer;
	}
	
	public void setServiceModel(Object service)
	{
		this.service = (ParamContainer)service;
	}

	public void execute() {		
		List tmpConnections;
		for(Object  tmpParam : service.getChildren())
		{
			if (!(tmpParam instanceof Param))
			{
				continue;
			}
			Param paramModel = (Param)tmpParam;
			connections.addAll(paramModel.getTargetConnection());
			connections.addAll(paramModel.getSourceConnection());
			for(Object tmpConnection : connections)
			{
				AbstractConnectionModel connectionModel = (AbstractConnectionModel)tmpConnection;
				connectionModel.detachSource();
				connectionModel.detachTarget();
			}
			connectionMap.put(paramModel, connections);
/*			sourceConnections.addAll(paramModel.getTargetConnection());
			for(Object tmpConnection : sourceConnections)
			{
				AbstractConnectionModel connectionModel = (AbstractConnectionModel)tmpConnection;
				connectionModel.detachSource();
				connectionModel.detachTarget();
			}
			
			targetConnections.addAll(paramModel.getTargetConnection());
			for(Object tmpConnection : targetConnections)
			{
				AbstractConnectionModel connectionModel = (AbstractConnectionModel)tmpConnection;
				connectionModel.detachSource();
				connectionModel.detachTarget();
			}*/
		}
		layer.removeChild(service);
	}

	public void undo() {
		layer.addChild(service);
		List tmpConnections;
		List tmpParams = service.getChildren();
		for(Object  tmpParam : tmpParams)
		{
			if (!(tmpParam instanceof Param))
			{
				continue;
			}			
			Param paramModel = (Param)tmpParam;
			for(Object tmpConnection : connectionMap.get(paramModel))
			{
				AbstractConnectionModel connectionModel = (AbstractConnectionModel)tmpConnection;
				connectionModel.attachSource();
				connectionModel.attachTarget();
			}			
		}
		
	}

}
