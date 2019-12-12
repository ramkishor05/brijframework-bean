package org.brijframework.bean.scope.monitor;

import org.brijframework.bean.scope.monitor.threads.GlobelThreadLocal;
import org.brijframework.util.runtime.RandomUtil;

public class GlobelScope{
	private String id;
	private String device;
	private GlobelThreadLocal monitor;

	public GlobelScope() {
		this.id = RandomUtil.genRandomUUID();
	}
	
	public String getId() {
		return id;
	}
	
	public void setDevice(String device) {
		this.device = device;
	}
	
	public String getDevice() {
		return device;
	}
	
	public GlobelThreadLocal getMonitor() {
		return monitor;
	}
	
	public void setMonitor(GlobelThreadLocal monitor) {
		this.monitor = monitor;
	}
	
}



