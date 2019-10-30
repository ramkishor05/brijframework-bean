package org.brijframework.bean.group;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.bean.setup.BeanSetup;
import org.brijframework.group.impl.DefaultGroup;

public class BeanSetupGroup implements DefaultGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object groupKey;
	private ConcurrentHashMap<String,BeanSetup> cache=new ConcurrentHashMap<>();
	
	public BeanSetupGroup(Object groupKey) {
		this.groupKey=groupKey;
	}

	@Override
	public Object getGroupKey() {
		return groupKey;
	}

	@Override
	public ConcurrentHashMap<String,BeanSetup> getCache() {
		return cache;
	}

	@Override
	public <T> T find(String parentID, Class<?> type) {
		return null;
	}
}
