package org.brijframework.bean.scope.monitor.factories;

import javax.annotation.PreDestroy;

import org.brijframework.bean.scope.monitor.PrototypeScope;
import org.brijframework.bean.scope.monitor.threads.PrototypeThreadLocal;
import org.brijframework.factories.impl.AbstractFactory;
import org.brijframework.util.reflect.InstanceUtil;

public class PrototypeScopeMonitorFactroy extends AbstractFactory<String, PrototypeScope>{
	
	private static PrototypeThreadLocal thread;
	
	static {
		thread = new PrototypeThreadLocal();
	}
	
	private static PrototypeScopeMonitorFactroy factory;

	public static PrototypeScopeMonitorFactroy factory() {
		if (factory == null) {
			factory = InstanceUtil.getSingletonInstance(PrototypeScopeMonitorFactroy.class);
		}
		return factory;
	}

	public synchronized PrototypeScope currentService() {
		registerService(initialScope());
		return thread.get();
	}

	public synchronized PrototypeScopeMonitorFactroy registerService(PrototypeScope service) {
		thread.set(service);
		return factory;
	}

	public synchronized PrototypeScope initialScope() {
		return new PrototypeScope();
	}

	@Override
	public PrototypeScopeMonitorFactroy loadFactory() {
		return this;
	}

	@Override
	protected void preregister(String key, PrototypeScope value) {
	}

	@Override
	
	@PreDestroy
	protected void postregister(String key, PrototypeScope value) {
	}

}