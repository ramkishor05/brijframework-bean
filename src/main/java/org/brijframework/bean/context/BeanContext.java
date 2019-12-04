package org.brijframework.bean.context;

import java.util.List;

import org.brijframework.bean.meta.BeanMetaData;
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

	public List<String> getRegisteredBeanNames();

	public List<String> getRegisteredBeanNames(Class<?> beanClass);
	
	public List<?> getBeanResourceNames();
	
	public List<?> getBeanResourceNames(String model);
	
	public BeanResource getBeanResource(String name);
	
	public List<? extends BeanResource> getBeanResourceList();
	
	public List<? extends BeanResource> getBeanResourceList(String model);
	
	public List<?> getBeanMetaDataNames();
	
	public List<?> getBeanMetaDataNames(String model);
	
	public BeanMetaData getBeanMetaData(String name);
	
	public List<? extends BeanMetaData> getBeanMetaDataList();
	
	public List<? extends BeanMetaData> getBeanMetaDataList(String model);

	public List<? extends BeanMetaData> getBeanMetaDataList(Class<?> metaClass);
	
}
