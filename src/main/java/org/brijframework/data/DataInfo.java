package org.brijframework.data;

import org.brijframework.lifecycle.Initializer;
import org.brijframework.support.enums.Scope;

public interface DataInfo extends Initializer{

	Scope getScope();
	
	String getId();

	String getName();
}
