package org.brijframework.data.factories.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.data.factories.ClassDataInfoFactory;
import org.brijframework.data.info.ClassDataInfo;
import org.brijframework.group.Group;
import org.brijframework.support.model.Assignable;

public class ClassDataInfoFactoryImpl implements ClassDataInfoFactory<ClassDataInfo>{
	
	private static ClassDataInfoFactoryImpl factory;
	
	private Container container;
	
	private ConcurrentHashMap<Object, ClassDataInfo> cache=new ConcurrentHashMap<>();

	@Assignable
	public static ClassDataInfoFactoryImpl getFactory() {
		if(factory==null) {
			factory=new ClassDataInfoFactoryImpl();
		}
		return factory;
	}

	@Override
	public ClassDataInfo getData(String modelKey) {
		if(getCache().containsKey(modelKey)) {
			return getCache().get(modelKey);
		}
		return getContainer(modelKey);
	}
	
	@Override
	public List<ClassDataInfo> getDataList(String model) {
		List<ClassDataInfo> list=new ArrayList<>();
		for(ClassDataInfo setup:getCache().values()) {
			if(setup.getOwner().getId().equals(model)) {
				list.add(setup);
			}
		}
		return list;
	}
	
	@Override
	public ClassDataInfo register(ClassDataInfo dataSetup) {
		getCache().put(dataSetup.getId(), dataSetup);
		System.err.println("Data Info    : "+dataSetup.getId());
		loadContainer(dataSetup);
		return dataSetup;
	}
	
	@Override
	public ClassDataInfoFactoryImpl loadFactory() {
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
	public ConcurrentHashMap<Object, ClassDataInfo> getCache() {
		if(getContainer()!=null) {
			for(Group  group:getContainer().getCache().values()) {
				group.getCache().forEach((key,value)->{
					cache.put(key, (ClassDataInfo)value);
				});
			}
		}
		return cache;
	}

	@Override
	public ClassDataInfoFactoryImpl clear() {
		getCache().clear();
		return this;
	}
	

	public void loadContainer(ClassDataInfo metaInfo) {
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

	public ClassDataInfo getContainer(String modelKey) {
		if (getContainer() == null) {
			return null;
		}
		return getContainer().find(modelKey);
	}

}
