package org.brijframework.bean.factories.resource.annotation;

import org.brijframework.bean.factories.resource.asm.AbstractBeanResourceFactory;
import org.brijframework.bean.resource.impl.BeanResourceConstructorImpl;
import org.brijframework.bean.resource.impl.BeanResourceImpl;
import org.brijframework.bean.resource.impl.BeanResourceParamImpl;
import org.brijframework.support.bean.Bean;
import org.brijframework.support.bean.BeanParam;
import org.brijframework.support.bean.Beans;
import org.brijframework.support.bean.properties.BeanProperty;
import org.brijframework.support.enums.Scope;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.support.ordering.OrderOn;
import org.brijframework.util.factories.ReflectionFactory;
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
		ReflectionFactory.getFactory().getInternalClassList().forEach(target -> {
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

	private void register(Class<?> target, Bean beanResource) {
		BeanResourceImpl dataSetup=new BeanResourceImpl();
		dataSetup.setId(Constants.DEFAULT.equals(beanResource.id())?target.getSimpleName():beanResource.id());
		dataSetup.setName(Constants.DEFAULT.equals(beanResource.name())?target.getSimpleName():beanResource.name());
		dataSetup.setModel(Constants.DEFAULT.equals(beanResource.model())?target.getSimpleName():beanResource.model());
		dataSetup.setScope(Constants.DEFAULT.equals(beanResource.id())? Scope.SINGLETON.toString() :beanResource.scope().toString());
		dataSetup.setType(Constants.DEFAULT.equals(beanResource.type())? target.getName() :beanResource.type());
		dataSetup.setFactoryClass(Constants.DEFAULT.equals(beanResource.factoryClass())?null:beanResource.factoryClass());
		dataSetup.setFactoryMethod(Constants.DEFAULT.equals(beanResource.factoryMethod())?null:beanResource.factoryMethod());
		for(BeanProperty field: beanResource.properties()) {
		    dataSetup.getProperties().put(field.name(), field.value());
		}
		if(beanResource.constructor()!=null) {
			BeanResourceConstructorImpl constructor=new BeanResourceConstructorImpl();
			constructor.setId(beanResource.constructor().id());
			if(beanResource.constructor().params()!=null) {
				for(BeanParam beanParam: beanResource.constructor().params() ) {
				   constructor.getParams().add(new BeanResourceParamImpl(beanParam.index(), beanParam.type(), beanParam.name(), beanParam.value()));
				}
			}
			dataSetup.setConstructor(constructor);
		}
		register(dataSetup.getId(),dataSetup);
	}

}
