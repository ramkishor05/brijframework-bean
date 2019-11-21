package org.brijframework.bean.factories;

import java.util.List;

import org.brijframework.bean.meta.BeanMetaData;

public interface BeanMetaDataGroupFactory<T extends BeanMetaData> extends BeanMetaDataFactory<T>{

	List<T> findAllByModel(String model);

}
