package org.brijframework.bean.factories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.bean.BeanSetup;
import org.brijframework.bean.factories.BeanSetupGroupFactory;
import org.brijframework.container.Container;
import org.brijframework.group.Group;
import org.brijframework.support.model.Assignable;

public class BeanSetupFactoryImpl implements BeanSetupGroupFactory<BeanSetup>{
	
	private static BeanSetupFactoryImpl factory;
	
	private Container container;
	
	private ConcurrentHashMap<String, BeanSetup> cache=new ConcurrentHashMap<>();

	@Assignable
	public static BeanSetupFactoryImpl getFactory() {
		if(factory==null) {
			factory=new BeanSetupFactoryImpl();
		}
		return factory;
	}

	@Override
	public BeanSetup getBeanSetup(String modelKey) {
		if(getCache().containsKey(modelKey)) {
			return getCache().get(modelKey);
		}
		return getContainer(modelKey);
	}
	
	@Override
	public List<BeanSetup> getBeanSetupList(String model) {
		List<BeanSetup> list=new ArrayList<>();
		for(BeanSetup setup:getCache().values()) {
			if(setup.getModel().equals(model)) {
				list.add(setup);
			}
		}
		return list;
	}
	
	@Override
	public BeanSetup register(BeanSetup dataSetup) {
		loadContainer(dataSetup);
		getCache().put(dataSetup.getId(), dataSetup);
		System.err.println("Bean Load    : "+dataSetup.getId());
		return dataSetup;
	}
	
	@Override
	public BeanSetupFactoryImpl loadFactory() {
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
	public ConcurrentHashMap<String, BeanSetup> getCache() {
		if(getContainer()!=null) {
			for(Group  group:getContainer().getCache().values()) {
				group.getCache().forEach((key,value)->{
					cache.put(key.toString(), (BeanSetup)value);
				});
			}
		}
		return cache;
	}

	@Override
	public BeanSetupFactoryImpl clear() {
		getCache().clear();
		return this;
	}
	

	public void loadContainer(BeanSetup metaInfo) {
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
	

	public BeanSetup getContainer(String modelKey) {
		if (getContainer() == null) {
			return null;
		}
		return getContainer().find(modelKey);
	}
}
