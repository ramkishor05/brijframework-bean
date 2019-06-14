package org.brijframework.data.factories.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.data.factories.DataSetupFactory;
import org.brijframework.data.setup.ClassDataSetup;
import org.brijframework.group.Group;
import org.brijframework.support.model.Assignable;

public class DataSetupFactoryImpl implements DataSetupFactory{
	
	private static DataSetupFactoryImpl factory;
	private Container container;
	
	private ConcurrentHashMap<Object, ClassDataSetup> cache=new ConcurrentHashMap<>();

	@Assignable
	public static DataSetupFactoryImpl getFactory() {
		if(factory==null) {
			factory=new DataSetupFactoryImpl();
		}
		return factory;
	}

	@Override
	public ClassDataSetup getDataSetup(String modelKey) {
		if(getCache().containsKey(modelKey)) {
			return getCache().get(modelKey);
		}
		return getContainer(modelKey);
	}
	
	@Override
	public ClassDataSetup register(ClassDataSetup dataSetup) {
		getCache().put(dataSetup.getId(), dataSetup);
		System.err.println("Data Info    : "+dataSetup.getId());
		loadContainer(dataSetup);
		return dataSetup;
	}
	
	@Override
	public DataSetupFactoryImpl loadFactory() {
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
	public ConcurrentHashMap<Object, ClassDataSetup> getCache() {
		if(getContainer()!=null) {
			for(Group  group:getContainer().getCache().values()) {
				group.getCache().forEach((key,value)->{
					cache.put(key, (ClassDataSetup)value);
				});
			}
		}
		return cache;
	}

	@Override
	public DataSetupFactoryImpl clear() {
		getCache().clear();
		return this;
	}
	

	public void loadContainer(ClassDataSetup metaInfo) {
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
	

	public ClassDataSetup getContainer(String modelKey) {
		if (getContainer() == null) {
			return null;
		}
		return getContainer().find(modelKey);
	}

	public List<ClassDataSetup> getDataSetupList(String model) {
		List<ClassDataSetup> list=new ArrayList<>();
		for(ClassDataSetup setup:getCache().values()) {
			if(setup.getModel().equals(model)) {
				list.add(setup);
			}
		}
		return list;
	}

}
