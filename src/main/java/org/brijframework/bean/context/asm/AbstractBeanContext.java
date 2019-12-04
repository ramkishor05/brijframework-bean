package org.brijframework.bean.context.asm;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.brijframework.bean.container.BeanContainer;
import org.brijframework.bean.context.BeanContext;
import org.brijframework.bean.factories.metadata.impl.BeanMetaDataFactoryImpl;
import org.brijframework.bean.factories.resource.impl.BeanResourceFactoryImpl;
import org.brijframework.bean.factories.scope.impl.BeanScopeFactoryImpl;
import org.brijframework.bean.meta.BeanMetaData;
import org.brijframework.bean.resource.BeanResource;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.context.impl.module.AbstractModuleContext;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public abstract class AbstractBeanContext extends AbstractModuleContext implements BeanContext {

	@Override
	@SuppressWarnings("unchecked")
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls->{
				if(BeanContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls->{
				if(BeanContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.brijframework.bean.context.BeanContext#getBeanObject(java.lang.String)
	 */
	@Override
	public <T> T getBeanObject(String name) {
		BeanMetaData beanMetaData = BeanMetaDataFactoryImpl.getFactory().find(name);
		if(beanMetaData==null) {
			return null;
		}
		String uniqueID = BeanScopeFactoryImpl.getFactory().getUniqueID(beanMetaData);
		return BeanScopeFactoryImpl.getFactory().getBeanScope(beanMetaData, uniqueID);
	}
	
	@Override
	public <T> T getBeanObject(Class<? extends Object> beanClass) {
		BeanMetaData beanMetaData = BeanMetaDataFactoryImpl.getFactory().find(beanClass.getSimpleName());
		if(beanMetaData==null) {
			return null;
		}
		String uniqueID = BeanScopeFactoryImpl.getFactory().getUniqueID(beanMetaData);
		return BeanScopeFactoryImpl.getFactory().getBeanScope(beanMetaData, uniqueID);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getBeanObject(String name,Class<? extends Object> beanClass) {
		BeanScope find = BeanScopeFactoryImpl.getFactory().find(name);
		if(find!=null && beanClass.isAssignableFrom(find.getDatainfo().getOwner().getType())) {
			return (T) find.getScopeObject();
		}
		BeanMetaData beanMetaData = BeanMetaDataFactoryImpl.getFactory().find(name);
		if(beanMetaData==null) {
			return null;
		}
		if(!beanClass.isAssignableFrom(beanMetaData.getOwner().getType())) {
			return null;
		}
		String uniqueID = BeanScopeFactoryImpl.getFactory().getUniqueID(beanMetaData);
		return BeanScopeFactoryImpl.getFactory().getBeanScope(beanMetaData, uniqueID);
	}

	@Override
	public List<?> getBeanObjects(Class<? extends Object> beanClass) {
		List<Object> objects=new ArrayList<Object>();
		List<BeanScope> findAll = BeanScopeFactoryImpl.getFactory().findAll(beanClass);
		if(!CollectionUtils.isEmpty(findAll)) {
			for (BeanScope beanScope : findAll) {
				objects.add(beanScope.getScopeObject());
			}
		}
		return objects;
	}

	@Override
	public List<String> getRegisteredBeanNames() {
		return BeanMetaDataFactoryImpl.getFactory().getBeanNames();
	}

	@Override
	public List<String> getRegisteredBeanNames(Class<?> beanClass) {
		return BeanMetaDataFactoryImpl.getFactory().getBeanNames(beanClass);
	}

	@Override
	public BeanResource getBeanResource(String name) {
		return BeanResourceFactoryImpl.getFactory().find(name);
	}

	@Override
	public List<? extends BeanResource> getBeanResourceList(String model) {
		return BeanResourceFactoryImpl.getFactory().findAllByModel(model);
	}
	
	@Override
	public List<? extends BeanResource> getBeanResourceList() {
		List<BeanResource> list=new ArrayList<>();
		for(BeanResource beanResource:BeanResourceFactoryImpl.getFactory().getCache().values()) {
			list.add(beanResource);
		}
		return list ;
	}

	@Override
	public BeanMetaData getBeanMetaData(String name) {
		return BeanMetaDataFactoryImpl.getFactory().find(name);
	}

	@Override
	public List<? extends BeanMetaData> getBeanMetaDataList() {
		List<BeanMetaData> list=new ArrayList<>();
		for(BeanMetaData beanMetaData:BeanMetaDataFactoryImpl.getFactory().getCache().values()) {
			list.add(beanMetaData);
		}
		return list;
	}

	@Override
	public List<? extends BeanMetaData> getBeanMetaDataList(String model) {
		return BeanMetaDataFactoryImpl.getFactory().findAllByModel(model);
	}

	@Override
	public List<?> getBeanResourceNames() {
		List<String> list=new ArrayList<>();
		Enumeration<String> keys = BeanResourceFactoryImpl.getFactory().getCache().keys();
		while(keys.hasMoreElements()) {
			list.add(keys.nextElement());
		}
		return list ;
	}

	@Override
	public List<?> getBeanResourceNames(String model) {
		List<String> list=new ArrayList<>();
		for(Entry<String, BeanResource>entry:BeanResourceFactoryImpl.getFactory().getCache().entrySet()) {
			if(model.equals(entry.getValue().getModel()))
			list.add(entry.getKey());
		}
		return list ;
	}

	@Override
	public List<?> getBeanMetaDataNames() {
		List<String> list=new ArrayList<>();
		Enumeration<String> keys = BeanMetaDataFactoryImpl.getFactory().getCache().keys();
		while(keys.hasMoreElements()) {
		   list.add(keys.nextElement());
		}
		return list ;
	}

	@Override
	public List<?> getBeanMetaDataNames(String model) {
		List<String> list=new ArrayList<>();
		for(Entry<String, BeanMetaData> entry:BeanMetaDataFactoryImpl.getFactory().getCache().entrySet()) {
			if(model.equals(entry.getValue().getOwner().getId()))
			list.add(entry.getKey());
		}
		return list ;
	}
	
	@Override
	public List<? extends BeanMetaData> getBeanMetaDataList(Class<?> metaClass) {
		List<BeanMetaData> list=new ArrayList<>();
		for(Entry<String, BeanMetaData> entry:BeanMetaDataFactoryImpl.getFactory().getCache().entrySet()) {
			if(metaClass.isAssignableFrom(entry.getValue().getOwner().getType()))
			list.add(entry.getValue());
		}
		return list ;
	}

}
