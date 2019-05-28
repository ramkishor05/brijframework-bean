package org.brijframework.data.factories.asm;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.data.asm.ObjectData;
import org.brijframework.data.factories.DataFactory;
import org.brijframework.factories.Factory;
import org.brijframework.meta.reflect.ClassMeta;
import org.brijframework.util.asserts.Assertion;

public class DataFactoryImpl implements DataFactory{
	
	private static DataFactoryImpl factory;
	private Container container;
	private ConcurrentHashMap<String, ObjectData> cache=new ConcurrentHashMap<>();

	public static DataFactoryImpl getFactory() {
		if(factory==null) {
			factory=new DataFactoryImpl();
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
		return data;
	}

	@Override
	public Factory loadFactory() {
		return null;
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
	public Factory clear() {
		getCache().clear();
		return this;
	}
}
