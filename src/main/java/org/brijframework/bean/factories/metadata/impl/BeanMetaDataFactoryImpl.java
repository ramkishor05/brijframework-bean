package org.brijframework.bean.factories.metadata.impl;

import java.util.Map.Entry;

import org.brijframework.bean.factories.metadata.asm.AbstractBeanMetaDataFactory;
import org.brijframework.group.Group;
import org.brijframework.support.config.OrderOn;
import org.brijframework.support.config.SingletonFactory;

@OrderOn(3)
public final class BeanMetaDataFactoryImpl extends AbstractBeanMetaDataFactory{
	
	private static BeanMetaDataFactoryImpl factory;
	
	@SingletonFactory
	public static BeanMetaDataFactoryImpl getFactory() {
		if(factory==null) {
			factory=new BeanMetaDataFactoryImpl();
		}
		return factory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BeanMetaDataFactoryImpl loadFactory() {
		for(Entry<Object, Group> entry:getContainer().getCache().entrySet()) {
			this.getCache().putAll(entry.getValue().getCache());
		}
		return this;
	}
	
}
