package org.brijframework.bean.factories.json;

import org.brijframework.bean.factories.impl.BeanInfoFactoryImpl;
import org.brijframework.bean.factories.impl.BeanSetupFactoryImpl;
import org.brijframework.bean.info.impl.BeanInfoImpl;
import org.brijframework.bean.setup.BeanSetup;
import org.brijframework.model.factories.asm.ClassMetaInfoFactoryImpl;
import org.brijframework.model.info.OwnerModelInfo;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.support.Constants;

public class JsonBeanInfoFactory extends BeanInfoFactoryImpl{

	private static JsonBeanInfoFactory factory;

	@Assignable
	public static JsonBeanInfoFactory getFactory() {
		if(factory==null) {
			factory=new JsonBeanInfoFactory();
		}
		return factory;
	}
	
	@Override
	public JsonBeanInfoFactory loadFactory() {
		JsonBeanSetupFactory.getFactory().getCache().forEach((id,beansetup)->{
			register(id, beansetup);
		});
		return this;
	}

	public void register(String id, BeanSetup metaSetup) {
		OwnerModelInfo owner=null;
		if(metaSetup.getModel() != null && !metaSetup.getModel().isEmpty() ) {
			owner=ClassMetaInfoFactoryImpl.getFactory().getClassInfo(metaSetup.getModel());
		}else {
			owner=ClassMetaInfoFactoryImpl.getFactory().register(metaSetup.getTarget());
		}
		Assertion.notNull(owner, "Model not found for "+metaSetup.getModel()+" of bean  : "+id);
		id=Constants.DEFAULT.equals(metaSetup.getId())?owner.getTarget().getSimpleName():metaSetup.getId();
		BeanSetup bean=BeanSetupFactoryImpl.getFactory().getBeanSetup(id);
		BeanInfoImpl dataSetup=new BeanInfoImpl(owner);
		dataSetup.setId(id);
		dataSetup.setName(bean.getName());
		dataSetup.setScope(Scope.valueFor(metaSetup.getScope(),Scope.SINGLETON));
		dataSetup.setProperties(bean.getProperties());
		register(dataSetup);
	}

	public void register(Class<?> target, BeanSetup metaSetup) {
		String id=Constants.DEFAULT.equals(metaSetup.getId())?target.getSimpleName():metaSetup.getId();
		OwnerModelInfo owner=ClassMetaInfoFactoryImpl.getFactory().getClassInfo(metaSetup.getModel());
		Assertion.notNull(owner, "Model not found for "+metaSetup.getModel()+" of bean  : "+id);
		BeanSetup bean=BeanSetupFactoryImpl.getFactory().getBeanSetup(id);
		BeanInfoImpl dataSetup=new BeanInfoImpl(owner);
		dataSetup.setId(id);
		dataSetup.setName(bean.getName());
		dataSetup.setScope(Scope.valueFor(metaSetup.getScope()));
		dataSetup.setProperties(bean.getProperties());
		register(dataSetup);
	}

}
