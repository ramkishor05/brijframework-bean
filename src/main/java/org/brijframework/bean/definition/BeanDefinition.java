package org.brijframework.bean.definition;

import java.util.Map;

import org.brijframework.lifecycle.Initializer;
import org.brijframework.model.diffination.ModelTypeDeffination;
import org.brijframework.support.enums.Scope;

public interface BeanDefinition extends Initializer{

	Scope getScope();
	
	String getId();

	String getName();

	ModelTypeDeffination getOwner();
	
	BeanDefinationConstructor getConstructor();

	Map<String, Object> getProperties();

	String getFactoryClass();

	String getFactoryMethod();
}
