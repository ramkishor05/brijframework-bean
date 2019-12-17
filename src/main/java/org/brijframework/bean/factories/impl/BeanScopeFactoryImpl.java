package org.brijframework.bean.factories.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.factories.asm.AbstractBeanScopeFactory;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.container.Container;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.support.ordering.OrderOn;

@OrderOn(3)
public final class BeanScopeFactoryImpl extends AbstractBeanScopeFactory<String, BeanScope>{
	
	private static BeanScopeFactoryImpl factory;
	
	@SingletonFactory
	public static BeanScopeFactoryImpl getFactory() {
		if(factory==null) {
			factory=new BeanScopeFactoryImpl();
		}
		return factory;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BeanScopeFactoryImpl loadFactory() {
		Container container = getContainer();
		if(container==null) {
			return this;
		}
		getContainer().getCache().forEach((key,group)->{
			ConcurrentHashMap<String, BeanScope> cache = group.getCache();
			this.getCache().putAll(cache);
		});
		return this;
	}

	@Override
	protected BeanScope create(BeanDefinition definition) {
		return new BeanScope();
	}

	
	
}
