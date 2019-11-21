package org.brijframework.bean.meta;

import java.util.Map;

import org.brijframework.lifecycle.Initializer;
import org.brijframework.model.info.OwnerModelInfo;
import org.brijframework.support.enums.Scope;

public interface BeanMetaData extends Initializer{

	Scope getScope();
	
	String getId();

	String getName();

	OwnerModelInfo getOwner();

	Map<String, Object> getProperties();
}
