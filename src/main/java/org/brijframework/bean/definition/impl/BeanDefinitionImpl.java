package org.brijframework.bean.definition.impl;

import java.util.HashMap;
import java.util.Map;

import org.brijframework.bean.definition.BeanDefinationConstructor;
import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.model.diffination.ModelTypeDeffination;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.asserts.Assertion;

public class BeanDefinitionImpl implements BeanDefinition {
	
	private ModelTypeDeffination owner;
	
	private String id;
	
	private String name;
	
	private Scope scope;
	
	private BeanDefinationConstructor constructor;
	
	private String factoryClass;
	
	private String factoryMethod;

	private Map<String, Object> properties;
	
	public BeanDefinitionImpl(ModelTypeDeffination owner) {
		this.owner=owner;
	}

	@Override
	public BeanDefinationConstructor getConstructor() {
		return constructor;
	}

	public void setConstructor(BeanDefinationConstructor constructor) {
		this.constructor = constructor;
	}
	
	public ModelTypeDeffination getOwner() {
		return owner;
	}
	
	@Override
	public void init() {
		Assertion.notNull(getOwner(), "Model should not be null.");
	}

	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setOwner(ModelTypeDeffination owner) {
		this.owner = owner;
	}
	
	public void setScope(Scope scope) {
		this.scope = scope;
	}
	
	public Scope getScope() {
		return scope;
	}

	@Override
	public String getFactoryClass() {
		return factoryClass;
	}

	public void setFactoryClass(String factoryClass) {
		this.factoryClass = factoryClass;
	}

	@Override
	public String getFactoryMethod() {
		return factoryMethod;
	}

	public void setFactoryMethod(String factoryMethod) {
		this.factoryMethod = factoryMethod;
	}

	public Map<String, Object> getProperties() {
		if(properties==null) {
			properties=new HashMap<>();
		}
		return properties;
	}
	
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return super.toString()+"["
		+ "id=" + id + ", "
		+ "name=" + name + ", "
		+ "scope=" + scope+ ", "
		+ "factoryClass=" + factoryClass + ", "
		+ "factoryMethod=" + factoryMethod + ", "
		+ "constructor="+ constructor+", "
		+ "model= "+owner.getId()+", "
		+ "properties=" + properties
		+ "]";
	}

}
