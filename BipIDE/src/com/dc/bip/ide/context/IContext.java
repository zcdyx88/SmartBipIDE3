package com.dc.bip.ide.context;

import java.util.Map;

public interface IContext {	
	public void setValues(Map<String, Object> paramMap);
	public Map<String, Object> getVlaues(String key);
}
