package org.brijframework.data.factories;

import org.brijframework.data.asm.ObjectData;
import org.brijframework.data.setup.ClassDataSetup;
import org.brijframework.factories.Factory;
import org.brijframework.meta.reflect.ClassMeta;

public interface DataFactory extends Factory{

	ObjectData register(String key, ClassMeta owner, ClassDataSetup datainfo);

	ObjectData getData(String modelKey);

}
