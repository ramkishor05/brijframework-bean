package org.brijframework.bean.scope;

import org.brijframework.bean.meta.BeanMetaData;

public class BeanScope {

	private BeanMetaData datainfo;
	private Object scopeObject;
	private String id;

	public BeanScope(BeanMetaData datainfo) {
		this.setDatainfo(datainfo);
	}

	public BeanMetaData getDatainfo() {
		return datainfo;
	}

	public void setDatainfo(BeanMetaData datainfo) {
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
