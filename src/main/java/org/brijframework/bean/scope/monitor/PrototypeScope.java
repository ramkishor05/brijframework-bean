package org.brijframework.bean.scope.monitor;

import org.brijframework.bean.scope.BeanScope;
import org.brijframework.util.runtime.RandomUtil;

public class PrototypeScope extends BeanScope{
	private String id;
	private String device;

	public PrototypeScope() {
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
	
}



