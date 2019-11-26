package org.brijframework.bean.factories.scope.json;

import org.brijframework.bean.factories.metadata.impl.BeanMetaDataFactoryImpl;
import org.brijframework.bean.factories.scope.asm.AbstractBeanScopeFactory;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.factories.Factory;
import org.brijframework.support.config.SingletonFactory;
import org.brijframework.support.config.OrderOn;
import org.brijframework.support.enums.Scope;

@OrderOn(3)
public final class JsonBeanScopeFactory extends AbstractBeanScopeFactory{
	
	private static JsonBeanScopeFactory factory;
	
	@SingletonFactory
	public static JsonBeanScopeFactory getFactory() {
		if(factory==null) {
			factory=new JsonBeanScopeFactory();
		}
		return factory;
	}
	
	@Override
	public Factory<String, BeanScope> loadFactory() {
		BeanMetaDataFactoryImpl.getFactory().getCache().forEach((key,datainfo)->{
			if (Scope.SINGLETON.equals(datainfo.getScope())) {
				String uniqueID=(String) key;
				register(uniqueID,datainfo);
			}
		});
		return this;
	}
}
