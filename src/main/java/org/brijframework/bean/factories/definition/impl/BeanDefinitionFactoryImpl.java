package org.brijframework.bean.factories.definition.impl;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.factories.definition.asm.AbstractBeanDefinitionFactory;
import org.brijframework.group.Group;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.support.ordering.OrderOn;

@OrderOn(3)
public final class BeanDefinitionFactoryImpl extends AbstractBeanDefinitionFactory{
	
	private static BeanDefinitionFactoryImpl factory;
	
	@SingletonFactory
	public static BeanDefinitionFactoryImpl getFactory() {
		if(factory==null) {
			factory=new BeanDefinitionFactoryImpl();
		}
		return factory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BeanDefinitionFactoryImpl loadFactory() {
		for(Entry<Object, Group> entryGroup:getContainer().getCache().entrySet()) {
			ConcurrentHashMap<String, BeanDefinition> cache= entryGroup.getValue().getCache();
			for(Entry<String, BeanDefinition> entryObject: cache.entrySet()) {
				preregister(entryObject.getKey(), entryObject.getValue());
				this.getCache().put(entryObject.getKey(), entryObject.getValue());
				postregister(entryObject.getKey(), entryObject.getValue());
			}
		}
		return this;
	}
	
}
