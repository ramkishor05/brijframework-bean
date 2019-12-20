package org.brijframework.bean.resource;

import java.util.Map;

public interface BeanResource {

	String getId();

	String getName();

	String getModel();
	
	String getType();
	
	Map<String, Object> getProperties();

	String getScope();
	
	String getUniqueKey();

	String getFactoryClass();

	String getFactoryMethod();

	BeanResourceConstructor getConstructor();

}
