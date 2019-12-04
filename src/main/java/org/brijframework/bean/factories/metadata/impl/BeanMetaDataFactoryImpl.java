package org.brijframework.bean.factories.metadata.impl;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.bean.factories.metadata.asm.AbstractBeanMetaDataFactory;
import org.brijframework.bean.meta.BeanMetaData;
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
		for(Entry<Object, Group> entryGroup:getContainer().getCache().entrySet()) {
			ConcurrentHashMap<String, BeanMetaData> cache= entryGroup.getValue().getCache();
			for(Entry<String, BeanMetaData> entryObject: cache.entrySet()) {
				preregister(entryObject.getKey(), entryObject.getValue());
				this.getCache().put(entryObject.getKey(), entryObject.getValue());
				postregister(entryObject.getKey(), entryObject.getValue());
			}
		}
		return this;
	}
	
}
