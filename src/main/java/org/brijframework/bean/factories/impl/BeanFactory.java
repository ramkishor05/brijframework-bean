package org.brijframework.bean.factories.impl;

import java.util.ArrayList;
import java.util.List;

import org.brijframework.bean.meta.BeanMetaData;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.asserts.Assertion;

public class BeanFactory extends BeanScopeFactory {
 
	protected BeanFactory() {
	}

	protected static BeanFactory factory;

	@Assignable
	public static BeanFactory getFactory() {
		if (factory == null) {
			factory = new BeanFactory();
		}
		return factory;
	}

	@Override
	public BeanFactory loadFactory() {
		BeanMetaDataFactoryImpl.getFactory().getCache().forEach((key,datainfo)->{
			if (Scope.SINGLETON.equals(datainfo.getScope())) {
				String uniqueID=(String) key;
				register(uniqueID,datainfo);
			}
		});
		return this;
	}

	@Override
	public BeanFactory clear() {
		getCache().clear();
		return this;
	}

	public <T> T getModel(String model) {
		BeanMetaData datainfo= BeanMetaDataFactoryImpl.getFactory().find(model);
		Assertion.notNull(datainfo, "Model data not found");
		return getModel(datainfo);
	}

	public <T> T getModel(Class<?> model) {
		BeanMetaData datainfo= BeanMetaDataFactoryImpl.getFactory().find(model.getSimpleName());
		Assertion.notNull(datainfo, "Model data not found");
		return getModel(datainfo);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getModel(BeanMetaData datainfo) {
		if(datainfo==null) {
			return null;
		}
		String uniqueID=getUniqueID(datainfo);
		BeanScope dataBean= find(uniqueID);
		if(dataBean==null) {
			dataBean=register(uniqueID,datainfo);
		}
	    return (T) dataBean.getScopeObject();
	}
	
	public List<?> getModels(Class<?> model) {
		List<Object> list=new ArrayList<>();
		List<BeanMetaData> datainfoList= BeanMetaDataFactoryImpl.getFactory().findAllByModel(model.getSimpleName());
		for(BeanMetaData datainfo:datainfoList) {
			Object bean=getModel(datainfo);
			if(bean!=null) {
				list.add(bean);
			}
		}
		return list;
	}

	@Override
	protected void preregister(String key, BeanScope value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void postregister(String key, BeanScope value) {
		// TODO Auto-generated method stub
		
	}
	
}
