package org.brijframework.data.setup;

import java.util.Map;

import org.brijframework.support.enums.Scope;

public interface ClassDataSetup extends DataSetup{

	Map<String, Object> getProperties();

	Scope getScope();
}
