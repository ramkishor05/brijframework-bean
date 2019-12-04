package org.brijframework.bean.factories.scope.json;

import org.brijframework.bean.factories.metadata.json.JsonBeanMetaDataFactory;
import org.brijframework.bean.factories.scope.asm.AbstractBeanScopeFactory;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.factories.Factory;
import org.brijframework.support.config.OrderOn;
import org.brijframework.support.config.SingletonFactory;
import org.brijframework.support.enums.Scope;

@OrderOn(2)
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
		JsonBeanMetaDataFactory.getFactory().getCache().forEach((key,datainfo)->{
			try {
			String uniqueID=(String) key;
			if (Scope.SINGLETON.equals(datainfo.getScope())) {
				register(uniqueID,datainfo);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
		});
		return this;
	}
}
