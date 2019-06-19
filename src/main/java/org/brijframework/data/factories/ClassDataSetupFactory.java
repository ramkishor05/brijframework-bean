package org.brijframework.data.factories;

import java.util.List;

import org.brijframework.data.setup.ClassDataSetup;

public interface ClassDataSetupFactory<T extends ClassDataSetup> extends DataSetupFactory<T>{

	@Override
	public T register(T dataSetup);

	@Override
	public T getSetup(String id);
	

	public List<ClassDataSetup> getSetupList(String model);
	
}
