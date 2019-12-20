package org.brijframework.bean.scope.monitor.factories;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.factories.asm.AbstractBeanScopeFactory;
import org.brijframework.bean.scope.monitor.GlobelScope;
import org.brijframework.bean.scope.monitor.threads.GlobelThreadLocal;
import org.brijframework.util.reflect.InstanceUtil;

public class GlobelScopeMonitorFactroy extends AbstractBeanScopeFactory<String, GlobelScope>{
	
	public int count;
	private static GlobelScopeMonitorFactroy factory;
	private GlobelThreadLocal thread;

	public static GlobelScopeMonitorFactroy factory() {
		if (factory == null) {
			factory = InstanceUtil.getSingletonInstance(GlobelScopeMonitorFactroy.class);
		}
		return factory;
	}

	public GlobelScope currentService() {
		if (this.thread == null) {
			this.thread = new GlobelThreadLocal();
		}
		return thread.get();
	}

	@Override
	public GlobelScopeMonitorFactroy loadFactory() {
		return this;
	}

	@Override
	protected void preregister(String key, GlobelScope value) {
		System.err.println("Registering global scope :"+key);
	}

	@Override
	protected void postregister(String key, GlobelScope value) {
		
	}

	public GlobelScope getService() {
		return new GlobelScope();
	}

	@Override
	protected GlobelScope createBeanScope(BeanDefinition definition) {
		return new GlobelScope(definition);
	}
}