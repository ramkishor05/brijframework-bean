package org.brijframework.bean.group.beanmeta;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.group.impl.DefaultGroup;

public class BeanMetaDataGroup implements DefaultGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object groupKey;
	private ConcurrentHashMap<String,BeanDefinition> cache=new ConcurrentHashMap<>();
	
	public BeanMetaDataGroup(Object groupKey) {
		this.groupKey=groupKey;
	}

	@Override
	public Object getGroupKey() {
		return groupKey;
	}

	@Override
	public ConcurrentHashMap<String,BeanDefinition> getCache() {
		return cache;
	}

	@Override
	public <T> T find(String parentID, Class<?> type) {
		return null;
	}
}
