package org.brijframework.data.container.asm;

import org.brijframework.asm.container.AbstractContainer;
import org.brijframework.data.container.DataContainer;
import org.brijframework.data.factories.ClassDataInfoFactory;
import org.brijframework.data.group.DataSetupGroup;
import org.brijframework.group.Group;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public class ClassDataInfoContainer extends AbstractContainer implements DataContainer {

	private static ClassDataInfoContainer container;

	@Assignable
	public static ClassDataInfoContainer getContainer() {
		if (container == null) {
			container = new ClassDataInfoContainer();
		}
		return container;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (ClassDataInfoFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends ClassDataInfoFactory<?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (ClassDataInfoFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends ClassDataInfoFactory<?>>) cls);
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
