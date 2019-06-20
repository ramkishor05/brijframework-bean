package org.brijframework.data.container.asm;

import org.brijframework.asm.container.AbstractContainer;
import org.brijframework.data.container.DataContainer;
import org.brijframework.data.factories.ClassDataSetupFactory;
import org.brijframework.data.group.DataSetupGroup;
import org.brijframework.group.Group;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public class DataSetupContainer extends AbstractContainer implements DataContainer {

	private static DataSetupContainer container;

	@Assignable
	public static DataSetupContainer getContainer() {
		if (container == null) {
			container = new DataSetupContainer();
		}
		return container;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (ClassDataSetupFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends ClassDataSetupFactory<?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (ClassDataSetupFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends ClassDataSetupFactory<?>>) cls);
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
			group = new DataSetupGroup(groupKey);
			System.err.println("Group        : " + groupKey);
			this.add(groupKey, group);
		}
		return group;
	}
}
