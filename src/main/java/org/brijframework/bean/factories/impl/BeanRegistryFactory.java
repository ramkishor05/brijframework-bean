package org.brijframework.bean.factories.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.bean.info.BeanInfo;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.container.Container;
import org.brijframework.factories.Factory;
import org.brijframework.group.Group;
import org.brijframework.model.info.PptModelInfoGroup;
import org.brijframework.monitor.factories.PrototypeFactroy;
import org.brijframework.monitor.factories.RequestFactroy;
import org.brijframework.monitor.factories.SessionFactroy;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.formatter.PrintUtil;
import org.brijframework.util.reflect.InstanceUtil;

public abstract class BeanRegistryFactory implements Factory{

	private ConcurrentHashMap<String, BeanScope> cache=new ConcurrentHashMap<String, BeanScope>();
	
	private Container container;

	public BeanScope register(String key,BeanInfo datainfo) {
		BeanScope dataObject=getBeanScope(key);
		Assertion.isTrue(dataObject!=null,"Model already exist in cache with : "+key);
		dataObject=new BeanScope(datainfo);
		dataObject.setScopeObject(buildScopeObject(key,datainfo));
		dataObject.setId(key);
		getCache().put(key, dataObject);
		System.err.println("Bean Scope    : "+key);
		loadContainer(dataObject);
		System.out.println(PrintUtil.getObjectInfo(dataObject));
		return dataObject;
	}

	protected BeanScope getBeanScope(String key) {
		return getCache().get(key);
	}

	public String getUniqueID(BeanInfo datainfo) {
		switch (datainfo.getScope()) {
		case SINGLETON:
			return datainfo.getId();
		case SESSION:
			return datainfo.getId()+SessionFactroy.factory().currentService().getId();
		case REQUEST:
			return datainfo.getId()+RequestFactroy.factory().currentService().getId();
		case PROTOTYPE:
			return datainfo.getId()+PrototypeFactroy.factory().currentService().getId();
		default:
			return datainfo.getId();
		}
	}
	

	private Object buildScopeObject(String uniqueID, BeanInfo datainfo) {
		Object bean=InstanceUtil.getInstance(datainfo.getOwner().getTarget(), datainfo.getOwner().getConstructor().getValues());
		datainfo.getProperties().forEach((_keyPath,_value)->{
			PptModelInfoGroup fieldGroup=datainfo.getOwner().getProperties().get(_keyPath);
			Assertion.notNull(fieldGroup, datainfo.getOwner().getName()+" : No such type of property contains : "+_keyPath);
			Assertion.notNull(fieldGroup.getSetterMeta(), datainfo.getOwner().getName()+" : Not attow to set such type of property contains : "+_keyPath);
			PropertyAccessorUtil.setProperty(bean, fieldGroup.getSetterMeta().getTarget(), _value);
		});
		return bean;
	}

	@Override
	public Container getContainer() {
		return this.container;
	}

	@Override
	public void setContainer(Container container) {
		this.container=container;
	}

	@Override
	public ConcurrentHashMap<String, BeanScope> getCache() {
		return cache;
	}

	@Override
	public BeanRegistryFactory clear() {
		this.getCache().clear();
		return this;
	}
	
	public void loadContainer(BeanScope metaInfo) {
		if (getContainer() == null) {
			return;
		}
		Group group = getContainer().load(metaInfo.getDatainfo().getOwner().getName());
		if(!group.containsKey(metaInfo.getId())) {
			group.add(metaInfo.getId(), metaInfo);
		}else {
			group.update(metaInfo.getId(), metaInfo);
		}
	}
	

	public BeanInfo getContainer(String modelKey) {
		if (getContainer() == null) {
			return null;
		}
		return getContainer().find(modelKey);
	}
}
