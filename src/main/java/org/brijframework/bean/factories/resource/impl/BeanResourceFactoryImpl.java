package org.brijframework.bean.factories.resource.impl;

import org.brijframework.bean.factories.resource.asm.AbstractBeanResourceFactory;
import org.brijframework.bean.resource.BeanResource;
import org.brijframework.group.Group;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.support.ordering.OrderOn;

@OrderOn(3)
public final class BeanResourceFactoryImpl extends AbstractBeanResourceFactory{
	
	private static BeanResourceFactoryImpl factory;
	
	@SingletonFactory
	public static BeanResourceFactoryImpl getFactory() {
		if(factory==null) {
			factory=new BeanResourceFactoryImpl();
		}
		return factory;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BeanResourceFactoryImpl loadFactory() {
		if(getContainer()==null) {
			return this;
		}
		for(Group  group:getContainer().getCache().values()) {
			group.getCache().forEach((key,value)->{
				getCache().put(key.toString(), (BeanResource)value);
			});
		}
		return this;
	}

	

}
