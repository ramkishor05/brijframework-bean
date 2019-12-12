package org.brijframework.bean.context;

import java.util.List;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.resource.BeanResource;
import org.brijframework.context.module.ModuleContext;
import org.brijframework.model.context.ModelContext;
import org.brijframework.support.config.DepandOn;
import org.brijframework.support.enums.Scope;

@DepandOn(depand = ModelContext.class)
public interface BeanContext extends ModuleContext {

	/*
	 * BeanObject
	 */
	
	public <T> T getBean(String name);

	public <T> T getBean(Class<? extends Object> beanClass);

	public <T> T getBean(String name, Class<T> beanClass);

	public List<?> getBeanList(Class<? extends Object> beanClass);
	
	public List<String> getBeanNameList();

	public List<String> getBeanNameList(Class<?> beanClass);
	
	public List<?> getBeanList(Scope scope);
	
	/*
	 * BeanResource
	 */
	public List<?> getBeanResourceNameList();
	
	public List<?> getBeanResourceNamesList(String model);
	
	public BeanResource getBeanResource(String name);
	
	public List<? extends BeanResource> getBeanResourceList();
	
	public List<? extends BeanResource> getBeanResourceList(String model);
	
	public List<? extends BeanResource> getBeanResourceList(Scope scope);
	
	/*
	 * BeanDefinition
	 */
	public List<?> getBeanDefinitionNameList();
	
	public List<?> getBeanDefinitionNameList(String model);
	
	public BeanDefinition getBeanDefinition(String name);
	
	public List<? extends BeanDefinition> getBeanDefinitionList();
	
	public List<? extends BeanDefinition> getBeanDefinitionList(String model);

	public List<? extends BeanDefinition> getBeanDefinitionList(Class<?> metaClass);
	
	public List<? extends BeanDefinition> getBeanDefinitionList(Scope scope);
	
}
