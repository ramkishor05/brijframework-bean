package org.brijframework.bean.factories.json;

import org.brijframework.bean.factories.impl.BeanMetaDataFactoryImpl;
import org.brijframework.bean.factories.impl.BeanSetupFactoryImpl;
import org.brijframework.bean.meta.impl.BeanMetaDataImpl;
import org.brijframework.bean.resource.BeanResource;
import org.brijframework.model.factories.metadata.asm.impl.ClassModelMetaDataFactoryImpl;
import org.brijframework.model.info.ClassModelMetaData;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.support.Constants;

public class JsonBeanMetaDataFactory extends BeanMetaDataFactoryImpl{

	private static JsonBeanMetaDataFactory factory;

	@Assignable
	public static JsonBeanMetaDataFactory getFactory() {
		if(factory==null) {
			factory=new JsonBeanMetaDataFactory();
		}
		return factory;
	}
	
	@Override
	public JsonBeanMetaDataFactory loadFactory() {
		JsonBeanSetupFactory.getFactory().getCache().forEach((id,beansetup)->{
			register(id, beansetup);
		});
		return this;
	}

	public void register(String id, BeanResource metaSetup) {
		ClassModelMetaData owner=null;
		if(metaSetup.getModel() != null && !metaSetup.getModel().isEmpty() ) {
			owner=ClassModelMetaDataFactoryImpl.getFactory().getClassInfo(metaSetup.getModel());
		}else {
			owner=ClassModelMetaDataFactoryImpl.getFactory().register(metaSetup.getTarget());
		}
		Assertion.notNull(owner, "Model not found for "+metaSetup.getModel()+" of bean  : "+id);
		id=Constants.DEFAULT.equals(metaSetup.getId())?owner.getTarget().getSimpleName():metaSetup.getId();
		BeanResource bean=BeanSetupFactoryImpl.getFactory().find(id);
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
		BeanResource bean=BeanSetupFactoryImpl.getFactory().find(id);
		BeanMetaDataImpl dataSetup=new BeanMetaDataImpl(owner);
		dataSetup.setId(id);
		dataSetup.setName(bean.getName());
		dataSetup.setScope(Scope.valueFor(metaSetup.getScope()));
		dataSetup.setProperties(bean.getProperties());
		register(id,dataSetup);
	}

}
