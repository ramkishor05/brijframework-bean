package org.brijframework.bean.resource.impl;

import java.util.ArrayList;
import java.util.List;

import org.brijframework.bean.resource.BeanResourceConstructor;
import org.brijframework.bean.resource.BeanResourceParam;

public class BeanResourceConstructorImpl implements BeanResourceConstructor {

	private List<BeanResourceParam> params;
	private String model;
	private String id;
	private String name;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getModel() {
		return this.model;
	}

	@Override
	public List<BeanResourceParam> getParameters() {
		if (params == null) {
			params = new ArrayList<>();
		}
		return params;
	}

	public void setParams(List<BeanResourceParam> params) {
		this.params = params;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
