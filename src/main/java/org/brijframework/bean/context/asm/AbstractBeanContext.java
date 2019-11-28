package org.brijframework.bean.context.asm;

import java.util.ArrayList;
import java.util.List;

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
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public class AbstractBeanContext extends AbstractModuleContext implements BeanContext {

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
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBeanObject(String name) {
		BeanScope find = BeanScopeFactoryImpl.getFactory().find(name);
		if(find!=null) {
			return (T) find.getScopeObject();
		}
		BeanMetaData beanMetaData = BeanMetaDataFactoryImpl.getFactory().find(name);
		Assertion.isNull(beanMetaData, "Bean not found");;
		BeanScope register = BeanScopeFactoryImpl.getFactory().register(name, beanMetaData);
		return (T) register.getScopeObject();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBeanObject(Class<? extends Object> beanClass) {
		BeanScope find = BeanScopeFactoryImpl.getFactory().find(beanClass.getSimpleName());
		if(find!=null) {
			return (T) find.getScopeObject();
		}
		BeanMetaData beanMetaData = BeanMetaDataFactoryImpl.getFactory().find(beanClass.getSimpleName());
		Assertion.isNull(beanMetaData, "Bean not found");;
		BeanScope register = BeanScopeFactoryImpl.getFactory().register(beanClass.getSimpleName(), beanMetaData);
		return (T) register.getScopeObject();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getBeanObject(String name,Class<? extends Object> beanClass) {
		BeanScope find = BeanScopeFactoryImpl.getFactory().find(name);
		if(find!=null && beanClass.isAssignableFrom(find.getDatainfo().getOwner().getTarget())) {
			return (T) find.getScopeObject();
		}
		BeanMetaData beanMetaData = BeanMetaDataFactoryImpl.getFactory().find(name);
		if(beanMetaData==null || !beanClass.isAssignableFrom(find.getDatainfo().getOwner().getTarget())) {
			return null;
		}
		Assertion.isNull(beanMetaData, "Bean not found");;
		BeanScope register = BeanScopeFactoryImpl.getFactory().register(name, beanMetaData);
		return (T) register.getScopeObject();
	}

	@Override
	public List<?> getBeanObjects(Class<? extends Object> beanClass) {
		List<Object> objects=new ArrayList<Object>();
		List<BeanScope> findAll = BeanScopeFactoryImpl.getFactory().findAll(beanClass);
		if(!CollectionUtils.isEmpty(findAll)) {
			for (BeanScope beanScope : findAll) {
				objects.add(beanScope.getScopeObject());
			}
			return objects;
		}
		List<BeanMetaData> beanMetaDatas = BeanMetaDataFactoryImpl.getFactory().findAll(beanClass);
		for(BeanMetaData beanMetaData:beanMetaDatas) {
			BeanScope register = BeanScopeFactoryImpl.getFactory().register(beanClass.getSimpleName(), beanMetaData);
			objects.add(register.getScopeObject());
		}
		return objects;
	}

	@Override
	public List<String> getBeanObjectNames() {
		return BeanMetaDataFactoryImpl.getFactory().getBeanNames();
	}

	@Override
	public List<String> getBeanObjectNames(Class<?> beanClass) {
		return BeanMetaDataFactoryImpl.getFactory().getBeanNames(beanClass);
	}

	@Override
	public BeanResource getBeanResource(String name) {
		return BeanResourceFactoryImpl.getFactory().find(name);
	}

	@Override
	public BeanResource getBeanResource(Class<? extends Object> beanClass) {
		return BeanResourceFactoryImpl.getFactory().find(beanClass);
	}

	@Override
	public BeanResource getBeanResource(String name, Class<?> beanClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends BeanResource> getBeanResources(Class<? extends Object> beanClass) {
		// TODO Auto-generated method stub
		return null;
	}

}
