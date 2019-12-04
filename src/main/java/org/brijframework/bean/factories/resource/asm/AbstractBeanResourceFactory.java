package org.brijframework.bean.factories.resource.asm;

import java.util.ArrayList;
import java.util.List;

import org.brijframework.bean.factories.resource.BeanResourceFactory;
import org.brijframework.bean.resource.BeanResource;
import org.brijframework.factories.impl.module.AbstractModuleFactory;
import org.brijframework.group.Group;
import org.brijframework.util.printer.LoggerConsole;

public class AbstractBeanResourceFactory extends AbstractModuleFactory<String,BeanResource> implements BeanResourceFactory<String,BeanResource>{

	@Override
	public AbstractBeanResourceFactory loadFactory() {
		return this;
	}

	@Override
	public boolean contains(BeanResource dataSetup) {
		return getCache().containsValue(dataSetup);
	}
	
	@Override
	public boolean contains(String key) {
		return getCache().containsKey(key);
	}
	
	public BeanResource find(Class<? extends Object> beanClass) {
		for(BeanResource setup:getCache().values()) {
			if(beanClass.getName().equals(setup.getTarget())) {
				return setup;
			}
		}
		return null;
	}
	
	@Override
	public List<BeanResource> findAllByModel(String model) {
		List<BeanResource> list=new ArrayList<>();
		for(BeanResource setup:getCache().values()) {
			if(setup.getModel()!=null && setup.getModel().equals(model)) {
				list.add(setup);
			}
		}
		return list;
	}
	
	public void loadContainer(String key, BeanResource value) {
		if (getContainer() == null) {
			return;
		}
		Group group = getContainer().load(value.getName());
		if(!group.containsKey(key)) {
			group.add(key, value);
		}else {
			group.update(key, value);
		}
	}

	@Override
	protected void preregister(String key, BeanResource value) {
		LoggerConsole.screen("Resource", "Loading bean resource with id :"+key);
	}

	@Override
	protected void postregister(String key, BeanResource value) {
		LoggerConsole.screen("Resource", "Loaded bean resource with id :"+key);
	}

}
