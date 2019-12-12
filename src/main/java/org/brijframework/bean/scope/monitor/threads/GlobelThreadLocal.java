package org.brijframework.bean.scope.monitor.threads;

import org.brijframework.bean.scope.monitor.GlobelScope;
import org.brijframework.bean.scope.monitor.factories.GlobelScopeMonitorFactroy;

public class GlobelThreadLocal extends ThreadLocal<GlobelScope> {
	@Override
	protected GlobelScope initialValue() {
		GlobelScope service = GlobelScopeMonitorFactroy.factory().getService();
		GlobelScopeMonitorFactroy.factory().count++;
		GlobelScopeMonitorFactroy.factory().register(service.getId(), service);
		return service;
	}
	
	@Override
	public GlobelScope get() {
		return super.get();
	}
}