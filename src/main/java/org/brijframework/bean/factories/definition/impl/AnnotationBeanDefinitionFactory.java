package org.brijframework.bean.factories.definition.impl;

import org.brijframework.bean.factories.definition.asm.AbstractBeanDefinitionFactory;
import org.brijframework.bean.factories.resource.annotation.AnnotationBeanResourceFactory;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.support.ordering.OrderOn;

@OrderOn(1)
public class AnnotationBeanDefinitionFactory extends AbstractBeanDefinitionFactory{

	private static AnnotationBeanDefinitionFactory factory;

	@SingletonFactory
	public static AnnotationBeanDefinitionFactory getFactory() {
		if(factory==null) {
			factory=new AnnotationBeanDefinitionFactory();
		}
		return factory;
	}
	
	@Override
	public AnnotationBeanDefinitionFactory loadFactory() {
		 AnnotationBeanResourceFactory.getFactory().getCache().forEach((id,resource)->{
			 register(id, resource);
		 });
		return this;
	}

}
