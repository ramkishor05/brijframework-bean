package org.brijframework.bean.factories.definition.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.brijframework.bean.definition.BeanDefinationConstructor;
import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.definition.impl.BeanDefinationConstructorImpl;
import org.brijframework.bean.definition.impl.BeanDefinitionImpl;
import org.brijframework.bean.factories.definition.BeanDefinitionFactory;
import org.brijframework.bean.factories.resource.impl.BeanResourceFactoryImpl;
import org.brijframework.bean.resource.BeanResource;
import org.brijframework.bean.resource.BeanResourceConstructor;
import org.brijframework.bean.resource.BeanResourceParam;
import org.brijframework.factories.impl.AbstractFactory;
import org.brijframework.group.Group;
import org.brijframework.model.diffination.ModelParameterDiffination;
import org.brijframework.model.diffination.ModelTypeDeffination;
import org.brijframework.model.factories.deffination.impl.DefaultTypeModelDeffinationFactory;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.printer.LoggerConsole;
import org.brijframework.util.support.Constants;

public abstract class AbstractBeanDefinitionFactory extends AbstractFactory<String,BeanDefinition> implements BeanDefinitionFactory<String,BeanDefinition>{

	public boolean contains(String id) {
		return BeanResourceFactoryImpl.getFactory().find(id)!=null;
	}
	
	public void register(String id, BeanResource beanResource) {
		ModelTypeDeffination owner=null;
		if(beanResource.getModel() != null && !beanResource.getModel().isEmpty() ) {
			owner=DefaultTypeModelDeffinationFactory.getFactory().findById(beanResource.getModel());
			if(owner==null) {
				owner=DefaultTypeModelDeffinationFactory.getFactory().findOrCreate(beanResource.getModel());
			}
		}else {
			owner=DefaultTypeModelDeffinationFactory.getFactory().register(beanResource.getType());
		}
		Assertion.notNull(owner, "Model not found for "+beanResource.getModel()+" of bean  : "+id);
		id=Constants.DEFAULT.equals(beanResource.getId())?owner.getType().getSimpleName():beanResource.getId();
		BeanDefinitionImpl beanDefinition=new BeanDefinitionImpl(owner);
		beanDefinition.setId(id);
		beanDefinition.setName(beanResource.getName());
		beanDefinition.setScope(Scope.valueFor(beanResource.getScope(),Scope.SINGLETON));
		beanDefinition.setFactoryClass(Constants.DEFAULT.equals(beanResource.getFactoryClass())?null:beanResource.getFactoryClass());
		beanDefinition.setFactoryMethod(Constants.DEFAULT.equals(beanResource.getFactoryMethod())?null:beanResource.getFactoryMethod());
		beanDefinition.setConstructor(buildConstructor(owner,beanResource.getConstructor()));
		beanDefinition.setProperties(beanResource.getProperties());
		register(id,beanDefinition);
	}

	private BeanDefinationConstructor buildConstructor(ModelTypeDeffination owner,BeanResourceConstructor resourceConstructor) {
		BeanDefinationConstructorImpl definationConstructor=new BeanDefinationConstructorImpl();
		if(resourceConstructor!=null) {
			definationConstructor.setId(resourceConstructor.getId());
			definationConstructor.setModel(resourceConstructor.getModel());
			definationConstructor.setName(resourceConstructor.getName());
			Object[] values=new Object[resourceConstructor.getParameters().size()];
			for(BeanResourceParam beanResourceParam: resourceConstructor.getParameters()) {
				values[beanResourceParam.getIndex()]=beanResourceParam.getValue();
			}
			definationConstructor.setValues(values);
		}else {
			definationConstructor.setId(owner.getConstructor().getId());
			definationConstructor.setModel(owner.getId());
			definationConstructor.setName(owner.getConstructor().getName());
			Object[] values=new Object[owner.getConstructor().getParameters().size()];
			for(ModelParameterDiffination<?> beanResourceParam: owner.getConstructor().getParameters()) {
				values[beanResourceParam.getIndex()]=null;
			}
			definationConstructor.setValues(values);
		}
		return definationConstructor;
	}
	

	public void register(Class<?> target, BeanResource beanResource) {
		String id=Constants.DEFAULT.equals(beanResource.getId())?target.getSimpleName():beanResource.getId();
		ModelTypeDeffination owner=DefaultTypeModelDeffinationFactory.getFactory().findById(beanResource.getModel());
		Assertion.notNull(owner, "Model not found for "+beanResource.getModel()+" of bean  : "+id);
		BeanDefinitionImpl dataSetup=new BeanDefinitionImpl(owner);
		dataSetup.setId(id);
		dataSetup.setName(beanResource.getName());
		dataSetup.setScope(Scope.valueFor(beanResource.getScope()));
		dataSetup.setProperties(beanResource.getProperties());
		register(id,dataSetup);
	}
	
	public List<String> getBeanNames() {
		List<String> list=new ArrayList<>();
		for (Entry<String, BeanDefinition> entry : getCache().entrySet()) {
			list.add(entry.getKey());
		}
		return list;
	}
	
	public List<String> getBeanNames(Class<?> beanClass) {
		List<String> list=new ArrayList<>();
		for (Entry<String, BeanDefinition> entry : getCache().entrySet()) {
			if(beanClass.isAssignableFrom(entry.getValue().getOwner().getType())) {
			  list.add(entry.getKey());
			}
		}
		return list;
	}
	
	@Override
	public AbstractBeanDefinitionFactory clear() {
		getCache().clear();
		return this;
	}

	@Override
	public List<BeanDefinition> findAll(Class<?> cls) {
		List<BeanDefinition> list=new ArrayList<>();
		for (BeanDefinition definition : getCache().values()) {
			if(cls.isAssignableFrom(definition.getOwner().getType())) {
				list.add(definition);
			}
		}
		return list;
	}

	@Override
	public BeanDefinition register(String key,BeanDefinition value) {
		preregister(key, value);
		loadContainer(key, value);
		getCache().put(key, value);
		postregister(key, value);
		return value;
	}
	
	public void loadContainer(String key, BeanDefinition value) {
		if (getContainer() == null) {
			return;
		}
		Group group = getContainer().load(value.getOwner().getName());
		if(!group.containsKey(key)) {
			group.add(key, value);
		}else {
			group.update(key, value);
		}
	}
	
	@Override
	public List<BeanDefinition> findAllByModel(String model) {
		List<BeanDefinition> list=new ArrayList<>();
		for(BeanDefinition setup:getCache().values()) {
			if(setup.getOwner().getId().equals(model)) {
				list.add(setup);
			}
		}
		return list;
	}
	
	@Override
	public BeanDefinition find(String modelKey) {
		BeanDefinition beanMetaData = getCache().get(modelKey);
		if(beanMetaData!=null) {
			return beanMetaData;
		}
		return getContainer(modelKey);
	}
	
	public BeanDefinition getContainer(String modelKey) {
		if (getContainer() == null) {
			return null;
		}
		return getContainer().find(modelKey);
	}

	@Override
	protected void preregister(String key, BeanDefinition value) {
		LoggerConsole.screen("BeanDefinition", "Registering for bean data with id :"+value.getId());
	}

	@Override
	protected void postregister(String key, BeanDefinition value) {
		LoggerConsole.screen("BeanDefinition", "Registered for bean data with id :"+value.getId());
	}

}
