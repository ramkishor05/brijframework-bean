package org.brijframework.bean.factories.scope.impl;

import org.brijframework.bean.factories.scope.asm.AbstractBeanScopeFactory;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.factories.Factory;
import org.brijframework.support.config.OrderOn;
import org.brijframework.support.config.SingletonFactory;

@OrderOn(3)
public final class BeanScopeFactoryImpl extends AbstractBeanScopeFactory{
	
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
	public Factory<String, BeanScope> loadFactory() {
		getContainer().getCache().forEach((key,group)->{
			this.getCache().putAll(group.getCache());
		});
		return this;
	}
	
}
