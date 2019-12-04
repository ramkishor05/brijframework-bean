package org.brijframework.bean.container.impl;

import org.brijframework.bean.container.BeanContainer;
import org.brijframework.bean.factories.metadata.BeanMetaDataFactory;
import org.brijframework.bean.group.beanmeta.BeanMetaDataGroup;
import org.brijframework.container.impl.module.AbstractModuleContainer;
import org.brijframework.group.Group;
import org.brijframework.support.config.DepandOn;
import org.brijframework.support.config.SingletonFactory;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

@DepandOn(depand=BeanResourceContainer.class)
public class BeanMetaDataContainer extends AbstractModuleContainer implements BeanContainer{

	private static BeanMetaDataContainer container;

	@SingletonFactory
	public static BeanMetaDataContainer getContainer() {
		if (container == null) {
			container = new BeanMetaDataContainer();
		}
		return container;
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (BeanMetaDataFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanMetaDataFactory<?,?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (BeanMetaDataFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanMetaDataFactory<?,?>>) cls);
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
