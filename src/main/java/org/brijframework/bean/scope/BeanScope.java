package org.brijframework.bean.scope;

import org.brijframework.bean.definition.BeanDefinition;

public class BeanScope {

	private BeanDefinition datainfo;
	private Object scopeObject;
	private String id;

	public BeanScope(BeanDefinition datainfo) {
		this.setDatainfo(datainfo);
	}

	public BeanDefinition getDatainfo() {
		return datainfo;
	}

	public void setDatainfo(BeanDefinition datainfo) {
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
