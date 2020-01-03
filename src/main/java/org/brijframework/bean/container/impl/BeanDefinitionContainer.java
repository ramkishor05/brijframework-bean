package org.brijframework.bean.container.impl;

import org.brijframework.bean.container.BeanContainer;
import org.brijframework.bean.factories.definition.BeanDefinitionFactory;
import org.brijframework.bean.group.beanmeta.BeanMetaDataGroup;
import org.brijframework.container.impl.module.AbstractModuleContainer;
import org.brijframework.group.Group;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.support.ordering.DepandOn;
import org.brijframework.util.factories.ReflectionFactory;
import org.brijframework.util.reflect.InstanceUtil;

@DepandOn(depand=BeanResourceContainer.class)
public class BeanDefinitionContainer extends AbstractModuleContainer implements BeanContainer{

	private static BeanDefinitionContainer container;

	@SingletonFactory
	public static BeanDefinitionContainer getContainer() {
		if (container == null) {
			container = new BeanDefinitionContainer();
		}
		return container;
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public void init() {
		try {
			ReflectionFactory.getFactory().getExternalClassList().forEach(cls -> {
				if (BeanDefinitionFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanDefinitionFactory<?,?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionFactory.getFactory().getInternalClassList().forEach(cls -> {
				if (BeanDefinitionFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanDefinitionFactory<?,?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Group load(Object groupKey) {
		Group group = get(groupKey);
		if (group == null) {
			group = new BeanMetaDataGroup(groupKey);
			this.add(groupKey, group);
		}
		return group;
	}

	@Override
	public boolean containsObject(Object key) {
		return false;
	}
}
