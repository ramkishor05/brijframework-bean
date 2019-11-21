package org.brijframework.bean.factories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.bean.factories.BeanResourceGroupFactory;
import org.brijframework.bean.resource.BeanResource;
import org.brijframework.container.Container;
import org.brijframework.group.Group;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.printer.ConsolePrint;

public class BeanSetupFactoryImpl implements BeanResourceGroupFactory<BeanResource>{
	
	private static BeanSetupFactoryImpl factory;
	
	private Container container;
	
	private ConcurrentHashMap<String, BeanResource> cache=new ConcurrentHashMap<>();

	@Assignable
	public static BeanSetupFactoryImpl getFactory() {
		if(factory==null) {
			factory=new BeanSetupFactoryImpl();
		}
		return factory;
	}

	@Override
	public BeanResource find(String modelKey) {
		if(getCache().containsKey(modelKey)) {
			return getCache().get(modelKey);
		}
		return getContainer(modelKey);
	}
	
	@Override
	public List<BeanResource> findAllByModel(String model) {
		List<BeanResource> list=new ArrayList<>();
		for(BeanResource setup:getCache().values()) {
			if(setup.getModel().equals(model)) {
				list.add(setup);
			}
		}
		return list;
	}
	
	@Override
	public BeanResource register(BeanResource dataSetup) {
		validate(dataSetup);
		loadContainer(dataSetup);
		ConsolePrint.screen("Resource", "Load bean resource with id :"+dataSetup.getId());
		getCache().put(dataSetup.getId(), dataSetup);
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
	public ConcurrentHashMap<String, BeanResource> getCache() {
		if(getContainer()!=null) {
			for(Group  group:getContainer().getCache().values()) {
				group.getCache().forEach((key,value)->{
					cache.put(key.toString(), (BeanResource)value);
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
	

	public void loadContainer(BeanResource metaInfo) {
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
	

	public BeanResource getContainer(String modelKey) {
		if (getContainer() == null) {
			return null;
		}
		return getContainer().find(modelKey);
	}

	@Override
	public boolean validate(BeanResource dataSetup) {
		Assertion.notNull(dataSetup.getId(), "Bean id should not be null or empty");
		Assertion.isTrue(dataSetup.getModel()==null && dataSetup.getTarget()==null , "Bean model or type at least one spacified.");
		return true;
	}
}
