package com.dc.bip.ide.objects;

import java.util.List;

import com.dc.bip.ide.gef.model.ParamContainer;
import com.dc.bip.ide.objects.param.IParam;

public interface IService {
	
	public ParamContainer getInParams();
	
	public ParamContainer getOutParams();
}
