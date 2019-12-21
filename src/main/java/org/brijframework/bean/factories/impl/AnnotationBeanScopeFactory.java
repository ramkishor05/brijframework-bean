package org.brijframework.bean.factories.impl;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.factories.asm.AbstractBeanScopeFactory;
import org.brijframework.bean.factories.definition.impl.AnnotationBeanDefinitionFactory;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.factories.Factory;
import org.brijframework.support.enums.Scope;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.support.ordering.OrderOn;

@OrderOn(1)
public final class AnnotationBeanScopeFactory extends AbstractBeanScopeFactory<String, BeanScope>{
	
	private static AnnotationBeanScopeFactory factory;
	
	@SingletonFactory
	public static AnnotationBeanScopeFactory getFactory() {
		if(factory==null) {
			factory=new AnnotationBeanScopeFactory();
		}
		return factory;
	}
	
	@Override
	public Factory<String, BeanScope> loadFactory() {
		AnnotationBeanDefinitionFactory.getFactory().getCache().forEach((key,datainfo)->{
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
		return new BeanScope();
	}
}
