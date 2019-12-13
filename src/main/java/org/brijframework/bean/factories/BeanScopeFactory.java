package org.brijframework.bean.factories;

import java.util.List;

import org.brijframework.factories.module.ModuleFactory;

public interface BeanScopeFactory<K, T> extends ModuleFactory<K, T>{

	T find(Class<? extends Object> beanClass);

	List<T> findAll(Class<? extends Object> beanClass);
	
	boolean contains(K key);

}
