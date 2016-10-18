package com.dc.bip.ide.objects.param;

import com.dc.bip.ide.gef.model.ParamContainer;
import com.dc.bip.ide.gef.model.ParamScope;

public interface IParam {
	public String getName();
	public void setContainer(ParamContainer container);
	public ParamContainer getContainer( );
	public Object getOperationId();
	public String getPath();
	public String getServiceId();
	public ParamScope getScope();
	
}
