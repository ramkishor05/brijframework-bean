package org.brijframework.bean.meta;

import java.util.Map;

import org.brijframework.lifecycle.Initializer;
import org.brijframework.model.info.ClassModelMetaData;
import org.brijframework.support.enums.Scope;

public interface BeanMetaData extends Initializer{

	Scope getScope();
	
	String getId();

	String getName();

	ClassModelMetaData getOwner();

	Map<String, Object> getProperties();
}
