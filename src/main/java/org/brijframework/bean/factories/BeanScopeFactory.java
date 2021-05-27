package org.brijframework.bean.factories;

import java.util.List;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.factories.module.ModuleFactory;

public interface BeanScopeFactory<K, T> extends ModuleFactory<K, T>{
	
	T find(K key);

	T find(Class<? extends Object> beanClass);

	List<T> findAll(Class<? extends Object> beanClass);
	
	boolean contains(K key);

	T getBeanScopeForObject(Object object);
	
	Object getBeanObject(String object);

	BeanDefinition getBeanDefinitionOfObject(Object current);
}
