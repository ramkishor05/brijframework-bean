package org.brijframework.bean.factories.metadata.annotation;

import org.brijframework.bean.factories.metadata.asm.AbstractBeanMetaDataFactory;
import org.brijframework.bean.factories.resource.annotation.AnnotationBeanResourceFactory;
import org.brijframework.support.config.OrderOn;
import org.brijframework.support.config.SingletonFactory;

@OrderOn(1)
public class AnnotationBeanMetaDataFactory extends AbstractBeanMetaDataFactory{

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
