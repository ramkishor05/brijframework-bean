package org.brijframework.bean.mapper;

public interface BeanMapper<SR,TR> {

	SR sourceMapper(Object object);
	
	TR targetMapper(Object object);
	
}
