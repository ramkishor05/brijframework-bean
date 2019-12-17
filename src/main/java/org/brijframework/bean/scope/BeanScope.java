package org.brijframework.bean.scope;

import java.util.Map;

import org.brijframework.bean.definition.BeanDefinition;

public class BeanScope {

	private BeanDefinition definition;
	private Object scopeObject;
	private Map<String, BeanScope> properties;
	
	private String id;

	public BeanScope() {
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

	public BeanDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(BeanDefinition definition) {
		this.definition = definition;
	}

	public Map<String, BeanScope> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, BeanScope> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "BeanScope [definition=" + definition + ", scopeObject=" + scopeObject + ", properties=" + properties
				+ ", id=" + id + "]";
	}
	
	
}
