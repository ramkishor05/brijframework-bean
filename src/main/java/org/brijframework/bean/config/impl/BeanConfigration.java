package org.brijframework.bean.config.impl;

import org.brijframework.bean.config.BeanConfig;

public class BeanConfigration implements BeanConfig{
	
	private boolean enable;
	private String location;
	private String packages;

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
	
	public void setPackages(String packages) {
		this.packages = packages;
	}
	
	public String getPackages() {
		return packages;
	}
}
