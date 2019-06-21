package org.brijframework.bean;

import java.util.Map;

public interface BeanSetup {

	String getId();

	String getName();

	String getModel();
	
	Map<String, Object> getProperties();
}
