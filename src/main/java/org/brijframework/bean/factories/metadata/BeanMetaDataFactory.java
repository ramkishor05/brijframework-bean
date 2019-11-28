package org.brijframework.bean.factories.metadata;

import java.util.List;

import org.brijframework.bean.meta.BeanMetaData;
import org.brijframework.factories.module.ModuleFactory;

public interface BeanMetaDataFactory<K,T> extends ModuleFactory<K,T>{

	List<BeanMetaData> findAll(Class<?> cls);
	
	List<BeanMetaData> findAllByModel(String model);

}
