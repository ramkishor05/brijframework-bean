package org.brijframework.bean.config.impl;

import org.brijframework.model.config.ModelConfig;

public class AnnotationBeanConfig implements ModelConfig {
	
	private boolean enable;
	private String location;

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

}
