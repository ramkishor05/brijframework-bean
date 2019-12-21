package org.brijframework.bean.resource;

import java.util.List;

public interface BeanResourceConstructor {

	String getId();

	String getName();

	String getModel();
	
	List<BeanResourceParam> getParameters();
}
