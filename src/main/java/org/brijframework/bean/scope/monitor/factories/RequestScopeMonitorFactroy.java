package org.brijframework.bean.scope.monitor.factories;

import org.brijframework.bean.scope.monitor.RequestScope;
import org.brijframework.bean.scope.monitor.threads.RequestThreadLocal;
import org.brijframework.factories.impl.AbstractFactory;

public class RequestScopeMonitorFactroy extends AbstractFactory<String, RequestScope>{
	
	public int count;
	private static RequestScopeMonitorFactroy factory;
	private RequestThreadLocal thread;
	private RequestScope service;

	public static RequestScopeMonitorFactroy factory() {
		if (factory == null) {
			factory = new RequestScopeMonitorFactroy();
		}
		return factory;
	}

	public RequestScope currentService() {
		if (this.thread == null) {
			registerService(new RequestScope());
		}
		return thread.get();
	}

	public RequestScopeMonitorFactroy registerService(RequestScope service) {
		this.service = service;
		this.thread = new RequestThreadLocal();
		return factory;
	}

	public RequestScope getService() {
		return this.service;
	}

	@Override
	public RequestScopeMonitorFactroy loadFactory() {
		return this;
	}

	@Override
	protected void preregister(String key, RequestScope value) {
	}

	@Override
	protected void postregister(String key, RequestScope value) {
	}
}