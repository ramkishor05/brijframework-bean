package org.brijframework.bean.factories.resource;

import org.brijframework.bean.resource.BeanResource;
import org.brijframework.factories.module.ModuleFactory;

public interface BeanResourceFactory<K,T extends BeanResource> extends ModuleFactory<K, T>{

	public  T register(T dataSetup);

	public  T find(String modelKey);
	
	public  boolean validate(T dataSetup) ;

}
