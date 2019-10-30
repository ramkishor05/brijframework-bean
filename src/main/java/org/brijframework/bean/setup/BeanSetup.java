package org.brijframework.bean.setup;

import java.util.Map;

public interface BeanSetup {

	String getId();

	String getName();

	String getModel();
	
	String getTarget();
	
	Map<String, Object> getProperties();

	String getScope();
	
	String getUniqueKey();
}
