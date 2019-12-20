package org.brijframework.bean.definition.impl;

import org.brijframework.bean.definition.BeanDefinationConstructor;

public class BeanDefinationConstructorImpl implements BeanDefinationConstructor {

	private String id;
	private String name;
	private String model;
	private Object[] values;

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
		return model;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public Object[] getValues() {
		return values;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}

}
