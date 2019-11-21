package org.brijframework.bean.context;

import org.brijframework.bean.container.BeanContainer;
import org.brijframework.context.module.impl.AbstractModuleContext;
import org.brijframework.model.context.ModelContext;
import org.brijframework.support.config.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

@DepandOn(depand=ModelContext.class)
public class BeanContext extends AbstractModuleContext{

	@Override
	@SuppressWarnings("unchecked")
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls->{
				if(BeanContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls->{
				if(BeanContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends BeanContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
