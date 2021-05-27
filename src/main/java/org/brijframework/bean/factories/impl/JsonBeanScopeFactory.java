package org.brijframework.bean.factories.impl;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.factories.asm.AbstractBeanScopeFactory;
import org.brijframework.bean.factories.definition.impl.JsonBeanDefinitionFactory;
import org.brijframework.bean.scope.BeanScope;
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
	public JsonBeanScopeFactory loadFactory() {
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
	protected BeanScope createBeanScope(BeanDefinition definition) {
		BeanScope beanScope = new BeanScope();
		beanScope.setBeanDefinition(definition);
		return beanScope;
	}
}
