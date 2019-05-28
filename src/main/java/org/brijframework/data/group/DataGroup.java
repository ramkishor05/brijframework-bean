package org.brijframework.data.group;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.asm.group.DefaultGroup;
import org.brijframework.data.DataInfo;

public class DataGroup implements DefaultGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object groupKey;
	private ConcurrentHashMap<String,DataInfo> cache=new ConcurrentHashMap<>();
	
	public DataGroup(Object groupKey) {
		this.groupKey=groupKey;
	}

	@Override
	public Object getGroupKey() {
		return groupKey;
	}

	@Override
	public ConcurrentHashMap<String,DataInfo> getCache() {
		return cache;
	}

	@Override
	public <T> T find(String parentID, Class<?> type) {
		return null;
	}
}
