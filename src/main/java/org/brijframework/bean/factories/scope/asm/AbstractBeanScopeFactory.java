package org.brijframework.bean.factories.scope.asm;

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
import org.brijframework.util.printer.ConsolePrint;
import org.brijframework.util.reflect.InstanceUtil;

public abstract class AbstractBeanScopeFactory extends AbstractFactory<String, BeanScope> implements BeanScopeFactory{

	public BeanScope register(String key,BeanMetaData datainfo) {
		BeanScope dataObject=find(key);
		Assertion.isTrue(dataObject!=null,"Bean already exist in cache with : "+key);
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
	
	@Override
	protected void preregister(String key, BeanScope value) {
	}

	@Override
	protected void postregister(String key, BeanScope value) {
	}

}
