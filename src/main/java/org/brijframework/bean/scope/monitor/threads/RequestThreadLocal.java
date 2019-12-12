package org.brijframework.bean.scope.monitor.threads;

import org.brijframework.bean.scope.monitor.RequestScope;
import org.brijframework.bean.scope.monitor.factories.RequestScopeMonitorFactroy;

public class RequestThreadLocal extends ThreadLocal<RequestScope> {
	@Override
	protected RequestScope initialValue() {
		RequestScope service = RequestScopeMonitorFactroy.factory().getService();
		RequestScopeMonitorFactroy.factory().count++;
		RequestScopeMonitorFactroy.factory().register(service.getId(), service);
		return service;
	}
	
	@Override
	public RequestScope get() {
		return super.get();
	}
}