package org.brijframework.bean.factories.resource;

import java.util.List;

import org.brijframework.bean.resource.BeanResource;
import org.brijframework.factories.module.ModuleFactory;

public interface BeanResourceFactory<K,T extends BeanResource> extends ModuleFactory<K, T>{
	
	public List<T> findAllByModel(String model);

	public  boolean contains(T dataSetup);

}
