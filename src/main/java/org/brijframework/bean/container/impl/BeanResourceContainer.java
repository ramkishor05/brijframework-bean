package org.brijframework.bean.container.impl;

import org.brijframework.bean.factories.resource.BeanResourceFactory;
import org.brijframework.bean.group.resource.BeanResourceGroup;
import org.brijframework.container.impl.module.AbstractModuleContainer;
import org.brijframework.group.Group;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.util.factories.ReflectionFactory;
import org.brijframework.util.reflect.InstanceUtil;

public class BeanResourceContainer extends AbstractModuleContainer{

	private static BeanResourceContainer container;

	@SingletonFactory
	public static BeanResourceContainer getContainer() {
		if (container == null) {
			container = new BeanResourceContainer();
		}
		return container;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionFactory.getFactory().getClassListFromExternal().forEach(cls -> {
				if (BeanResourceFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanResourceFactory<?,?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionFactory.getFactory().getClassListFromInternal().forEach(cls -> {
				if (BeanResourceFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanResourceFactory<?,?>>) cls);
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
			group = new BeanResourceGroup(groupKey);
			this.add(groupKey, group);
		}
		return group;
	}
	
	
}
