package org.brijframework.bean.container.impl;

import org.brijframework.asm.container.AbstractContainer;
import org.brijframework.bean.container.BeanContainer;
import org.brijframework.bean.factories.BeanSetupGroupFactory;
import org.brijframework.bean.group.BeanSetupGroup;
import org.brijframework.group.Group;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public class BeanSetupContainer extends AbstractContainer implements BeanContainer {

	private static BeanSetupContainer container;

	@Assignable
	public static BeanSetupContainer getContainer() {
		if (container == null) {
			container = new BeanSetupContainer();
		}
		return container;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (BeanSetupGroupFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanSetupGroupFactory<?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (BeanSetupGroupFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanSetupGroupFactory<?>>) cls);
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
			group = new BeanSetupGroup(groupKey);
			System.err.println("Group        : " + groupKey);
			this.add(groupKey, group);
		}
		return group;
	}
}
