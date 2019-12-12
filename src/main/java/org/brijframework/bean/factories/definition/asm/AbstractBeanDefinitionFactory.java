package org.brijframework.bean.factories.definition.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.definition.impl.BeanDefinitionImpl;
import org.brijframework.bean.factories.definition.BeanDefinitionFactory;
import org.brijframework.bean.factories.resource.impl.BeanResourceFactoryImpl;
import org.brijframework.bean.resource.BeanResource;
import org.brijframework.factories.impl.AbstractFactory;
import org.brijframework.group.Group;
import org.brijframework.model.diffination.TypeModelDiffination;
import org.brijframework.model.factories.metadata.impl.TypeModelMetaDataFactoryImpl;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.printer.LoggerConsole;
import org.brijframework.util.support.Constants;

public abstract class AbstractBeanDefinitionFactory extends AbstractFactory<String,BeanDefinition> implements BeanDefinitionFactory<String,BeanDefinition>{


	public boolean contains(String id) {
		return BeanResourceFactoryImpl.getFactory().find(id)!=null;
	}
	
	public void register(String id, BeanResource metaSetup) {
		TypeModelDiffination owner=null;
		if(metaSetup.getModel() != null && !metaSetup.getModel().isEmpty() ) {
			owner=TypeModelMetaDataFactoryImpl.getFactory().findById(metaSetup.getModel());
			if(owner==null) {
				owner=TypeModelMetaDataFactoryImpl.getFactory().findOrCreate(metaSetup.getModel());
			}
		}else {
			owner=TypeModelMetaDataFactoryImpl.getFactory().register(metaSetup.getType());
		}
		Assertion.notNull(owner, "Model not found for "+metaSetup.getModel()+" of bean  : "+id);
		id=Constants.DEFAULT.equals(metaSetup.getId())?owner.getType().getSimpleName():metaSetup.getId();
		BeanResource bean=BeanResourceFactoryImpl.getFactory().find(id);
		BeanDefinitionImpl dataSetup=new BeanDefinitionImpl(owner);
		dataSetup.setId(id);
		dataSetup.setName(bean.getName());
		dataSetup.setScope(Scope.valueFor(metaSetup.getScope(),Scope.SINGLETON));
		dataSetup.setFactoryClass(Constants.DEFAULT.equals(metaSetup.getFactoryClass())?null:metaSetup.getFactoryClass());
		dataSetup.setFactoryMethod(Constants.DEFAULT.equals(metaSetup.getFactoryMethod())?null:metaSetup.getFactoryMethod());
		dataSetup.setProperties(bean.getProperties());
		register(id,dataSetup);
	}

	public void register(Class<?> target, BeanResource metaSetup) {
		String id=Constants.DEFAULT.equals(metaSetup.getId())?target.getSimpleName():metaSetup.getId();
		TypeModelDiffination owner=TypeModelMetaDataFactoryImpl.getFactory().findById(metaSetup.getModel());
		Assertion.notNull(owner, "Model not found for "+metaSetup.getModel()+" of bean  : "+id);
		BeanResource bean=BeanResourceFactoryImpl.getFactory().find(id);
		BeanDefinitionImpl dataSetup=new BeanDefinitionImpl(owner);
		dataSetup.setId(id);
		dataSetup.setName(bean.getName());
		dataSetup.setScope(Scope.valueFor(metaSetup.getScope()));
		dataSetup.setProperties(bean.getProperties());
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
		for (BeanDefinition beanInfo : getCache().values()) {
			if(cls.isAssignableFrom(beanInfo.getOwner().getType())) {
				list.add(beanInfo);
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
		LoggerConsole.screen("BeanMeta", "Registering for bean data with id :"+value.getId());
	}

	@Override
	protected void postregister(String key, BeanDefinition value) {
		LoggerConsole.screen("BeanMeta", "Registered for bean data with id :"+value.getId());
	}

}
