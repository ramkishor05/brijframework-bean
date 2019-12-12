package org.brijframework.bean.scope.monitor.threads;

import org.brijframework.bean.scope.monitor.SessionScope;
import org.brijframework.bean.scope.monitor.factories.SessionScopeMonitorFactroy;

public class SessionThreadLocal extends ThreadLocal<SessionScope> {
	@Override
	protected SessionScope initialValue() {
		SessionScope service = SessionScopeMonitorFactroy.factory().getService();
		SessionScopeMonitorFactroy.factory().count++;
		SessionScopeMonitorFactroy.factory().setObject(service.getId(), service);
		return service;
	}
	
	@Override
	public SessionScope get() {
		return super.get();
	}
}