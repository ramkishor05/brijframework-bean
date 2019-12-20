package org.brijframework.bean.resource.impl;

import org.brijframework.bean.resource.BeanResourceParam;

public class BeanResourceParamImpl implements BeanResourceParam {
	
	private int index;
	private Class<?> type;
	private String name;
	private String value;
	
	public BeanResourceParamImpl(int index, Class<?> type, String name, String value) {
		super();
		this.index = index;
		this.type = type;
		this.name = name;
		this.value = value;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
}
