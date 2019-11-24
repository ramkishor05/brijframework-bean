package org.brijframework.bean.factories;

import java.util.List;

import org.brijframework.bean.meta.BeanMetaData;

public interface BeanMetaDataGroupFactory<K,T> extends BeanMetaDataFactory<K,T>{

	List<BeanMetaData> findAllByModel(String model);

}
