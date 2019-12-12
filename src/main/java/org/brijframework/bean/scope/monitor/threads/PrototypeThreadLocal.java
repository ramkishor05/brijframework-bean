package org.brijframework.bean.scope.monitor.threads;

import org.brijframework.bean.scope.monitor.PrototypeScope;
import org.brijframework.bean.scope.monitor.factories.PrototypeScopeMonitorFactroy;

public class PrototypeThreadLocal extends ThreadLocal<PrototypeScope> {
	@Override
	protected PrototypeScope initialValue() {
		PrototypeScope service = PrototypeScopeMonitorFactroy.factory().initialScope();
		PrototypeScopeMonitorFactroy.factory().register(service.getId(), service);
		return service;
	}
	
	@Override
	public PrototypeScope get() {
		return super.get();
	}
}