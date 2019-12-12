package org.brijframework.bean.scope.monitor.factories;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.bean.scope.monitor.SessionScope;
import org.brijframework.bean.scope.monitor.threads.SessionThreadLocal;
import org.brijframework.factories.impl.AbstractFactory;
import org.brijframework.util.reflect.InstanceUtil;

public class SessionScopeMonitorFactroy extends AbstractFactory<String, SessionScope>{
	
	public int count;
	private static SessionScopeMonitorFactroy factory;
	private SessionThreadLocal thread;
	private SessionScope service;
	private static ConcurrentHashMap<Object, SessionScope> container = new ConcurrentHashMap<>();

	public static SessionScopeMonitorFactroy factory() {
		if (factory == null) {
			factory = InstanceUtil.getSingletonInstance(SessionScopeMonitorFactroy.class);
		}
		return factory;
	}

	public SessionScope currentService() {
		if (this.thread == null) {
			registerService(new SessionScope());
		}
		return thread.get();
	}

	public SessionScopeMonitorFactroy registerService(SessionScope service) {
		this.service = service;
		this.thread = new SessionThreadLocal();
		return factory;
	}

	public SessionScope getService() {
		return this.service;
	}

	public void setObject(Object key, SessionScope service) {
		container.put(key, service);
	}

	@Override
	public SessionScopeMonitorFactroy loadFactory() {
		return null;
	}

	@Override
	protected void preregister(String key, SessionScope value) {
	}

	@Override
	protected void postregister(String key, SessionScope value) {
	}

}