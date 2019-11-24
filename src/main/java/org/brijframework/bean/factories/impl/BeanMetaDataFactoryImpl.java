package org.brijframework.bean.factories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.bean.factories.BeanMetaDataGroupFactory;
import org.brijframework.bean.meta.BeanMetaData;
import org.brijframework.container.Container;
import org.brijframework.factories.impl.AbstractFactory;
import org.brijframework.group.Group;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.printer.ConsolePrint;

public class BeanMetaDataFactoryImpl extends AbstractFactory<String,BeanMetaData> implements BeanMetaDataGroupFactory<String,BeanMetaData>{
	
	private static BeanMetaDataFactoryImpl factory;
	
	private Container container;
	
	private ConcurrentHashMap<String, BeanMetaData> cache=new ConcurrentHashMap<>();

	@Assignable
	public static BeanMetaDataFactoryImpl getFactory() {
		if(factory==null) {
			factory=new BeanMetaDataFactoryImpl();
		}
		return factory;
	}

	@Override
	public BeanMetaData find(String modelKey) {
		if(getCache().containsKey(modelKey)) {
			return getCache().get(modelKey);
		}
		return getContainer(modelKey);
	}
	
	@Override
	public List<BeanMetaData> findAllByModel(String model) {
		List<BeanMetaData> list=new ArrayList<>();
		for(BeanMetaData setup:getCache().values()) {
			if(setup.getOwner().getId().equals(model)) {
				list.add(setup);
			}
		}
		return list;
	}
	
	@Override
	public BeanMetaData register(String key,BeanMetaData dataSetup) {
		loadContainer(dataSetup);
		ConsolePrint.screen("Resource", "Registery for bean data with id :"+dataSetup.getId());
		getCache().put(dataSetup.getId(), dataSetup);
		return dataSetup;
	}
	
	@Override
	public BeanMetaDataFactoryImpl loadFactory() {
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
	public ConcurrentHashMap<String, BeanMetaData> getCache() {
		if(getContainer()!=null) {
			for(Group  group:getContainer().getCache().values()) {
				group.getCache().forEach((key,value)->{
					cache.put(key.toString(), (BeanMetaData)value);
				});
			}
		}
		return cache;
	}

	@Override
	public BeanMetaDataFactoryImpl clear() {
		getCache().clear();
		return this;
	}
	

	public void loadContainer(BeanMetaData metaInfo) {
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

	public BeanMetaData getContainer(String modelKey) {
		if (getContainer() == null) {
			return null;
		}
		return getContainer().find(modelKey);
	}

	public List<BeanMetaData> getBeanInfoList(Class<?> cls) {
		List<BeanMetaData> list=new ArrayList<>();
		for (BeanMetaData beanInfo : getCache().values()) {
			if(cls.isAssignableFrom(beanInfo.getOwner().getTarget())) {
				list.add(beanInfo);
			}
		}
		return list;
	}

	@Override
	protected void preregister(String key, BeanMetaData value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void postregister(String key, BeanMetaData value) {
		// TODO Auto-generated method stub
		
	}

}
