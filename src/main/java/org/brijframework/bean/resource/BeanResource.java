package org.brijframework.bean.resource;

import java.util.Map;

public interface BeanResource {

	String getId();

	String getName();

	String getModel();
	
	String getTarget();
	
	Map<String, Object> getProperties();

	String getScope();
	
	String getUniqueKey();

	String getFactoryClass();

	String getFactoryMethod();

}
