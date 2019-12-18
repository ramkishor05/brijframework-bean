package org.brijframework.bean.util;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.factories.impl.BeanScopeFactoryImpl;
import org.brijframework.model.diffination.ModelPropertyDiffination;
import org.brijframework.model.diffination.ModelPropertyDiffinationGroup;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.support.Constants;
import org.brijframework.util.support.ReflectionAccess;

public class BeanScopeUtil {

	@SuppressWarnings("unchecked")
	public static <T> T getPropertyObject(Object instance, String _keyPath, boolean isDefault) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		String[] keyArray = _keyPath.split(Constants.SPLIT_DOT);
		return (T) getPropertyObject(instance, keyArray, isDefault);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getPropertyObject(Object instance, String[] keyArray, boolean isDefault) {
		Object current = instance;
		for (int i = 0; i < keyArray.length - 1; i++) {
			if(current==null) {
			   return null;
			}
			String keyPoint = keyArray[i];
			Object refPoint=getPropertyPath(instance, keyPoint, isDefault);
			if(refPoint==null) {
				current=refPoint;
				continue;
			}
			ModelPropertyDiffination setterMeta = setterPropertyDefination(current, keyPoint);
			if(setterMeta!=null) {
				refPoint=InstanceUtil.getInstance(setterMeta.getTargetAsField().getType());
				setPropertyPath(current, keyPoint, refPoint, isDefault);
				current=refPoint;
			}
		}
		return (T) current;
	}
	
	public static <T> T getPropertyPath(Object instance, String _keyPath, boolean isDefault) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		String[] keyArray = _keyPath.split(Constants.SPLIT_DOT);
		Object current = getPropertyObject(instance, keyArray, isDefault);
		Assertion.notEmpty(current, "Instance should not be null");
		String keyPoint=keyArray[keyArray.length-1];
		ModelPropertyDiffination getterMeta = getterPropertyDefination(current, keyPoint);
		if(getterMeta!=null) {
			return getProperty(current, keyPoint, getterMeta);
		}
		return PropertyAccessorUtil.getProperty(current, keyPoint, ReflectionAccess.PUBLIC);
	}

	protected static <T> T  getProperty(Object current, String keyPoint, ModelPropertyDiffination getterMeta) {
		System.out.println(keyPoint+" ==> getterMeta<== "+getterMeta);
		switch (getterMeta.getAccess()) {
		case AUTO:
			return PropertyAccessorUtil.getProperty(current, keyPoint);
		case READ_ONLY:
			return PropertyAccessorUtil.getProperty(current, keyPoint);
		case READ_WRITE:
			return PropertyAccessorUtil.getProperty(current, keyPoint);
		default:
			Assertion.state(false, "Can't read '"+keyPoint+"'. it's protected to read.");
			return null;
		}
	}
	
	public static <T> T setPropertyPath(Object instance, String _keyPath,Object _val, boolean isDefault) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		String[] keyArray = _keyPath.split(Constants.SPLIT_DOT);
		Object current = getPropertyObject(instance, keyArray, isDefault);
		String keyPoint=keyArray[keyArray.length-1];
		Assertion.notNull(current, "Instance should not be null");
		ModelPropertyDiffination setterMeta = setterPropertyDefination(current, keyPoint);
		System.out.println(keyPoint+" ==> getterMeta<== "+setterMeta);
		if(setterMeta!=null) {
			return setProperty(current, keyPoint, _val, setterMeta);
		}
		return PropertyAccessorUtil.setProperty(current, keyPoint, ReflectionAccess.PUBLIC, _val);
	}

	private static <T> T setProperty(Object current, String keyPoint,Object _val, ModelPropertyDiffination setterMeta) {
		switch (setterMeta.getAccess()) {
		case AUTO:
			return PropertyAccessorUtil.setProperty(current, keyPoint,_val);
		case WRITE_ONLY:
			return PropertyAccessorUtil.setProperty(current, keyPoint,_val);
		case READ_WRITE:
			return PropertyAccessorUtil.setProperty(current, keyPoint,_val);
		default:
			Assertion.state(false, "Can't write '"+keyPoint+"'. it's protected to write.");
			return null;
		}
	}

	private static ModelPropertyDiffination getterPropertyDefination(Object current, String keyPoint) {
		BeanDefinition beanDefinition = BeanScopeFactoryImpl.getFactory().getBeanDefinitionOfObject(current);
		if(beanDefinition==null) {
			return null;
		}
		ModelPropertyDiffinationGroup diffinationGroup = beanDefinition.getOwner().getProperty(keyPoint);
		if(diffinationGroup==null) {
			return null;
		}
		return diffinationGroup.getGetterMeta();
	}
	
	private static ModelPropertyDiffination setterPropertyDefination(Object current, String keyPoint) {
		BeanDefinition beanDefinition = BeanScopeFactoryImpl.getFactory().getBeanDefinitionOfObject(current);
		if(beanDefinition==null) {
			return null;
		}
		ModelPropertyDiffinationGroup diffinationGroup = beanDefinition.getOwner().getProperty(keyPoint);
		if(diffinationGroup==null) {
			return null;
		}
		return diffinationGroup.getSetterMeta();
	}
	
}
