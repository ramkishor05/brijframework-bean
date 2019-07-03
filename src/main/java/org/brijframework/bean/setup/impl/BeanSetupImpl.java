package org.brijframework.bean.setup.impl;

import java.util.HashMap;
import java.util.Map;

import org.brijframework.bean.setup.BeanSetup;

public class BeanSetupImpl implements BeanSetup {

	private String id;

	private String name;

	private String scope;
	
	private String model;
	
	private Map<String, Object> properties;

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

	public Map<String, Object> getProperties() {
		if(properties==null) {
			properties=new HashMap<String, Object>();
		}
		return properties;
	}
	
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}
