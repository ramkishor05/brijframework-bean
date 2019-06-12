package org.brijframework.data.factories.asm;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.data.asm.ObjectData;
import org.brijframework.data.factories.DataFactory;
import org.brijframework.meta.KeyInfo;
import org.brijframework.meta.factories.asm.MetaFactoryImpl;
import org.brijframework.meta.reflect.ClassMeta;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.asserts.Assertion;

public class DataFactoryImpl implements DataFactory{
	
	private static DataFactoryImpl factory;
	private Container container;
	private ConcurrentHashMap<String, ObjectData> cache=new ConcurrentHashMap<>();

	public static DataFactoryImpl getFactory() {
		if(factory==null) {
			factory=new DataFactoryImpl();
			factory.loadFactory();
		}
		return factory;
	}

	@Override
	public ObjectData getData(String key) {
		return getCache().get(key);
	}
	
	@Override
	public ObjectData register(ClassMeta classMeta) {
		return register(classMeta.getId(), classMeta);
	}
	
	@Override
	public ObjectData register(String key,ClassMeta owner) {
		ObjectData data=getData(key);
		Assertion.isTrue(data!=null,"Data already exist in cache with : "+key);
		data=new ObjectData(owner);
		data.setId(key);
		getCache().put(key, data);
		System.err.println("Data Info    : "+key);
		return data;
	}

	@Override
	public DataFactoryImpl loadFactory() {
		ConcurrentHashMap<KeyInfo, ClassMeta> resources=MetaFactoryImpl.getFactory().getCache();
		for(Entry<KeyInfo, ClassMeta> resource:resources.entrySet()) {
			if(Scope.SINGLETON.equals(resource.getValue().getScope())) {
				register(resource.getValue());
			}
		}
		return this;
	}

	@Override
	public Container getContainer() {
		return container;
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
		getCache().clear();
		return this;
	}
}
