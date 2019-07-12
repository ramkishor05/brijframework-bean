package org.brijframework.bean.factories;

import org.brijframework.bean.setup.BeanSetup;
import org.brijframework.factories.Factory;

public interface BeanSetupFactory<T extends BeanSetup> extends Factory{

	public  T register(T dataSetup);

	public  T getBeanSetup(String modelKey);
	
	public  boolean validate(T dataSetup) ;

}
