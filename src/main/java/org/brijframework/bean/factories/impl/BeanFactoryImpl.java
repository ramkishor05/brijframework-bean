package org.brijframework.bean.factories.impl;

import org.brijframework.bean.factories.asm.AbstractBeanFactory;
import org.brijframework.factories.Factory;
import org.brijframework.support.config.SingletonFactory;

public class BeanFactoryImpl extends AbstractBeanFactory{
 
	protected BeanFactoryImpl() {
	}

	protected static BeanFactoryImpl factory;

	@SingletonFactory
	public static BeanFactoryImpl getFactory() {
		if (factory == null) {
			factory = new BeanFactoryImpl();
		}
		return factory;
	}

	@Override
	public Factory<String, Object> loadFactory() {
		return this;
	}
	
}
