package org.brijframework.bean.factories.annotation;

import org.brijframework.bean.factories.impl.BeanSetupFactoryImpl;
import org.brijframework.bean.setup.impl.BeanSetupImpl;
import org.brijframework.support.beans.Attribute;
import org.brijframework.support.beans.Bean;
import org.brijframework.support.beans.Beans;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.enums.Scope;
import org.brijframework.util.reflect.ReflectionUtils;
import org.brijframework.util.support.Constants;

public class AnnotationBeanSetupFactory extends BeanSetupFactoryImpl{

	private static AnnotationBeanSetupFactory factory;

	@Assignable
	public static AnnotationBeanSetupFactory getFactory() {
		if(factory==null) {
			factory=new AnnotationBeanSetupFactory();
		}
		return factory;
	}
	
	@Override
	public AnnotationBeanSetupFactory loadFactory() {
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
		BeanSetupImpl dataSetup=new BeanSetupImpl();
		dataSetup.setId(Constants.DEFAULT.equals(metaSetup.id())?target.getSimpleName():metaSetup.id());
		dataSetup.setName(Constants.DEFAULT.equals(metaSetup.name())?target.getSimpleName():metaSetup.name());
		dataSetup.setModel(Constants.DEFAULT.equals(metaSetup.id())?target.getSimpleName():metaSetup.id());
		dataSetup.setScope(Constants.DEFAULT.equals(metaSetup.id())? Scope.SINGLETON.toString() :metaSetup.id());
		for(Attribute field: metaSetup.properties()) {
		    dataSetup.getProperties().put(field.name(), field.value());
		}
		register(dataSetup);
	}

}
