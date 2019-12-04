package org.brijframework.bean.container.impl;

import org.brijframework.bean.container.BeanContainer;
import org.brijframework.bean.factories.scope.BeanScopeFactory;
import org.brijframework.bean.group.scope.BeanScopeGroup;
import org.brijframework.container.impl.module.AbstractModuleContainer;
import org.brijframework.group.Group;
import org.brijframework.support.config.DepandOn;
import org.brijframework.support.config.SingletonFactory;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

@DepandOn(depand=BeanMetaDataContainer.class)
public class BeanScopeContainer extends AbstractModuleContainer implements BeanContainer{

	private static BeanScopeContainer container;

	@SingletonFactory
	public static BeanScopeContainer getContainer() {
		if (container == null) {
			container = InstanceUtil.getSingletonInstance(BeanScopeContainer.class);
		}
		return container;
	}

	@Override
	public Group load(Object groupKey) {
		Group group = get(groupKey);
		if (group == null) {
			group = new BeanScopeGroup(groupKey);
			this.add(groupKey, group);
		}
		return group;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (BeanScopeFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanScopeFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (BeanScopeFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanScopeFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
