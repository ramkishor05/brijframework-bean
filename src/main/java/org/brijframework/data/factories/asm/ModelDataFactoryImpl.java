package org.brijframework.data.factories.asm;

import org.brijframework.data.ModelDataInfo;
import org.brijframework.meta.reflect.ClassMeta;

public class ModelDataFactoryImpl implements ModelDataFactory{
	
	private static ModelDataFactoryImpl factory;

	public static ModelDataFactoryImpl getFactory() {
		if(factory==null) {
			factory=new ModelDataFactoryImpl();
		}
		return factory;
	}

	public ModelDataInfo getModelInfo(ClassMeta metaSetup) {
		return null;
	}

}
