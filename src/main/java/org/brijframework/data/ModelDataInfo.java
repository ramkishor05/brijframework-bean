package org.brijframework.data;

import org.brijframework.meta.reflect.ClassMeta;
import org.brijframework.util.asserts.Assertion;

public class ModelDataInfo implements DataInfo {
	
	private ClassMeta owner;
	private Object scopeObject;
	
	public ModelDataInfo(ClassMeta owner) {
		this.owner=owner;
	}
	
	public ClassMeta getOwner() {
		return owner;
	}
	
	@Override
	public void init() {
		Assertion.notNull(getOwner(), "Model info should not be null.");
	}

	public String getId() {
		return owner.getId();
	}
	
	public void setScopeObject(Object scopeObject) {
		this.scopeObject=scopeObject;
	}

	@SuppressWarnings("unchecked")
	public <T>T getScopeObject() {
		return (T) scopeObject;
	}
}
