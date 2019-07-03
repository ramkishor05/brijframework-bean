package org.brijframework.bean.info.impl;

import java.util.HashMap;
import java.util.Map;

import org.brijframework.bean.info.BeanInfo;
import org.brijframework.model.info.OwnerModelInfo;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.asserts.Assertion;

public class BeanInfoImpl implements BeanInfo {
	
	private OwnerModelInfo owner;
	
	private String id;
	
	private String name;
	
	private Scope scope;

	private Map<String, Object> properties;
	
	public BeanInfoImpl(OwnerModelInfo owner) {
		this.owner=owner;
	}
	
	public OwnerModelInfo getOwner() {
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
	
	public void setOwner(OwnerModelInfo owner) {
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
