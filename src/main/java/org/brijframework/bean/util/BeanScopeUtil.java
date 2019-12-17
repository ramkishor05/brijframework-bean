package org.brijframework.bean.util;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.factories.impl.BeanScopeFactoryImpl;
import org.brijframework.model.diffination.ModelPropertyDiffination;
import org.brijframework.model.diffination.ModelPropertyDiffinationGroup;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.support.Access;
import org.brijframework.util.support.Constants;

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
			String keyPoint = keyArray[i];
			Object refPoint=getPropertyPath(instance, keyPoint, isDefault);
			if(refPoint==null) {
				ModelPropertyDiffination setterMeta = setterPropertyDefination(current, keyPoint);
				if(setterMeta!=null) {
					refPoint=InstanceUtil.getInstance(setterMeta.getTargetAsField().getType());
					PropertyAccessorUtil.setProperty(current, keyPoint,setterMeta.getAccess(), refPoint);
				}
			}
			if(refPoint==null) {
				return null;
			}
			current=refPoint;
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
			return PropertyAccessorUtil.getProperty(current, keyPoint,getterMeta.getAccess());
		}
		return PropertyAccessorUtil.getProperty(current, keyPoint);
	}
	
	public static <T> T setPropertyPath(Object instance, String _keyPath,Object _val, boolean isDefault) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		String[] keyArray = _keyPath.split(Constants.SPLIT_DOT);
		Object current = getPropertyObject(instance, keyArray, isDefault);
		Assertion.notEmpty(current, "Instance should not be null");
		String keyPoint=keyArray[keyArray.length-1];
		ModelPropertyDiffination getterMeta = setterPropertyDefination(current, keyPoint);
		if(getterMeta!=null) {
			return PropertyAccessorUtil.setProperty(current, keyPoint,getterMeta.getAccess(), _val);
		}
		return PropertyAccessorUtil.setProperty(current, keyPoint, Access.PUBLIC, _val);
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
