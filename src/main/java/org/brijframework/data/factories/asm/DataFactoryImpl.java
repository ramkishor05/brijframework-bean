package org.brijframework.data.factories.asm;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.data.asm.ObjectData;
import org.brijframework.data.factories.DataFactory;
import org.brijframework.data.setup.ClassDataSetup;
import org.brijframework.group.Group;
import org.brijframework.meta.reflect.ClassMeta;
import org.brijframework.monitor.factories.PrototypeFactroy;
import org.brijframework.monitor.factories.RequestFactroy;
import org.brijframework.monitor.factories.SessionFactroy;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.InstanceUtil;

public class DataFactoryImpl implements DataFactory{

	private static DataFactoryImpl factory;
	private ConcurrentHashMap<String, ObjectData> cache=new ConcurrentHashMap<String, ObjectData>();
	private Container container;

	@Assignable
	public static DataFactoryImpl getFactory() {
		if(factory==null) {
			factory=new DataFactoryImpl();
		}
		return factory;
	}

	@Override
	public ObjectData register(String key,ClassMeta owner,ClassDataSetup datainfo) {
		ObjectData dataObject=getData(key);
		Assertion.isTrue(dataObject!=null,"Data already exist in cache with : "+key);
		dataObject=new ObjectData(owner);
		dataObject.setScopeObject(buildScopeObject(key,owner,datainfo));
		dataObject.setId(key);
		getCache().put(key, dataObject);
		System.err.println("Data Info    : "+key);
		loadContainer(dataObject);
		return dataObject;
	}

	@Override
	public ObjectData getData(String modelKey) {
		if(getCache().containsKey(modelKey)) {
			return getCache().get(modelKey);
		}
		return getContainer(modelKey);
	}


	public String getUniqueID(ClassMeta metaSetup) {
		switch (metaSetup.getScope()) {
		case SINGLETON:
			return metaSetup.getId();
		case SESSION:
			return metaSetup.getId()+SessionFactroy.factory().currentService().getId();
		case REQUEST:
			return metaSetup.getId()+RequestFactroy.factory().currentService().getId();
		case PROTOTYPE:
			return metaSetup.getId()+PrototypeFactroy.factory().currentService().getId();
		default:
			return metaSetup.getId();
		}
	}
	

	private Object buildScopeObject(String uniqueID, ClassMeta owner,ClassDataSetup datainfo) {
		Object bean=InstanceUtil.getInstance(owner.getTarget(), owner.getConstructor().getValues());
		datainfo.getProperties().forEach((key,value)->{
			PropertyAccessorUtil.setProperty(bean, key, value);
		});
		return bean;
	}

	@Override
	public DataFactoryImpl loadFactory() {
		
		return this;
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
	public ConcurrentHashMap<String, ObjectData> getCache() {
		return cache;
	}

	@Override
	public DataFactoryImpl clear() {
		this.getCache().clear();
		return this;
	}
	
	public void loadContainer(ObjectData metaInfo) {
		if (getContainer() == null) {
			return;
		}
		Group group = getContainer().load(metaInfo.getOwner().getName());
		if(!group.containsKey(metaInfo.getId())) {
			group.add(metaInfo.getId(), metaInfo);
		}else {
			group.update(metaInfo.getId(), metaInfo);
		}
	}
	

	public ObjectData getContainer(String modelKey) {
		if (getContainer() == null) {
			return null;
		}
		return getContainer().find(modelKey);
	}
}
