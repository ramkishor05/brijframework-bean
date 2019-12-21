package org.brijframework.bean.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

	public static final String REF_BY_KEY="@ref";
	
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
			if(refPoint!=null) {
				current=refPoint;
				continue;
			}
			BeanDefinition beanDefinition = BeanScopeFactoryImpl.getFactory().getBeanDefinitionOfObject(current);
			ModelPropertyDiffination setterMeta = setterPropertyDefination(beanDefinition, keyPoint);
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
		BeanDefinition beanDefinition = BeanScopeFactoryImpl.getFactory().getBeanDefinitionOfObject(current);
		ModelPropertyDiffination getterMeta = getterPropertyDefination(beanDefinition, keyPoint);
		if(getterMeta!=null) {
			return getProperty(current, keyPoint,beanDefinition, getterMeta);
		}
		return PropertyAccessorUtil.getProperty(current, keyPoint, ReflectionAccess.PUBLIC);
	}

	protected static <T> T  getProperty(Object current, String keyPoint,BeanDefinition beanDefinition , ModelPropertyDiffination getterMeta) {
		switch (getterMeta.getAccess()) {
		case AUTO:
			return PropertyAccessorUtil.getProperty(current, keyPoint,ReflectionAccess.PRIVATE);
		case READ_ONLY:
			return PropertyAccessorUtil.getProperty(current, keyPoint,ReflectionAccess.PRIVATE);
		case READ_WRITE:
			return PropertyAccessorUtil.getProperty(current, keyPoint,ReflectionAccess.PRIVATE);
		default:
			Assertion.state(false, "Can't read '"+keyPoint+"' for bean "+beanDefinition.getId()+" Model '"+getterMeta.getOwner().getId()+"' is protected to read.");
			return null;
		}
	}
	
	public static <T> T setPropertyPath(Object instance, String _keyPath,Object _val, boolean isDefault) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		String[] keyArray = _keyPath.split(Constants.SPLIT_DOT);
		Object current = getPropertyObject(instance, keyArray, isDefault);
		String keyPoint=keyArray[keyArray.length-1];
		if(keyPoint.endsWith(BeanScopeUtil.REF_BY_KEY)) {
			keyPoint=keyPoint.split(BeanScopeUtil.REF_BY_KEY)[0];
			_val = BeanScopeFactoryImpl.getFactory().getObjectForRef(_val.toString());
		}else if(_val instanceof Map && ((Map<?,?>) _val).containsKey(BeanScopeUtil.REF_BY_KEY)) {
			 String ref=(String) ((Map<?,?>) _val).get(BeanScopeUtil.REF_BY_KEY);
			_val = BeanScopeFactoryImpl.getFactory().getObjectForRef(ref);
		}
		Assertion.notNull(current, "Instance should not be null");
		BeanDefinition beanDefinition = BeanScopeFactoryImpl.getFactory().getBeanDefinitionOfObject(current);
		ModelPropertyDiffination setterMeta = setterPropertyDefination(beanDefinition, keyPoint);
		if(setterMeta!=null) {
			return setProperty(current, keyPoint, _val, beanDefinition,setterMeta);
		}
		return PropertyAccessorUtil.setProperty(current, keyPoint, ReflectionAccess.PUBLIC, _val);
	}

	private static <T> T setProperty(Object current, String keyPoint,Object _val, BeanDefinition beanDefinition , ModelPropertyDiffination setterMeta) {
		switch (setterMeta.getAccess()) {
		case AUTO:
			return PropertyAccessorUtil.setProperty(current, keyPoint,ReflectionAccess.PRIVATE,_val);
		case WRITE_ONLY:
			return PropertyAccessorUtil.setProperty(current, keyPoint,ReflectionAccess.PRIVATE,_val);
		case READ_WRITE:
			return PropertyAccessorUtil.setProperty(current, keyPoint,ReflectionAccess.PRIVATE,_val);
		default:
			Assertion.state(false, "Can't write '"+keyPoint+"' for bean "+beanDefinition.getId()+". Model '"+setterMeta.getOwner().getId()+"' is protected to write.");
			return null;
		}
	}

	private static ModelPropertyDiffination getterPropertyDefination(BeanDefinition beanDefinition, String keyPoint) {
		if(beanDefinition==null) {
			return null;
		}
		ModelPropertyDiffinationGroup diffinationGroup = beanDefinition.getOwner().getProperty(keyPoint);
		if(diffinationGroup==null) {
			return null;
		}
		return diffinationGroup.getGetterMeta();
	}
	
	private static ModelPropertyDiffination setterPropertyDefination(BeanDefinition beanDefinition, String keyPoint) {
		if(beanDefinition==null) {
			return null;
		}
		ModelPropertyDiffinationGroup diffinationGroup = beanDefinition.getOwner().getProperty(keyPoint);
		if(diffinationGroup==null) {
			return null;
		}
		return diffinationGroup.getSetterMeta();
	}

	public static Map<String, ?> setPropertiesPath(Object instance, Map<String, Object> properties, boolean isDefault) {
		Assertion.notNull(properties, "Properties should not be null." );
		for (Entry<String, Object> entry : properties.entrySet()) {
			setPropertyPath(instance, entry.getKey(), entry.getValue(), isDefault);
		}
		return properties;
	}
	
	public static void getPropertiesPath(Object instance, Map<String, Object> properties, boolean isDefault) {
		Assertion.notNull(properties, "Properties should not be null." );
		for (Entry<String, Object> entry : properties.entrySet()) {
			properties.put(entry.getKey(),getPropertyPath(instance, entry.getKey(), isDefault));
		}
	}

	public static Map<String, ?> getPropertiesPath(Object current, boolean isDefault) {
		Map<String, Object> properties =new HashMap<String, Object>();
		BeanDefinition beanDefinition = BeanScopeFactoryImpl.getFactory().getBeanDefinitionOfObject(current);
		return null;
	}

	public static Map<String, ?> setSafePropertiesPath(Object currentInstance, String _keyPath, Object[] _values,
			boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Map<String, ?> setPropertiesPath(Object currentInstance, String _keys, Object[] _values, boolean b) {
		return null;
	}

	public static Map<String, ?> getPropertiesPath(Object currentInstance, String[] _keyPath, boolean isDefault) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Map<String, ?> getSafeProperties(Object currentInstance, String[] _keyPath, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Map<String, Boolean> containsPropertiesPath(Object currentInstance, String _keyPath, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Map<String, ?> setPropertiesPath(Object currentInstance, String[] _keyPaths, Object[] _values,
			boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Class<?> typeOfPropertyPath(Object currentInstance, String _keyPath, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Boolean containsPathValue(Object currentInstance, String _keyPath, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Boolean containsPathKey(Object currentInstance, String _keyPath, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
