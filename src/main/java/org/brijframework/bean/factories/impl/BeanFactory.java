package org.brijframework.bean.factories.impl;

import java.util.ArrayList;
import java.util.List;

import org.brijframework.bean.info.BeanInfo;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.factories.Factory;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.asserts.Assertion;

public class BeanFactory extends BeanRegistryFactory implements Factory{
 
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
		BeanInfoFactoryImpl.getFactory().getCache().forEach((key,datainfo)->{
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
		BeanInfo datainfo= BeanInfoFactoryImpl.getFactory().getData(model);
		Assertion.notNull(datainfo, "Model data not found");
		return getModel(datainfo);
	}

	public <T> T getModel(Class<?> model) {
		BeanInfo datainfo= BeanInfoFactoryImpl.getFactory().getData(model.getSimpleName());
		Assertion.notNull(datainfo, "Model data not found");
		return getModel(datainfo);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getModel(BeanInfo datainfo) {
		if(datainfo==null) {
			return null;
		}
		String uniqueID=getUniqueID(datainfo);
		BeanScope dataBean= getBeanScope(uniqueID);
		if(dataBean==null) {
			dataBean=register(uniqueID,datainfo);
		}
	    return (T) dataBean.getScopeObject();
	}
	
	public List<?> getModels(Class<?> model) {
		List<Object> list=new ArrayList<>();
		List<BeanInfo> datainfoList= BeanInfoFactoryImpl.getFactory().getBeanInfoList(model.getSimpleName());
		for(BeanInfo datainfo:datainfoList) {
			Object bean=getModel(datainfo);
			if(bean!=null) {
				list.add(bean);
			}
		}
		return list;
	}
}
