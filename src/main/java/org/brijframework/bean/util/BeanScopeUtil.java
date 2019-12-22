package org.brijframework.bean.util;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.brijframework.Access;
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
	
	/*
	 * contains properties
	 */
	
	public static Boolean containsKeyPath(Object instance, String _keyPath, boolean isDefault) {
		Object current = getPropertyObject(instance, _keyPath, isDefault);
		if(current==null) {
			return false;
		}
		BeanDefinition beanDefinition = BeanScopeFactoryImpl.getFactory().getBeanDefinitionOfObject(current);
		ModelPropertyDiffination setterMeta = setterPropertyDefination(beanDefinition, _keyPath);
		if(setterMeta!=null) {
			return true;
		}else {
			return true;
		}
	}
	

	public static Boolean containsPathValue(Object modelBean, String _keyPath, boolean isDefault) {
		return null;
	}

	public static Class<?> typeOfPropertyPath(Object currentInstance, String _keyPath, boolean isDefault) {
		return null;
	}
	
	public static Map<String, Boolean> containsPropertiesPath(Object currentInstance, String _keyPath,
			boolean isDefault) {
		Map<String, Boolean> returnMap = new LinkedHashMap<String, Boolean>();
		String keyArray[] = _keyPath.split("~");
		for (int index = 0; index < keyArray.length; index++) {
			String _key = keyArray[index];
			returnMap.put(_key, containsKeyPath(currentInstance, _key, isDefault));
		}
		return returnMap;
	}
	
	/*
	 * Getting properties
	 */
	
	public static Set<String> getReadPropertiesNames(Object current) {
		BeanDefinition beanDefinition = BeanScopeFactoryImpl.getFactory().getBeanDefinitionOfObject(current);
		if(beanDefinition!=null) {
			return beanDefinition.getOwner().getPropertiesGetterNames(Access.READ_ONLY, Access.READ_WRITE);
		}
		return new HashSet<String>();
	}
	
	public static ModelPropertyDiffination getterPropertyDefination(BeanDefinition beanDefinition, String keyPoint) {
		if(beanDefinition==null) {
			return null;
		}
		ModelPropertyDiffinationGroup diffinationGroup = beanDefinition.getOwner().getProperty(keyPoint);
		if(diffinationGroup==null) {
			return null;
		}
		return diffinationGroup.getGetterMeta();
	}
	
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
			Object refPoint = getPropertyPath(instance, keyPoint, isDefault);
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
			return getPropertyPath(current, keyPoint,beanDefinition, getterMeta);
		}
		return PropertyAccessorUtil.getProperty(current, keyPoint, ReflectionAccess.PUBLIC);
	}

	public static <T> T  getPropertyPath(Object current, String keyPoint,BeanDefinition beanDefinition , ModelPropertyDiffination getterMeta) {
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
	

	public static void getPropertiesPath(Object instance, Map<String, Object> properties, boolean isDefault) {
		Assertion.notNull(properties, "Properties should not be null." );
		for (Entry<String, Object> entry : properties.entrySet()) {
			properties.put(entry.getKey(),getPropertyPath(instance, entry.getKey(), isDefault));
		}
	}
	
	public static Map<String, ?> getPropertiesPath(Object currentInstance, boolean isDefault) {
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (String _key : getReadPropertiesNames(currentInstance)) {
			returnMap.put(_key, getPropertyPath(currentInstance, _key, isDefault));
		}
		return returnMap;
	}

	public static Map<String, ?> getPropertiesPath(Object currentInstance, String[] _keyPath, boolean isDefault) {
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (String _key : _keyPath) {
			returnMap.put(_key, getPropertyPath(currentInstance, _key,isDefault));
		}
		return returnMap;
	}

	public static Map<String, ?> getSafeProperties(Object currentInstance, String[] _keyPath, boolean isDefault) {
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (String _key : _keyPath) {
			if (containsKeyPath(currentInstance, _key, isDefault)) {
				returnMap.put(_key, getPropertyPath(currentInstance,  _key, isDefault));
			}
		}
		return returnMap;
	}
	
	/*
	 * Setting properties
	 */
	
	public static ModelPropertyDiffination setterPropertyDefination(BeanDefinition beanDefinition, String keyPoint) {
		if(beanDefinition==null) {
			return null;
		}
		ModelPropertyDiffinationGroup diffinationGroup = beanDefinition.getOwner().getProperty(keyPoint);
		if(diffinationGroup==null) {
			return null;
		}
		return diffinationGroup.getSetterMeta();
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
			return setPropertyPath(current, keyPoint, _val, beanDefinition,setterMeta);
		}
		return PropertyAccessorUtil.setProperty(current, keyPoint, ReflectionAccess.PUBLIC, _val);
	}

	public static <T> T setPropertyPath(Object current, String keyPoint,Object _val, BeanDefinition beanDefinition , ModelPropertyDiffination setterMeta) {
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

	public static Map<String, ?> setPropertiesPath(Object currentInstance, Map<String, Object> _properties,
			boolean isDefault) {
		Assertion.notNull(_properties, "Properties should not be null." );
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (String _key : _properties.keySet()) {
			returnMap.put(_key, setPropertyPath(currentInstance, _key, _properties.get(_key),true));
		}
		return returnMap;
	}
	
	public static Map<String, ?> setPropertiesPath(Object currentInstance, String[] _keyPaths, Object[] _values,
			boolean isDefault) {
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
		for (int index = 0; index < _keyPaths.length; index++) {
			String _key = _keyPaths[index];
			Object _value=setPropertyPath(currentInstance, _key, _values[index], isDefault);
			returnMap.put(_key, _value);
		}
		return returnMap;
	}
	
	public static Map<String, ?> setPropertiesPath(Object currentInstance, String _keyPath, Object[] _values, boolean isDefault) {
		return setPropertiesPath(currentInstance, _keyPath.split("~"), _values, isDefault);
	}

	public static Map<String, ?> setSafePropertiesPath(Object currentInstance, String _keyPath, Object[] _values,
			boolean isDefault) {
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		String keyArray[] = _keyPath.split("~");
		for (int index = 0; index < keyArray.length; index++) {
			String _key = keyArray[index];
			if (containsKeyPath(currentInstance, _key, isDefault)) {
				returnMap.put(_key, setPropertyPath(currentInstance, _key, _values[index],true));
			}
		}
		return returnMap;
	}
	
}
