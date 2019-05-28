package org.brijframework.data.asm;

import org.brijframework.data.DataInfo;
import org.brijframework.meta.reflect.ClassMeta;
import org.brijframework.util.asserts.Assertion;

public class ObjectData implements DataInfo {
	
	private ClassMeta owner;
	private String id;
	private Object scopeObject;
	
	public ObjectData(ClassMeta owner) {
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
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setOwner(ClassMeta owner) {
		this.owner = owner;
	}
	
	public void setScopeObject(Object scopeObject) {
		this.scopeObject=scopeObject;
	}

	@SuppressWarnings("unchecked")
	public <T>T getScopeObject() {
		return (T) scopeObject;
	}
}
