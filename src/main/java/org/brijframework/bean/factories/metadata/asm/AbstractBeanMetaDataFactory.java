package org.brijframework.bean.factories.metadata.asm;

import java.util.ArrayList;
import java.util.List;

import org.brijframework.bean.factories.metadata.BeanMetaDataGroupFactory;
import org.brijframework.bean.factories.resource.impl.BeanResourceFactoryImpl;
import org.brijframework.bean.meta.BeanMetaData;
import org.brijframework.bean.meta.impl.BeanMetaDataImpl;
import org.brijframework.bean.resource.BeanResource;
import org.brijframework.factories.impl.AbstractFactory;
import org.brijframework.group.Group;
import org.brijframework.model.factories.metadata.asm.impl.ClassModelMetaDataFactoryImpl;
import org.brijframework.model.info.ClassModelMetaData;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.printer.ConsolePrint;
import org.brijframework.util.support.Constants;

public abstract class AbstractBeanMetaDataFactory extends AbstractFactory<String,BeanMetaData> implements BeanMetaDataGroupFactory<String,BeanMetaData>{

	public void register(String id, BeanResource metaSetup) {
		ClassModelMetaData owner=null;
		if(metaSetup.getModel() != null && !metaSetup.getModel().isEmpty() ) {
			owner=ClassModelMetaDataFactoryImpl.getFactory().getClassInfo(metaSetup.getModel());
		}else {
			owner=ClassModelMetaDataFactoryImpl.getFactory().register(metaSetup.getTarget());
		}
		Assertion.notNull(owner, "Model not found for "+metaSetup.getModel()+" of bean  : "+id);
		id=Constants.DEFAULT.equals(metaSetup.getId())?owner.getTarget().getSimpleName():metaSetup.getId();
		BeanResource bean=BeanResourceFactoryImpl.getFactory().find(id);
		BeanMetaDataImpl dataSetup=new BeanMetaDataImpl(owner);
		dataSetup.setId(id);
		dataSetup.setName(bean.getName());
		dataSetup.setScope(Scope.valueFor(metaSetup.getScope(),Scope.SINGLETON));
		dataSetup.setProperties(bean.getProperties());
		register(id,dataSetup);
	}

	public void register(Class<?> target, BeanResource metaSetup) {
		String id=Constants.DEFAULT.equals(metaSetup.getId())?target.getSimpleName():metaSetup.getId();
		ClassModelMetaData owner=ClassModelMetaDataFactoryImpl.getFactory().getClassInfo(metaSetup.getModel());
		Assertion.notNull(owner, "Model not found for "+metaSetup.getModel()+" of bean  : "+id);
		BeanResource bean=BeanResourceFactoryImpl.getFactory().find(id);
		BeanMetaDataImpl dataSetup=new BeanMetaDataImpl(owner);
		dataSetup.setId(id);
		dataSetup.setName(bean.getName());
		dataSetup.setScope(Scope.valueFor(metaSetup.getScope()));
		dataSetup.setProperties(bean.getProperties());
		register(id,dataSetup);
	}
	
	@Override
	public AbstractBeanMetaDataFactory clear() {
		getCache().clear();
		return this;
	}

	public List<BeanMetaData> getBeanInfoList(Class<?> cls) {
		List<BeanMetaData> list=new ArrayList<>();
		for (BeanMetaData beanInfo : getCache().values()) {
			if(cls.isAssignableFrom(beanInfo.getOwner().getTarget())) {
				list.add(beanInfo);
			}
		}
		return list;
	}

	@Override
	public BeanMetaData register(String key,BeanMetaData dataSetup) {
		loadContainer(dataSetup);
		ConsolePrint.screen("Resource", "Registery for bean data with id :"+dataSetup.getId());
		getCache().put(dataSetup.getId(), dataSetup);
		return dataSetup;
	}
	
	@Override
	public List<BeanMetaData> findAllByModel(String model) {
		List<BeanMetaData> list=new ArrayList<>();
		for(BeanMetaData setup:getCache().values()) {
			if(setup.getOwner().getId().equals(model)) {
				list.add(setup);
			}
		}
		return list;
	}
	
	@Override
	public BeanMetaData find(String modelKey) {
		if(getCache().containsKey(modelKey)) {
			return getCache().get(modelKey);
		}
		return getContainer(modelKey);
	}
	
	public BeanMetaData getContainer(String modelKey) {
		if (getContainer() == null) {
			return null;
		}
		return getContainer().find(modelKey);
	}
	
	public void loadContainer(BeanMetaData metaInfo) {
		if (getContainer() == null) {
			return;
		}
		Group group = getContainer().load(metaInfo.getName());
		if(!group.containsKey(metaInfo.getId())) {
			group.add(metaInfo.getId(), metaInfo);
		}else {
			group.update(metaInfo.getId(), metaInfo);
		}
	}

	@Override
	protected void preregister(String key, BeanMetaData value) {
	}

	@Override
	protected void postregister(String key, BeanMetaData value) {
	}

}
