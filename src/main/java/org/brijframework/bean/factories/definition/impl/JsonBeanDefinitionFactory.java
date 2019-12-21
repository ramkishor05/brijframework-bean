package org.brijframework.bean.factories.definition.impl;

import org.brijframework.bean.factories.definition.asm.AbstractBeanDefinitionFactory;
import org.brijframework.bean.factories.resource.json.JsonBeanResourceFactory;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.support.ordering.OrderOn;

@OrderOn(2)
public class JsonBeanDefinitionFactory extends AbstractBeanDefinitionFactory{

	private static JsonBeanDefinitionFactory factory;

	@SingletonFactory
	public static JsonBeanDefinitionFactory getFactory() {
		if(factory==null) {
			factory=new JsonBeanDefinitionFactory();
		}
		return factory;
	}
	
	@Override
	public JsonBeanDefinitionFactory loadFactory() {
		JsonBeanResourceFactory.getFactory().getCache().forEach((id,beansetup)->{
			register(id, beansetup);
		});
		return this;
	}

}
