package org.brijframework.data.factories;

import org.brijframework.data.DataSetup;
import org.brijframework.factories.Factory;

public interface DataSetupFactory<T extends DataSetup> extends Factory{

	public  T register(T dataSetup);

	public  T getSetup(String modelKey);

}
