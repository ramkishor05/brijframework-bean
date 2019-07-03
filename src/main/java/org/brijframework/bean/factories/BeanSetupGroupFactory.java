package org.brijframework.bean.factories;

import java.util.List;

import org.brijframework.bean.setup.BeanSetup;

public interface BeanSetupGroupFactory<T extends BeanSetup> extends BeanSetupFactory<T>{

	@Override
	public T register(T dataSetup);

	@Override
	public T getBeanSetup(String id);
	
	public List<BeanSetup> getBeanSetupList(String model);
	
}
