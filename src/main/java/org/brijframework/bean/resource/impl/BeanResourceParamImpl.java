package org.brijframework.bean.resource.impl;

import org.brijframework.bean.resource.BeanResourceParam;

public class BeanResourceParamImpl implements BeanResourceParam {
	
	private int index;
	private String name;
	private String value;
	
	public BeanResourceParamImpl(int index, String name, String value) {
		super();
		this.index = index;
		this.name = name;
		this.value = value;
	}

	@Override
	public int getIndex() {
		return index;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
}
