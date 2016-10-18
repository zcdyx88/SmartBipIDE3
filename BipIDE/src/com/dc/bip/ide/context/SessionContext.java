package com.dc.bip.ide.context;

import java.util.List;
import java.util.Map;
/**
 * 会话上下文
 * @author pangzt
 *
 */
public class SessionContext implements IContext{
	/*流程输入*/
	private List<Map<String,String>> flowin;
	/*流程输出*/
 	private List<Map<String,String>> flowout;
 	/*业务输入*/
 	private List<Map<String,String>> businessin;
 	/*业务输出*/
 	private List<Map<String,String>> businessout;
 	/*临时变量*/
 	private List<Map<String,String>> vars;
	public List<Map<String,String>> getFlowin() {
		return flowin;
	}
	public void setFlowin(List<Map<String,String>> flowin) {
		this.flowin = flowin;
	}
	public List<Map<String,String>> getFlowout() {
		return flowout;
	}
	public void setFlowout(List<Map<String,String>> flowout) {
		this.flowout = flowout;
	}
	public List<Map<String,String>> getBusinessin() {
		return businessin;
	}
	public void setBusinessin(List<Map<String,String>> businessin) {
		this.businessin = businessin;
	}
	public List<Map<String,String>> getBusinessout() {
		return businessout;
	}
	public void setBusinessout(List<Map<String,String>> businessout) {
		this.businessout = businessout;
	}
	public List<Map<String, String>> getVars() {
		return vars;
	}
	public void setVars(List<Map<String, String>> vars) {
		this.vars = vars;
	}
	@Override
	public void setValues(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Map<String, Object> getVlaues(String key) {
		// TODO Auto-generated method stub
		return null;
	}
}
