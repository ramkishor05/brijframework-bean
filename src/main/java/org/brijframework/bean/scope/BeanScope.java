package org.brijframework.bean.scope;

import org.brijframework.bean.info.BeanInfo;

public class BeanScope {

	private BeanInfo datainfo;
	private Object scopeObject;
	private String id;

	public BeanScope(BeanInfo datainfo) {
		this.setDatainfo(datainfo);
	}

	public BeanInfo getDatainfo() {
		return datainfo;
	}

	public void setDatainfo(BeanInfo datainfo) {
		this.datainfo = datainfo;
	}

	public void setScopeObject(Object scopeObject) {
		this.scopeObject=scopeObject;
	}

	public Object getScopeObject() {
		return scopeObject;
	}

	public void setId(String id) {
		this.id=id;
	}
	
	public String getId() {
		return id;
	}
}
