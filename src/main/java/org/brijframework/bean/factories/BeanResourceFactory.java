package org.brijframework.bean.factories;

import org.brijframework.bean.resource.BeanResource;
import org.brijframework.factories.Factory;

public interface BeanResourceFactory<T extends BeanResource> extends Factory{

	public  T register(T dataSetup);

	public  T find(String modelKey);
	
	public  boolean validate(T dataSetup) ;

}
