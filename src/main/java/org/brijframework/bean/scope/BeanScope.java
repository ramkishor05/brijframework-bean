package org.brijframework.bean.scope;

import org.brijframework.bean.definition.BeanDefinition;

public class BeanScope {

	private BeanDefinition definition;
	private Object scopeObject;
	private String id;

	public BeanScope(BeanDefinition definition) {
		this.setBeanDefinition(definition);
	}

	public BeanDefinition getBeanDefinition() {
		return definition;
	}

	public void setBeanDefinition(BeanDefinition definition) {
		this.definition = definition;
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
