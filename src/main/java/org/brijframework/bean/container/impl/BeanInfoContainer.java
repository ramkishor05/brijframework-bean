package org.brijframework.bean.container.impl;

import org.brijframework.asm.container.AbstractContainer;
import org.brijframework.bean.container.BeanContainer;
import org.brijframework.bean.factories.BeanInfoGroupFactory;
import org.brijframework.bean.group.BeanSetupGroup;
import org.brijframework.group.Group;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

@DepandOn(depand=BeanSetupContainer.class)
public class BeanInfoContainer extends AbstractContainer implements BeanContainer {

	private static BeanInfoContainer container;

	@Assignable
	public static BeanInfoContainer getContainer() {
		if (container == null) {
			container = new BeanInfoContainer();
		}
		return container;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (BeanInfoGroupFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanInfoGroupFactory<?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (BeanInfoGroupFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanInfoGroupFactory<?>>) cls);
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
