package org.brijframework.bean.factories.annotation;

import org.brijframework.bean.factories.impl.BeanInfoFactoryImpl;
import org.brijframework.bean.factories.impl.BeanSetupFactoryImpl;
import org.brijframework.bean.info.impl.BeanInfoImpl;
import org.brijframework.bean.setup.BeanSetup;
import org.brijframework.model.factories.asm.ClassMetaInfoFactoryImpl;
import org.brijframework.model.info.OwnerModelInfo;
import org.brijframework.support.beans.Bean;
import org.brijframework.support.beans.Beans;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.ReflectionUtils;
import org.brijframework.util.support.Constants;

public class AnnotationBeanInfoFactory extends BeanInfoFactoryImpl{

	private static AnnotationBeanInfoFactory factory;

	@Assignable
	public static AnnotationBeanInfoFactory getFactory() {
		if(factory==null) {
			factory=new AnnotationBeanInfoFactory();
		}
		return factory;
	}
	
	@Override
	public AnnotationBeanInfoFactory loadFactory() {
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
		String id=Constants.DEFAULT.equals(metaSetup.id())?target.getSimpleName():metaSetup.id();
		OwnerModelInfo owner=ClassMetaInfoFactoryImpl.getFactory().getClassInfo(metaSetup.model());
		Assertion.notNull(owner, "Model not found for "+metaSetup.model()+" of bean  : "+id);
		BeanSetup bean=BeanSetupFactoryImpl.getFactory().getBeanSetup(id);
		BeanInfoImpl dataSetup=new BeanInfoImpl(owner);
		dataSetup.setId(id);
		dataSetup.setName(bean.getName());
		dataSetup.setScope(metaSetup.scope());
		dataSetup.setProperties(bean.getProperties());
		register(dataSetup);
	}

}
