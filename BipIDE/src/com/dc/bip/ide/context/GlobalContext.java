package com.dc.bip.ide.context;

import java.util.List;
import java.util.Map;

/**
 * 全局上下文
 * @author pangzt
 *
 */
public class GlobalContext implements IContext{
 	private List<Map<String,String>> globalContext;
	public List<Map<String,String>> getGlobalContext() {
		return globalContext;
	}
	public void setGlobalContext(List<Map<String,String>> globalContext) {
		this.globalContext = globalContext;
	}
	@Override
	public void setValues(Map<String, Object> paramMap) {
		
	}
	@Override
	public Map<String, Object> getVlaues(String key) {
		return null;
	}
}
