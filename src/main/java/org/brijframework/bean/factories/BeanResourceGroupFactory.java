package org.brijframework.bean.factories;

import java.util.List;

import org.brijframework.bean.resource.BeanResource;

public interface BeanResourceGroupFactory<T extends BeanResource> extends BeanResourceFactory<T>{

	@Override
	public T register(T dataSetup);

	@Override
	public T find(String id);
	
	public List<BeanResource> findAllByModel(String model);
	
}
