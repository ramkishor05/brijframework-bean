package org.brijframework.bean.factories.scope.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.brijframework.bean.factories.scope.BeanScopeFactory;
import org.brijframework.bean.meta.BeanMetaData;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.factories.impl.AbstractFactory;
import org.brijframework.group.Group;
import org.brijframework.model.info.PropertyModelMetaDataGroup;
import org.brijframework.monitor.factories.PrototypeScopeMonitorFactroy;
import org.brijframework.monitor.factories.RequestScopeMonitorFactroy;
import org.brijframework.monitor.factories.SessionScopeMonitorFactroy;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.InstanceUtil;

public abstract class AbstractBeanScopeFactory extends AbstractFactory<String, BeanScope> implements BeanScopeFactory{

	@Override
	public boolean contains(String key) {
		return find(key)!=null;
	}
	
	public BeanScope register(String key,BeanMetaData datainfo) {
		BeanScope dataObject=find(key);
		Assertion.isTrue(dataObject!=null,"Bean already exist in cache with : "+key);
		dataObject=new BeanScope(datainfo);
		dataObject.setScopeObject(buildScopeObject(key,datainfo));
		dataObject.setId(key);
		preregister(key, dataObject);
		loadContainer(key,dataObject);
		getCache().put(key, dataObject);
		postregister(key, dataObject);
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
		Assertion.isTrue(datainfo==null,"BeanMetaData not found in cache with : "+uniqueID);
		Assertion.isTrue(datainfo.getOwner()==null,"BeanMetaData Owner not found in cache with : "+uniqueID);
		Object bean=InstanceUtil.getInstance(datainfo.getOwner().getTarget(), datainfo.getOwner().getConstructor().getValues());
		datainfo.getProperties().forEach((_keyPath,_value)->{
			PropertyModelMetaDataGroup fieldGroup=datainfo.getOwner().getProperties().get(_keyPath);
			if(_value instanceof Map && ((Map) _value).containsKey("@ref")) {
				_value=getCache().get(((Map) _value).get("@ref"));
			}
			Assertion.notNull(fieldGroup, datainfo.getOwner().getName()+" : No such type of property contains : "+_keyPath+" for "+datainfo.getId());
			Assertion.notNull(fieldGroup.getSetterMeta(), datainfo.getOwner().getName()+" : Not allowed to set such type of property contains : "+_keyPath);
			PropertyAccessorUtil.setProperty(bean, fieldGroup.getSetterMeta().getTarget(), _value);
		});
		return bean;
	}
	
	@Override
	protected void loadContainer(String key, BeanScope value) {
		if (getContainer() == null) {
			return;
		}
		Group group = getContainer().load(value.getDatainfo().getOwner().getName());
		if(!group.containsKey(value.getId())) {
			group.add(key, value);
		}else {
			group.update(key, value);
		}
	}
	
	@Override
	public BeanScope find(Class<? extends Object> beanClass) {
		List<BeanScope> findAll = findAll(beanClass);
		if(findAll.isEmpty()) {
			return null;
		}
		Assertion.isTrue(findAll.size()>1, beanClass.getSimpleName()+" nonunique bean exist.");
		return findAll.get(0);
	}

	@Override
	public List<BeanScope> findAll(Class<? extends Object> beanClass) {
		List<BeanScope> list=new ArrayList<BeanScope>();
		for(BeanScope beanScope:getCache().values()) {
			if(beanClass.isAssignableFrom(beanScope.getDatainfo().getOwner().getTarget())) {
				list.add(beanScope);
			}
		}
		return list;
	}
	
	@Override
	protected void preregister(String key, BeanScope value) {
	}

	@Override
	protected void postregister(String key, BeanScope value) {
	}

}
