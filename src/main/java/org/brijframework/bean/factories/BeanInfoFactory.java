package org.brijframework.bean.factories;

import org.brijframework.bean.BeanInfo;
import org.brijframework.factories.Factory;

public interface BeanInfoFactory< T extends BeanInfo> extends Factory{

	public  T register(T datainfo);

	public  T getData(String modelKey);

}
