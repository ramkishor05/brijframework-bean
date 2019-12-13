package org.brijframework.bean.scope.monitor.threads;

import org.brijframework.bean.scope.monitor.PrototypeScope;

public class PrototypeThreadLocal extends ThreadLocal<PrototypeScope> {
	
	@Override
	public PrototypeScope get() {
		return super.get();
	}
}