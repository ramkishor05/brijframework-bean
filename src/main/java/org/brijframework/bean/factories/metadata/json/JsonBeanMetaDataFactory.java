package org.brijframework.bean.factories.metadata.json;

import org.brijframework.bean.factories.metadata.asm.AbstractBeanMetaDataFactory;
import org.brijframework.bean.factories.resource.json.JsonBeanResourceFactory;
import org.brijframework.support.config.OrderOn;
import org.brijframework.support.config.SingletonFactory;

@OrderOn(2)
public class JsonBeanMetaDataFactory extends AbstractBeanMetaDataFactory{

	private static JsonBeanMetaDataFactory factory;

	@SingletonFactory
	public static JsonBeanMetaDataFactory getFactory() {
		if(factory==null) {
			factory=new JsonBeanMetaDataFactory();
		}
		return factory;
	}
	
	@Override
	public JsonBeanMetaDataFactory loadFactory() {
		JsonBeanResourceFactory.getFactory().getCache().forEach((id,beansetup)->{
			register(id, beansetup);
		});
		return this;
	}

}
