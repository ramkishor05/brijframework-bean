package org.brijframework.bean.factories;

import java.util.List;

import org.brijframework.factories.module.ModuleFactory;

public interface BeanFactory extends ModuleFactory<String,Object>{

	public  <T>T getBean(String name);
	
	public  <T>T getBean(Class<? extends Object> beanClass);
	
	public  List<?> getBeans(Class<? extends Object> beanClass);
	
	public  List<String> getBeanNames();
	
	public  List<String> getBeanNames(Class<?> beanClass);
	
}
