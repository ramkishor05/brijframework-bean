package org.brijframework.bean.factories;

import java.util.List;

import org.brijframework.bean.scope.BeanScope;
import org.brijframework.factories.module.ModuleFactory;

public interface BeanScopeFactory extends ModuleFactory<String, BeanScope>{

	BeanScope find(Class<? extends Object> beanClass);

	List<BeanScope> findAll(Class<? extends Object> beanClass);
	
	boolean contains(String key);

}
