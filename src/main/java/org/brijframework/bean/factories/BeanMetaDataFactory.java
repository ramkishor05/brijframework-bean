package org.brijframework.bean.factories;

import org.brijframework.bean.meta.BeanMetaData;
import org.brijframework.factories.Factory;

public interface BeanMetaDataFactory< T extends BeanMetaData> extends Factory{

	public  T register(T datainfo);

	public  T find(String modelKey);

}
