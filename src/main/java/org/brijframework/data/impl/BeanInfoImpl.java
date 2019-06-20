package org.brijframework.data.impl;

import java.util.HashMap;
import java.util.Map;

import org.brijframework.bean.BeanInfo;
import org.brijframework.meta.info.ClassMetaInfo;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.asserts.Assertion;

public class BeanInfoImpl implements BeanInfo {
	
	private ClassMetaInfo owner;
	
	private String id;
	
	private String name;
	
	private Scope scope;

	private Map<String, Object> properties;
	
	public BeanInfoImpl(ClassMetaInfo owner) {
		this.owner=owner;
	}
	
	public ClassMetaInfo getOwner() {
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
	
	public void setOwner(ClassMetaInfo owner) {
		this.owner = owner;
	}
	
	public void setScope(Scope scope) {
		this.scope = scope;
	}
	
	public Scope getScope() {
		return scope;
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
