package org.brijframework.bean.factories.resource;

import java.util.List;

import org.brijframework.bean.resource.BeanResource;

public interface BeanResourceGroupFactory<K, T extends BeanResource> extends BeanResourceFactory<K,T>{

	@Override
	public T register(T dataSetup);

	@Override
	public T find(String id);
	
	public List<BeanResource> findAllByModel(String model);
	
}
