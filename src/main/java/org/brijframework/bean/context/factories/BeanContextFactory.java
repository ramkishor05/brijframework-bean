package org.brijframework.bean.context.factories;

import org.brijframework.bean.context.BeanContext;
import org.brijframework.boot.runner.ApplicationContextRunner;
import org.brijframework.factories.impl.bootstrap.AbstractBootstrapFactory;
import org.brijframework.group.Group;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.support.ordering.OrderOn;
import org.brijframework.util.factories.ReflectionFactory;
import org.brijframework.util.printer.LoggerConsole;
import org.brijframework.util.reflect.InstanceUtil;

@OrderOn(3)
public class BeanContextFactory extends AbstractBootstrapFactory<String, BeanContext>{
	
	private static BeanContextFactory factory ;
	
	private static boolean isLoaded= false;
	
	@SingletonFactory
	public static BeanContextFactory getFactory() {
		if(factory==null) {
		    factory=new BeanContextFactory();
		    ApplicationContextRunner.run();
		}
		return factory;
	}

	@Override
	public BeanContextFactory loadFactory() {
		if(isLoaded) {
			return this;
		}
		isLoaded=true;
		try {
			LoggerConsole.screen("BootstrapFactory -> "+this.getClass().getSimpleName(), "Lunching the factory to bean context");
			ReflectionFactory.getFactory().getExternalClassList().forEach(cls->{
				if(BeanContext.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					BeanContext beanContext = (BeanContext) InstanceUtil.getInstance(cls);
					beanContext.start();
					this.register(beanContext.getClass().getSimpleName().equals(BeanContext.class.getSimpleName()+"Impl")? BeanContext.class.getSimpleName(): beanContext.getClass().getSimpleName(), beanContext);
					this.register(beanContext.getClass().getName().equals(BeanContext.class.getName()+"Impl")? BeanContext.class.getName(): beanContext.getClass().getName(), beanContext);
			    }
			});
			ReflectionFactory.getFactory().getInternalClassList().forEach(cls->{
				if(BeanContext.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					BeanContext beanContext = (BeanContext) InstanceUtil.getInstance(cls);
					beanContext.start();
					this.register(beanContext.getClass().getSimpleName().equals(BeanContext.class.getSimpleName()+"Impl")? BeanContext.class.getSimpleName(): beanContext.getClass().getSimpleName(), beanContext);
					this.register(beanContext.getClass().getName().equals(BeanContext.class.getName()+"Impl")? BeanContext.class.getName(): beanContext.getClass().getName(), beanContext);
				}
			});
			LoggerConsole.screen("BootstrapFactory -> "+this.getClass().getSimpleName(), "Lunched the factory to bean context");
		} catch (Exception e) {
			LoggerConsole.screen("BootstrapFactory -> "+this.getClass().getSimpleName(), "Error Lunching the factory to bean context");
			e.printStackTrace();
		}
		return this;
	}

	@Override
	protected void preregister(String key, BeanContext value) {
		LoggerConsole.screen("BeanContext -> "+value.getClass().getSimpleName(), "Lunching the bean context");
	}

	@Override
	protected void postregister(String key, BeanContext value) {
		LoggerConsole.screen("BeanContext -> "+value.getClass().getSimpleName(), "Lunched the bean context");
	}

	public BeanContext getBeanContext() {
		return getCache().get(BeanContext.class.getSimpleName());
	}
	
	public BeanContext getBeanContext(Class<? extends BeanContext> beanContextClass) {
		return getCache().get(beanContextClass.getName());
	}

	@Override
	public void loadContainer(String key, BeanContext value) {
		if (getContainer() == null) {
			return;
		}
		Group group = getContainer().load(value.getClass().getName());
		if(!group.containsKey(key)) {
			group.add(key, value);
		}
	}

}
