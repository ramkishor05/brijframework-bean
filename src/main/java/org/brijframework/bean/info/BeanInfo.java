package org.brijframework.bean.info;

import java.util.Map;

import org.brijframework.lifecycle.Initializer;
import org.brijframework.model.info.OwnerModelInfo;
import org.brijframework.support.enums.Scope;

public interface BeanInfo extends Initializer{

	Scope getScope();
	
	String getId();

	String getName();

	OwnerModelInfo getOwner();

	Map<String, Object> getProperties();
}
