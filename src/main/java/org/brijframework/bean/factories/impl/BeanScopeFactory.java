package org.brijframework.bean.factories.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.bean.meta.BeanMetaData;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.container.Container;
import org.brijframework.factories.impl.AbstractFactory;
import org.brijframework.factories.module.ModuleFactory;
import org.brijframework.group.Group;
import org.brijframework.model.info.PptModelInfoGroup;
import org.brijframework.monitor.factories.PrototypeScopeMonitorFactroy;
import org.brijframework.monitor.factories.RequestScopeMonitorFactroy;
import org.brijframework.monitor.factories.SessionScopeMonitorFactroy;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.printer.ConsolePrint;
import org.brijframework.util.reflect.InstanceUtil;

public abstract class BeanScopeFactory extends AbstractFactory<String, BeanScope> implements ModuleFactory<String, BeanScope>{

	private ConcurrentHashMap<String, BeanScope> cache=new ConcurrentHashMap<String, BeanScope>();
	
	private Container container;

	public BeanScope register(String key,BeanMetaData datainfo) {
		BeanScope dataObject=find(key);
		Assertion.isTrue(dataObject!=null,"Model already exist in cache with : "+key);
		dataObject=new BeanScope(datainfo);
		dataObject.setScopeObject(buildScopeObject(key,datainfo));
		dataObject.setId(key);
		loadContainer(dataObject);
		getCache().put(key, dataObject);
		ConsolePrint.screen("Bean", "Registery for bean scope '"+datainfo.getScope()+"' for id :"+datainfo.getId());
		return dataObject;
	}

	public String getUniqueID(BeanMetaData datainfo) {
		switch (datainfo.getScope()) {
		case SINGLETON:
			return datainfo.getId();
		case SESSION:
			return datainfo.getId()+SessionScopeMonitorFactroy.factory().currentService().getId();
		case REQUEST:
			return datainfo.getId()+RequestScopeMonitorFactroy.factory().currentService().getId();
		case PROTOTYPE:
			return datainfo.getId()+PrototypeScopeMonitorFactroy.factory().currentService().getId();
		default:
			return datainfo.getId();
		}
	}
	

	@SuppressWarnings("rawtypes")
	private Object buildScopeObject(String uniqueID, BeanMetaData datainfo) {
		Object bean=InstanceUtil.getInstance(datainfo.getOwner().getTarget(), datainfo.getOwner().getConstructor().getValues());
		datainfo.getProperties().forEach((_keyPath,_value)->{
			PptModelInfoGroup fieldGroup=datainfo.getOwner().getProperties().get(_keyPath);
			if(_value instanceof Map && ((Map) _value).containsKey("@ref")) {
				_value=getCache().get(((Map) _value).get("@ref"));
			}
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
	public BeanScopeFactory clear() {
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
	

	public BeanMetaData getContainer(String modelKey) {
		if (getContainer() == null) {
			return null;
		}
		return getContainer().find(modelKey);
	}
}
