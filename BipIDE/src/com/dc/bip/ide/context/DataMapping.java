package com.dc.bip.ide.context;
/**
 * 数据映射
 * @author pangzt
 *
 */
public class DataMapping {
	/*字段*/
	private String target;
	/*来源*/
	private String source;
	/*上下文key*/
	private String key;
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
