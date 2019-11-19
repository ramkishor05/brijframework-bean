package org.brijframework.bean.context.factories;

import org.brijframework.bean.context.BeanContext;
import org.brijframework.factories.BootstrapFactory;
import org.brijframework.factories.Factory;
import org.brijframework.factories.impl.AbstractFactory;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public class BeanContextFactory extends AbstractFactory<String, BeanContext> implements BootstrapFactory{
	
	private static BeanContextFactory factory ;
	
	@Assignable
	public static BeanContextFactory getFactory() {
		if(factory==null) {
		    factory=new BeanContextFactory();
		}
		return factory;
	}

	@Override
	public Factory loadFactory() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls->{
				if(BeanContext.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					BeanContext beanContext = (BeanContext) InstanceUtil.getInstance(cls);
					beanContext.start();
					this.register(beanContext.getClass().getName(), beanContext);
					this.register(beanContext.getClass().getSimpleName(), beanContext);
				}
			});
			ReflectionUtils.getClassListFromInternal().forEach(cls->{
				if(BeanContext.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					BeanContext beanContext = (BeanContext) InstanceUtil.getInstance(cls);
					beanContext.start();
					this.register(beanContext.getClass().getName(), beanContext);
					this.register(beanContext.getClass().getSimpleName(), beanContext);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	protected void preregister(String key, BeanContext value) {
	}

	@Override
	protected void postregister(String key, BeanContext value) {
	}

	public BeanContext getBeanContext() {
		return getCache().get(BeanContext.class.getName());
	}
	
	public BeanContext getBeanContext(Class<? extends BeanContext> beanContextClass) {
		return getCache().get(beanContextClass.getName());
	}

}
