package org.brijframework.bean.context;

import java.util.List;

import org.brijframework.bean.resource.BeanResource;
import org.brijframework.context.module.ModuleContext;
import org.brijframework.model.context.ModelContext;
import org.brijframework.support.config.DepandOn;

@DepandOn(depand = ModelContext.class)
public interface BeanContext extends ModuleContext {

	public <T> T getBeanObject(String name);

	public <T> T getBeanObject(Class<? extends Object> beanClass);

	public <T> T getBeanObject(String name, Class<?> beanClass);

	public List<?> getBeanObjects(Class<? extends Object> beanClass);

	public List<String> getBeanObjectNames();

	public List<String> getBeanObjectNames(Class<?> beanClass);
	
	public BeanResource getBeanResource(String name);
	
	public BeanResource getBeanResource(Class<? extends Object> beanClass);
	
	public BeanResource getBeanResource(String name, Class<?> beanClass);
	
	public List<? extends BeanResource> getBeanResources(Class<? extends Object> beanClass);
}
