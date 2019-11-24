package org.brijframework.bean.context.factories;

import org.brijframework.bean.context.BeanContext;
import org.brijframework.factories.impl.bootstrap.AbstractBootstrapFactory;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.config.OrderOn;
import org.brijframework.util.printer.ConsolePrint;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

@OrderOn(3)
public class BeanContextFactory extends AbstractBootstrapFactory<String, BeanContext>{
	
	private static BeanContextFactory factory ;
	
	@Assignable
	public static BeanContextFactory getFactory() {
		if(factory==null) {
		    factory=new BeanContextFactory();
		}
		return factory;
	}

	@Override
	public BeanContextFactory loadFactory() {
		try {
			ConsolePrint.screen("BootstrapFactory -> "+this.getClass().getSimpleName(), "Lunching the factory to bean context");
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
			ConsolePrint.screen("BootstrapFactory -> "+this.getClass().getSimpleName(), "Lunched the factory to bean context");
		} catch (Exception e) {
			ConsolePrint.screen("BootstrapFactory -> "+this.getClass().getSimpleName(), "Error Lunching the factory to bean context");
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
