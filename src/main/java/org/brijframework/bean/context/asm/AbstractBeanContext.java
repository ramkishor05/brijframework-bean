package org.brijframework.bean.context.asm;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.brijframework.bean.container.BeanContainer;
import org.brijframework.bean.context.BeanContext;
import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.factories.definition.impl.BeanDefinitionFactoryImpl;
import org.brijframework.bean.factories.impl.BeanScopeFactoryImpl;
import org.brijframework.bean.factories.resource.impl.BeanResourceFactoryImpl;
import org.brijframework.bean.resource.BeanResource;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.context.impl.module.AbstractModuleContext;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.factories.ReflectionFactory;
import org.brijframework.util.reflect.InstanceUtil;

public abstract class AbstractBeanContext extends AbstractModuleContext implements BeanContext {

	@Override
	@SuppressWarnings("unchecked")
	public void init() {
		try {
			ReflectionFactory.getFactory().getExternalClassList().forEach(cls->{
				if(BeanContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionFactory.getFactory().getInternalClassList().forEach(cls->{
				if(BeanContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Bean Object
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String name) {
		BeanDefinition definition = BeanDefinitionFactoryImpl.getFactory().find(name);
		if(definition==null) {
			return null;
		}
		String uniqueID = BeanScopeFactoryImpl.getFactory().getUniqueID(definition);
		return (T) BeanScopeFactoryImpl.getFactory().getBeanScope(definition, uniqueID).getScopeObject();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(Class<? extends Object> beanClass) {
		BeanDefinition beanMetaData = BeanDefinitionFactoryImpl.getFactory().find(beanClass.getSimpleName());
		if(beanMetaData==null) {
			return null;
		}
		String uniqueID = BeanScopeFactoryImpl.getFactory().getUniqueID(beanMetaData);
		return (T) BeanScopeFactoryImpl.getFactory().getBeanScope(beanMetaData, uniqueID).getScopeObject();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getBean(String name, Class<T> beanClass) {
		BeanDefinition beanMetaData = BeanDefinitionFactoryImpl.getFactory().find(name);
		if(beanMetaData==null) {
			return null;
		}
		if(!beanClass.isAssignableFrom(beanMetaData.getOwner().getType())) {
			return null;
		}
		String uniqueID = BeanScopeFactoryImpl.getFactory().getUniqueID(beanMetaData);
		return (T) BeanScopeFactoryImpl.getFactory().getBeanScope(beanMetaData, uniqueID).getScopeObject();
	}

	@Override
	public List<?> getBeanList(Class<? extends Object> beanClass) {
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
	public List<String> getBeanNameList() {
		return BeanDefinitionFactoryImpl.getFactory().getBeanNames();
	}

	@Override
	public List<String> getBeanNameList(Class<?> beanClass) {
		return BeanDefinitionFactoryImpl.getFactory().getBeanNames(beanClass);
	}

	@Override
	public List<?> getBeanList(Scope scope) {
		List<Object> list=new ArrayList<>();
		for (Entry<String, BeanScope> entry : BeanScopeFactoryImpl.getFactory().getCache().entrySet()) {
			if(entry.getValue().getBeanDefinition().getScope().equals(scope)) {
				list.add(entry.getValue().getScopeObject());
			}
		}
		return list;
	}
	
	/*
	 * Bean Resource
	 */
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
	public List<?> getBeanResourceNameList() {
		List<String> list=new ArrayList<>();
		Enumeration<String> keys = BeanResourceFactoryImpl.getFactory().getCache().keys();
		while(keys.hasMoreElements()) {
			list.add(keys.nextElement());
		}
		return list ;
	}

	@Override
	public List<?> getBeanResourceNamesList(String model) {
		List<String> list=new ArrayList<>();
		for(Entry<String, BeanResource>entry:BeanResourceFactoryImpl.getFactory().getCache().entrySet()) {
			if(model.equals(entry.getValue().getModel())) {
				list.add(entry.getKey());
			}
		}
		return list ;
	}
	
	@Override
	public List<BeanResource> getBeanResourceList(Scope scope) {
		List<BeanResource> list=new ArrayList<>();
		for (Entry<String, BeanResource> entry : BeanResourceFactoryImpl.getFactory().getCache().entrySet()) {
			if(entry.getValue().getScope().equalsIgnoreCase(scope.getName())) {
				list.add(entry.getValue());
			}
		}
		return list;
	}

	/*
	 * BeanDefinition
	 */

	@Override
	public BeanDefinition getBeanDefinition(String name) {
		return BeanDefinitionFactoryImpl.getFactory().find(name);
	}

	@Override
	public List<? extends BeanDefinition> getBeanDefinitionList() {
		List<BeanDefinition> list=new ArrayList<>();
		for(BeanDefinition beanMetaData:BeanDefinitionFactoryImpl.getFactory().getCache().values()) {
			list.add(beanMetaData);
		}
		return list;
	}

	@Override
	public List<? extends BeanDefinition> getBeanDefinitionList(String model) {
		return BeanDefinitionFactoryImpl.getFactory().findAllByModel(model);
	}
	
	@Override
	public List<?> getBeanDefinitionNameList() {
		List<String> list=new ArrayList<>();
		Enumeration<String> keys = BeanDefinitionFactoryImpl.getFactory().getCache().keys();
		while(keys.hasMoreElements()) {
		   list.add(keys.nextElement());
		}
		return list ;
	}

	@Override
	public List<?> getBeanDefinitionNameList(String model) {
		List<String> list=new ArrayList<>();
		for(Entry<String, BeanDefinition> entry:BeanDefinitionFactoryImpl.getFactory().getCache().entrySet()) {
			if(model.equals(entry.getValue().getOwner().getId())) {
			  list.add(entry.getKey());
			}
		}
		return list ;
	}
	
	@Override
	public List<? extends BeanDefinition> getBeanDefinitionList(Class<?> metaClass) {
		List<BeanDefinition> list=new ArrayList<>();
		for(Entry<String, BeanDefinition> entry:BeanDefinitionFactoryImpl.getFactory().getCache().entrySet()) {
			if(metaClass.isAssignableFrom(entry.getValue().getOwner().getType())) {
			  list.add(entry.getValue());
			}
		}
		return list ;
	}
	
	@Override
	public List<BeanDefinition> getBeanDefinitionList(Scope scope) {
		List<BeanDefinition> list=new ArrayList<>();
		for (Entry<String, BeanDefinition> entry : BeanDefinitionFactoryImpl.getFactory().getCache().entrySet()) {
			if(entry.getValue().getScope().equalsIgnoreCase(scope)) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
}
