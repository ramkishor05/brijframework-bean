package org.brijframework.data.container;

import org.brijframework.asm.container.AbstractContainer;
import org.brijframework.data.group.DataSetupGroup;
import org.brijframework.group.Group;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;

@DepandOn(depand=ClassDataContainer.class)
public class PropertyDataContainer extends AbstractContainer implements DataContainer{

	private static PropertyDataContainer container;

	@Assignable
	public static PropertyDataContainer getContainer() {
		if (container == null) {
			container = new PropertyDataContainer();
		}
		return container;
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
