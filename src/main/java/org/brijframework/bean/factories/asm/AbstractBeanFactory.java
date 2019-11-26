package org.brijframework.bean.factories.asm;

import java.util.List;

import org.brijframework.bean.factories.BeanFactory;
import org.brijframework.bean.factories.metadata.impl.BeanMetaDataFactoryImpl;
import org.brijframework.bean.factories.scope.impl.BeanScopeFactoryImpl;
import org.brijframework.bean.meta.BeanMetaData;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.factories.impl.module.AbstractModuleFactory;
import org.brijframework.util.asserts.Assertion;

public abstract class AbstractBeanFactory extends AbstractModuleFactory<String, Object> implements BeanFactory{

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String name) {
		BeanScope find = BeanScopeFactoryImpl.getFactory().find(name);
		if(find!=null) {
			return (T) find.getScopeObject();
		}
		BeanMetaData beanMetaData = BeanMetaDataFactoryImpl.getFactory().find(name);
		Assertion.isNull(beanMetaData, "Bean not found");;
		BeanScope register = BeanScopeFactoryImpl.getFactory().register(name, beanMetaData);
		return (T) register.getScopeObject();
	}

	@Override
	public <T> T getBean(Class<? extends Object> beanClass) {
		return null;
	}

	@Override
	public List<?> getBeans(Class<? extends Object> beanClass) {
		return null;
	}

	@Override
	public List<String> getBeanNames() {
		return null;
	}

	@Override
	public List<String> getBeanNames(Class<?> beanClass) {
		return null;
	}

	@Override
	protected void preregister(String key, Object value) {
		
	}

	@Override
	protected void postregister(String key, Object value) {
		
	}

}
