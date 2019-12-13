package org.brijframework.bean.factories.json;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.factories.asm.AbstractBeanScopeFactory;
import org.brijframework.bean.factories.definition.json.JsonBeanDefinitionFactory;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.factories.Factory;
import org.brijframework.support.enums.Scope;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.support.ordering.OrderOn;

@OrderOn(2)
public final class JsonBeanScopeFactory extends AbstractBeanScopeFactory<String, BeanScope>{
	
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
		JsonBeanDefinitionFactory.getFactory().getCache().forEach((key,datainfo)->{
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

	@Override
	protected BeanScope create(BeanDefinition definition) {
		return new BeanScope();
	}
}
