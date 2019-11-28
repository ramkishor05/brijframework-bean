package org.brijframework.bean.meta.impl;

import java.util.HashMap;
import java.util.Map;

import org.brijframework.bean.meta.BeanMetaData;
import org.brijframework.model.info.ClassModelMetaData;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.asserts.Assertion;

public class BeanMetaDataImpl implements BeanMetaData {
	
	private ClassModelMetaData owner;
	
	private String id;
	
	private String name;
	
	private Scope scope;
	
	private String factoryClass;
	
	private String factoryMethod;

	private Map<String, Object> properties;
	
	public BeanMetaDataImpl(ClassModelMetaData owner) {
		this.owner=owner;
	}
	
	public ClassModelMetaData getOwner() {
		return owner;
	}
	
	@Override
	public void init() {
		Assertion.notNull(getOwner(), "Model info should not be null.");
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
	
	public void setOwner(ClassModelMetaData owner) {
		this.owner = owner;
	}
	
	public void setScope(Scope scope) {
		this.scope = scope;
	}
	
	public Scope getScope() {
		return scope;
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
			properties=new HashMap<>();
		}
		return properties;
	}
	
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
