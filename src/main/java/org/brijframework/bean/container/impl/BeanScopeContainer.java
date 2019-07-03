package org.brijframework.bean.container.impl;

import org.brijframework.asm.container.AbstractContainer;
import org.brijframework.bean.container.BeanContainer;
import org.brijframework.bean.factories.impl.BeanFactory;
import org.brijframework.bean.group.BeanScopeGroup;
import org.brijframework.group.Group;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.config.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

@DepandOn(depand=BeanInfoContainer.class)
public class BeanScopeContainer extends AbstractContainer implements BeanContainer {

	private static BeanScopeContainer container;

	@Assignable
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
				if (BeanFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (BeanFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
