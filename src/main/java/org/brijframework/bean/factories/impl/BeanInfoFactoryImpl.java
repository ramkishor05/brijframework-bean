package org.brijframework.bean.factories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.bean.factories.BeanInfoGroupFactory;
import org.brijframework.bean.info.BeanInfo;
import org.brijframework.container.Container;
import org.brijframework.group.Group;
import org.brijframework.support.config.Assignable;

public class BeanInfoFactoryImpl implements BeanInfoGroupFactory<BeanInfo>{
	
	private static BeanInfoFactoryImpl factory;
	
	private Container container;
	
	private ConcurrentHashMap<Object, BeanInfo> cache=new ConcurrentHashMap<>();

	@Assignable
	public static BeanInfoFactoryImpl getFactory() {
		if(factory==null) {
			factory=new BeanInfoFactoryImpl();
		}
		return factory;
	}

	@Override
	public BeanInfo getData(String modelKey) {
		if(getCache().containsKey(modelKey)) {
			return getCache().get(modelKey);
		}
		return getContainer(modelKey);
	}
	
	@Override
	public List<BeanInfo> getBeanInfoList(String model) {
		List<BeanInfo> list=new ArrayList<>();
		for(BeanInfo setup:getCache().values()) {
			if(setup.getOwner().getId().equals(model)) {
				list.add(setup);
			}
		}
		return list;
	}
	
	@Override
	public BeanInfo register(BeanInfo dataSetup) {
		loadContainer(dataSetup);
		getCache().put(dataSetup.getId(), dataSetup);
		System.err.println("Bean Info    : "+dataSetup.getId());
		return dataSetup;
	}
	
	@Override
	public BeanInfoFactoryImpl loadFactory() {
		return this;
	}

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public void setContainer(Container container) {
		this.container=container;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConcurrentHashMap<Object, BeanInfo> getCache() {
		if(getContainer()!=null) {
			for(Group  group:getContainer().getCache().values()) {
				group.getCache().forEach((key,value)->{
					cache.put(key, (BeanInfo)value);
				});
			}
		}
		return cache;
	}

	@Override
	public BeanInfoFactoryImpl clear() {
		getCache().clear();
		return this;
	}
	

	public void loadContainer(BeanInfo metaInfo) {
		if (getContainer() == null) {
			return;
		}
		Group group = getContainer().load(metaInfo.getName());
		if(!group.containsKey(metaInfo.getId())) {
			group.add(metaInfo.getId(), metaInfo);
		}else {
			group.update(metaInfo.getId(), metaInfo);
		}
	}

	public BeanInfo getContainer(String modelKey) {
		if (getContainer() == null) {
			return null;
		}
		return getContainer().find(modelKey);
	}

	public List<BeanInfo> getBeanInfoList(Class<?> cls) {
		List<BeanInfo> list=new ArrayList<>();
		for (BeanInfo beanInfo : getCache().values()) {
			if(cls.isAssignableFrom(beanInfo.getOwner().getTarget())) {
				list.add(beanInfo);
			}
		}
		return list;
	}

}
