package org.brijframework.bean.factories.definition.annotation;

import org.brijframework.bean.factories.definition.asm.AbstractBeanDefinitionFactory;
import org.brijframework.bean.factories.resource.annotation.AnnotationBeanResourceFactory;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.support.ordering.OrderOn;

@OrderOn(1)
public class AnnotationBeanMetaDataFactory extends AbstractBeanDefinitionFactory{

	private static AnnotationBeanMetaDataFactory factory;

	@SingletonFactory
	public static AnnotationBeanMetaDataFactory getFactory() {
		if(factory==null) {
			factory=new AnnotationBeanMetaDataFactory();
		}
		return factory;
	}
	
	@Override
	public AnnotationBeanMetaDataFactory loadFactory() {
		 AnnotationBeanResourceFactory.getFactory().getCache().forEach((id,resource)->{
			 register(id, resource);
		 });
		return this;
	}

}
