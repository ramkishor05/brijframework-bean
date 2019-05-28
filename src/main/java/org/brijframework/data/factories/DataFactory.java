package org.brijframework.data.factories;

import org.brijframework.data.asm.ObjectData;
import org.brijframework.factories.Factory;
import org.brijframework.meta.reflect.ClassMeta;

public interface DataFactory extends Factory{

	public ObjectData register(ClassMeta classMeta);

	public ObjectData getData(String id);

	public ObjectData register(String id, ClassMeta classMeta);
}
