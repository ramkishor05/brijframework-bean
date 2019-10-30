package org.brijframework.bean.setup.impl;

import java.util.HashMap;
import java.util.Map;

import org.brijframework.bean.setup.BeanSetup;

public class BeanSetupImpl implements BeanSetup {

	private String id;

	private String name;

	private String scope;
	
	private String model;
	
	private String type;
	
	private String uniqueKey;
	
	private Map<String, Object> properties;
	
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	
	public String getUniqueKey() {
		return uniqueKey;
	}
	
	public String getType() {
		return type;
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
	public String getTarget() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
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
