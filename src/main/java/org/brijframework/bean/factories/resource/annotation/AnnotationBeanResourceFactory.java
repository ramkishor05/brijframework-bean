package org.brijframework.bean.factories.resource.annotation;

import org.brijframework.bean.factories.resource.asm.AbstractBeanResourceFactory;
import org.brijframework.bean.resource.impl.BeanResourceImpl;
import org.brijframework.support.beans.Attribute;
import org.brijframework.support.beans.Bean;
import org.brijframework.support.beans.Beans;
import org.brijframework.support.config.OrderOn;
import org.brijframework.support.config.SingletonFactory;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.reflect.ReflectionUtils;
import org.brijframework.util.support.Constants;

@OrderOn(1)
public class AnnotationBeanResourceFactory extends AbstractBeanResourceFactory{

	private static AnnotationBeanResourceFactory factory;

	@SingletonFactory
	public static AnnotationBeanResourceFactory getFactory() {
		if(factory==null) {
			factory=new AnnotationBeanResourceFactory();
		}
		return factory;
	}
	
	@Override
	public AnnotationBeanResourceFactory loadFactory() {
		ReflectionUtils.getInternalClassList().forEach(target -> {
			if (target.isAnnotationPresent(Beans.class) || target.isAnnotationPresent(Bean.class)) {
				this.register(target);
			}
		});
		return this;
	}

	public void register(Class<?> target) {
		if (target.isAnnotationPresent(Beans.class)) {
			Beans models = target.getAnnotation(Beans.class);
			for (Bean metaSetup : models.value()) {
				this.register(target, metaSetup);
			}
		}
		if (target.isAnnotationPresent(Bean.class)) {
			Bean metaSetup = target.getAnnotation(Bean.class);
			this.register(target, metaSetup);
		}
	}

	private void register(Class<?> target, Bean metaSetup) {
		BeanResourceImpl dataSetup=new BeanResourceImpl();
		dataSetup.setId(Constants.DEFAULT.equals(metaSetup.id())?target.getSimpleName():metaSetup.id());
		dataSetup.setName(Constants.DEFAULT.equals(metaSetup.name())?target.getSimpleName():metaSetup.name());
		dataSetup.setModel(Constants.DEFAULT.equals(metaSetup.model())?target.getSimpleName():metaSetup.model());
		dataSetup.setScope(Constants.DEFAULT.equals(metaSetup.id())? Scope.SINGLETON.toString() :metaSetup.scope().toString());
		dataSetup.setFactoryClass(Constants.DEFAULT.equals(metaSetup.factoryClass())?null:metaSetup.factoryClass());
		dataSetup.setFactoryMethod(Constants.DEFAULT.equals(metaSetup.factoryMethod())?null:metaSetup.factoryMethod());
		for(Attribute field: metaSetup.properties()) {
		    dataSetup.getProperties().put(field.name(), field.value());
		}
		register(dataSetup.getId(),dataSetup);
	}

}
