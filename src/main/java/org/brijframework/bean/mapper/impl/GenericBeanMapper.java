package org.brijframework.bean.mapper.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.brijframework.bean.mapper.BeanMapper;
import org.brijframework.bean.util.BeanMapperUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.InstanceUtil;

public class GenericBeanMapper<SR,TR> implements BeanMapper<SR,TR> {

	public GenericBeanMapper() {
	}

	public Class<?> getGenericType(int index) {
		try {
			Type sooper = getClass().getGenericSuperclass();
			return (Class<?>) ((ParameterizedType) sooper).getActualTypeArguments()[index];
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public SR getBuildSourceObject() {
		Class<?> toClass=getGenericType(0);
		Assertion.notNull(toClass, "Generic type should not be null or empty.");
		return (SR) InstanceUtil.getInstance(toClass);
	}
	
	@SuppressWarnings("unchecked")
	public TR getBuildTargetObject() {
		Class<?> toClass=getGenericType(1);
		Assertion.notNull(toClass, "Generic type should not be null or empty.");
		return (TR) InstanceUtil.getInstance(toClass);
	}

	@Override
	public SR sourceMapper(Object fromObject) {
		try {
			SR toObject=getBuildSourceObject();
			Assertion.notNull(toObject, "Generic object should not be null or empty.");
			sourceMapper(toObject, fromObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public TR targetMapper(Object fromObject) {
		TR toObject=getBuildTargetObject();
		Assertion.notNull(toObject, "Generic object should not be null or empty.");
		this.targetMapper(toObject, fromObject);
		return toObject;
	}
	
	@SuppressWarnings("unchecked")
	public void sourceMapper(Object toObject,Object fromObject) {
		try {
			Assertion.notNull(toObject, "To object should not be null or empty.");
			if(fromObject instanceof Map) {
				BeanMapperUtil.mappedToObjectFromMap(toObject,(Map<String,Object>)fromObject);
			}else {
				BeanMapperUtil.mappedToObjectFromObject(toObject,fromObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void targetMapper(Object toObject,Object fromObject) {
		try {
			Assertion.notNull(toObject, "To object should not be null or empty.");
			if(fromObject instanceof Map) {
				BeanMapperUtil.mappedFromObjectToMap(toObject,(Map<String,Object>)fromObject);
			}else {
				BeanMapperUtil.mappedFromObjectToObject(toObject,fromObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
