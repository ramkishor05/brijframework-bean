package org.brijframework.bean.builder;

import org.brijframework.bean.BeanObject;
import org.brijframework.bean.context.factories.BeanContextFactory;

public class BeanBuilder implements BeanObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object instance;
	
	public BeanBuilder(Object instance) {
		this.instance=instance;
	}
	
	public static BeanBuilder getBuilder(Object instance) {
		return new BeanBuilder(instance);
	}
	
	public static BeanBuilder getBuilder(Class<?> model) {
		Object object=BeanContextFactory.getFactory().getBeanContext().getBean(model);
		return getBuilder(object);
	}
	
	public static BeanBuilder getBuilder(String model) {
		Object object=BeanContextFactory.getFactory().getBeanContext().getBean(model);
		return getBuilder(object);
	}
	
	@Override
	public Object getCurrentInstance() {
		return instance;
	}
		
}
