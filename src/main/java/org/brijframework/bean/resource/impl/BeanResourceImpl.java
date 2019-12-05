package org.brijframework.bean.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.brijframework.bean.resource.BeanResource;

public class BeanResourceImpl implements BeanResource {

	private String id;

	private String name;

	private String scope;
	
	private String model;
	
	private String type;
	
	private String uniqueKey;
	
	private String factoryClass;
	
	private String factoryMethod;
	
	private Map<String, Object> properties;
	
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	
	public String getUniqueKey() {
		return uniqueKey;
	}
	
	public String getScope() {
		return scope;
	}
	
	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public String getModel() {
		return model;
	}
	
	@Override
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String getFactoryClass() {
		return factoryClass;
	}

	public void setFactoryClass(String factoryClass) {
		this.factoryClass = factoryClass;
	}

	@Override
	public String getFactoryMethod() {
		return factoryMethod;
	}

	public void setFactoryMethod(String factoryMethod) {
		this.factoryMethod = factoryMethod;
	}

	public Map<String, Object> getProperties() {
		if(properties==null) {
			properties=new HashMap<String, Object>();
		}
		return properties;
	}
	
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return super.toString()+"[id=" + id + ", name=" + name + ", scope=" + scope + ", model=" + model + ", type="
				+ type + ", uniqueKey=" + uniqueKey + ", factoryClass=" + factoryClass + ", factoryMethod="
				+ factoryMethod + ", properties=" + properties + "]";
	}
	
	
}
