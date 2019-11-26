package org.brijframework.bean.factories.metadata.impl;

import org.brijframework.bean.factories.metadata.asm.AbstractBeanMetaDataFactory;
import org.brijframework.bean.factories.resource.impl.BeanResourceFactoryImpl;
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

	@Override
	public BeanMetaDataFactoryImpl loadFactory() {
		BeanResourceFactoryImpl.getFactory().getCache().forEach((id,beansetup)->{
			register(id, beansetup);
		});
		return this;
	}

}
