package org.brijframework.bean;

import java.util.Map;

import org.brijframework.lifecycle.Initializer;
import org.brijframework.meta.info.ClassMetaInfo;
import org.brijframework.support.enums.Scope;

public interface BeanInfo extends Initializer{

	Scope getScope();
	
	String getId();

	String getName();

	ClassMetaInfo getOwner();

	Map<String, Object> getProperties();
}
