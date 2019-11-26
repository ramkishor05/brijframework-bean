package org.brijframework.bean.factories.metadata;

import java.util.List;

import org.brijframework.bean.factories.metadata.BeanMetaDataFactory;
import org.brijframework.bean.meta.BeanMetaData;

public interface BeanMetaDataGroupFactory<K,T> extends BeanMetaDataFactory<K,T>{

	List<BeanMetaData> findAllByModel(String model);

}
