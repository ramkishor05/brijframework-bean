package org.brijframework.data.factories;

import org.brijframework.data.setup.ClassDataSetup;
import org.brijframework.factories.Factory;

public interface ClassDataFactory extends Factory{

	public ClassDataSetup register(ClassDataSetup dataSetup );

	public ClassDataSetup getDataSetup(String id);
}
