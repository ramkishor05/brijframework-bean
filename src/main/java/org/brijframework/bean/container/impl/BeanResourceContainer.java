package org.brijframework.bean.container.impl;

import org.brijframework.bean.factories.BeanResourceGroupFactory;
import org.brijframework.bean.group.BeanSetupGroup;
import org.brijframework.container.impl.AbstractModuleContainer;
import org.brijframework.group.Group;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.printer.ConsolePrint;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public class BeanResourceContainer extends AbstractModuleContainer{

	private static BeanResourceContainer container;

	@Assignable
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
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (BeanResourceGroupFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanResourceGroupFactory<?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (BeanResourceGroupFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanResourceGroupFactory<?>>) cls);
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
			ConsolePrint.screen("Resource", "Registery for bean resource group with id :"+groupKey);
			this.add(groupKey, group);
		}
		return group;
	}
}
