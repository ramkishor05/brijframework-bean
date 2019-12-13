package org.brijframework.bean.context.impl;

import org.brijframework.bean.context.asm.AbstractBeanContext;
import org.brijframework.model.context.ModelContext;
import org.brijframework.support.ordering.DepandOn;

@DepandOn(depand=ModelContext.class)
public final class BeanContextImpl extends AbstractBeanContext{

	
	
}