package org.brijframework.bean.factories;

import java.util.List;

import org.brijframework.bean.info.BeanInfo;

public interface BeanInfoGroupFactory<T extends BeanInfo> extends BeanInfoFactory<T>{

	List<T> getBeanInfoList(String model);

	T register(BeanInfo dataSetup);

}
