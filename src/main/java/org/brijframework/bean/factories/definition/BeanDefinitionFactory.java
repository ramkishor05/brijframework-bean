package org.brijframework.bean.factories.definition;

import java.util.List;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.factories.module.ModuleFactory;

public interface BeanDefinitionFactory<K,T> extends ModuleFactory<K,T>{

	List<BeanDefinition> findAll(Class<?> cls);
	
	List<BeanDefinition> findAllByModel(String model);

}
