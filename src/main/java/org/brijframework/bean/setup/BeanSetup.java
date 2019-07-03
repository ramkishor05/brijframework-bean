package org.brijframework.bean.setup;

import java.util.Map;

public interface BeanSetup {

	String getId();

	String getName();

	String getModel();
	
	Map<String, Object> getProperties();

	String getScope();
}
