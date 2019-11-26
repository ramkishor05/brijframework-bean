package org.brijframework.bean.group.scope;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.bean.scope.BeanScope;
import org.brijframework.group.impl.DefaultGroup;

public class BeanScopeGroup implements DefaultGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object groupKey;
	private ConcurrentHashMap<String,BeanScope> cache=new ConcurrentHashMap<>();
	
	public BeanScopeGroup(Object groupKey) {
		this.groupKey=groupKey;
	}

	@Override
	public Object getGroupKey() {
		return groupKey;
	}

	@Override
	public ConcurrentHashMap<String,BeanScope> getCache() {
		return cache;
	}

	@Override
	public <T> T find(String parentID, Class<?> type) {
		return null;
	}
}