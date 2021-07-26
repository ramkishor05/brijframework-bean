package org.brijframework.bean.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.brijframework.Access;
import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.factories.BeanScopeFactory;
import org.brijframework.bean.factories.impl.BeanScopeFactoryImpl;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.model.diffination.ModelPropertyDiffination;
import org.brijframework.model.diffination.ModelPropertyDiffinationGroup;
import org.brijframework.util.accessor.MetaAccessorUtil;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.ClassUtil;
import org.brijframework.util.reflect.ConstructUtil;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ParamUtil;
import org.brijframework.util.support.Constants;
import org.brijframework.util.support.ReflectionAccess;
import org.brijframework.util.validator.ValidationUtil;

public class BeanScopeUtil {

	public static final String REF_BY_KEY = "@ref";

	/*
	 * contains properties
	 */

	public static Boolean containsKeyPath(Object instance, String _keyPath, boolean isDefault, boolean isLogger) {
		Object current = getCurrentObjectForGetter(instance, _keyPath, isDefault, isLogger);
		if (current == null) {
			return false;
		}
		BeanDefinition beanDefinition = BeanScopeFactoryImpl.getFactory().getBeanDefinitionOfObject(current);
		ModelPropertyDiffination setterMeta = setterPropertyDefination(beanDefinition, _keyPath);
		if (setterMeta != null) {
			return true;
		} else {
			return true;
		}
	}

	public static Boolean containsPathValue(Object instance, String _keyPath, boolean isDefault, boolean isLogger) {
		Object current = getCurrentObjectForGetter(instance, _keyPath, isDefault, isLogger);
		if (current == null) {
			return false;
		}
		return null;
	}

	public static Class<?> typeOfPropertyPath(Object instance, String _keyPath, boolean isDefault, boolean isLogger) {
		return null;
	}

	public static Map<String, Boolean> containsPropertiesPath(Object instance, String _keyPath, boolean isDefault,
			boolean isLogger) {
		Map<String, Boolean> returnMap = new LinkedHashMap<String, Boolean>();
		String keyArray[] = _keyPath.split("~");
		for (int index = 0; index < keyArray.length; index++) {
			String _key = keyArray[index];
			returnMap.put(_key, containsKeyPath(instance, _key, isDefault, isLogger));
		}
		return returnMap;
	}

	/*
	 * Getting properties
	 */

	public static Set<String> getPropertiesNames(Object instance, Access... accesses) {
		BeanDefinition beanDefinition = BeanScopeFactoryImpl.getFactory().getBeanDefinitionOfObject(instance);
		if (beanDefinition != null) {
			return beanDefinition.getOwner().getPropertiesNames(accesses);
		}
		return new HashSet<String>();
	}

	public static ModelPropertyDiffination getterPropertyDefination(BeanDefinition beanDefinition, String keyPoint) {
		if (beanDefinition == null) {
			return null;
		}
		ModelPropertyDiffinationGroup diffinationGroup = beanDefinition.getOwner().getProperty(keyPoint);
		if (diffinationGroup == null) {
			return null;
		}
		return diffinationGroup.getGetterMeta();
	}

	public static PropertyObject get1PropertyObject(Object instance, String _keyPath, boolean isDefault,
			boolean isLogger) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		String[] keyArray = _keyPath.split(Constants.SPLIT_DOT);
		return getCurrentObjectForGetter(instance, keyArray, isDefault, isLogger);
	}
	
	public static PropertyObject getCurrentObjectForGetter(Object instance, String _keyPath, boolean isDefault,
			boolean isLogger) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		String[] keyArray = _keyPath.split(Constants.SPLIT_DOT);
		return getCurrentObjectForSetter(instance, keyArray, isDefault, isLogger);
	}
	
	public static PropertyObject getCurrentObjectForSetter(Object instance, String[] keyArray, boolean isDefault,
			boolean isLogger) {
		Object current = instance;
		for (int i = 0; i < keyArray.length - 1; i++) {
			if (current == null) {
				return null;
			}
			String keyPoint = keyArray[i];
			if (isArray(keyPoint)) {
				current = getPropertyPathFromArray(current, keyPoint, isDefault, isLogger, true);
			} else {
				current = getPropertyPath(current, keyPoint, isDefault, isLogger);
			}
		}
		PropertyObject propertyObject = new PropertyObject();
		propertyObject.setObject(current);
		propertyObject.setProperty(keyArray[keyArray.length - 1]);
		return propertyObject;
	}

	public static PropertyObject getCurrentObjectForGetter(Object instance, String[] keyArray, boolean isDefault,
			boolean isLogger) {
		Object current = instance;
		for (int i = 0; i < keyArray.length - 1; i++) {
			if (current == null) {
				return null;
			}
			String keyPoint = keyArray[i];
			if (isArray(keyPoint)) {
				current = getPropertyPathFromArray(current, keyPoint, isDefault, isLogger ,true);
			} else {
				Object refPoint = getPropertyPath(current, keyPoint, isDefault, isLogger);
				if(refPoint==null) {
					AccessibleObject colling = MetaAccessorUtil.findSetterMeta(current.getClass(), keyPoint, ReflectionAccess.PRIVATE);
					Class<?> tyep=colling instanceof Method ? ((Method) colling).getParameterTypes()[0] : ((Field) colling).getType();
					refPoint=InstanceUtil.getInstance(tyep);
					PropertyAccessorUtil.setSafeProperty(current, colling, refPoint);
					current = refPoint;
				}else {
					current = refPoint;
				}
			}
		}
		PropertyObject propertyObject = new PropertyObject();
		propertyObject.setObject(current);
		propertyObject.setProperty(keyArray[keyArray.length - 1]);
		return propertyObject;
	}

	private static boolean isArray(String keyPoint) {
		return keyPoint.contains("[") && keyPoint.contains("]");
	}
	
	private static Integer getArrayIndex(String _keyPath) {
		String indexOf = _keyPath.substring(_keyPath.indexOf("[") + 1, _keyPath.indexOf("]")).trim();
		if(ValidationUtil.isNumber(indexOf)) {
			return Integer.valueOf(indexOf);
		}
		return null;
	}
	
	private static String getArrayKey(String _keyPath) {
	  return _keyPath.substring(0, _keyPath.indexOf("["));
	}

	public static <T> T getPropertyPath(Object instance, String _keyPath, boolean isDefault, boolean isLogger) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		PropertyObject propertyObject = getCurrentObjectForGetter(instance, _keyPath, isDefault, isLogger);
		if (isArray(propertyObject.getProperty())) {
			return getPropertyPathFromArray(propertyObject.getObject(), propertyObject.getProperty(), isDefault, isLogger, true);
		}
		return PropertyAccessorUtil.getProperty(propertyObject.getObject(), propertyObject.getProperty(), ReflectionAccess.PRIVATE);
	}
	
	public static <T> T getPropertyPath(Map<?,?> instance, String _keyPath, boolean isDefault, boolean isLogger) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		PropertyObject propertyObject = getCurrentObjectForGetter(instance, _keyPath, isDefault, isLogger);
		if (isArray(propertyObject.getProperty())) {
			return getPropertyPathFromArray(propertyObject.getObject(), propertyObject.getProperty(), isDefault, isLogger, true);
		}
		return PropertyAccessorUtil.getProperty(propertyObject.getObject(), propertyObject.getProperty(), ReflectionAccess.PRIVATE);
	}
	
	public static <T> T getPropertyPath(Collection<?> instance, String _keyPath, boolean isDefault, boolean isLogger) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		PropertyObject propertyObject = getCurrentObjectForGetter(instance, _keyPath, isDefault, isLogger);
		if (isArray(propertyObject.getProperty())) {
			return getPropertyPathFromArray(propertyObject.getObject(), propertyObject.getProperty(), isDefault, isLogger, true);
		}
		return PropertyAccessorUtil.getProperty(propertyObject.getObject(), propertyObject.getProperty(), ReflectionAccess.PRIVATE);
	}
	
	private static Collection<?> getPropertyCollection(Object object, String key, boolean isDefault, boolean isLogger, Boolean setter) {
		AccessibleObject colling = MetaAccessorUtil.findGetterMeta(object.getClass(), key, ReflectionAccess.PRIVATE);
		Collection<?> collection = getPropertyPath(object, key, isDefault, isLogger);
		if(!setter) {
			return collection;
		}
		if(collection==null) {
			collection=(Collection<?>) InstanceUtil.getImpletationInstanse(colling instanceof Method ? ((Method) colling).getReturnType() : ((Field) colling).getType());
			PropertyAccessorUtil.setSafeProperty(object, colling, collection);
			return  collection;
		}
		return collection;
	
	}

	@SuppressWarnings("unchecked")
	private static <T> T getPropertyPathFromArray(Object object, String _keyPath, boolean isDefault, boolean isLogger, Boolean setter) {
		String key = _keyPath.substring(0, _keyPath.indexOf("["));
		int index = Integer.valueOf(_keyPath.substring(_keyPath.indexOf("[") + 1, _keyPath.indexOf("]")).trim());
		AccessibleObject colling = MetaAccessorUtil.findSetterMeta(object.getClass(), key, ReflectionAccess.PRIVATE);
		Collection<?> propertyCollection = getPropertyCollection(object, key, isDefault, isLogger, setter);
		if (propertyCollection instanceof List<?>) {
			Class<?> cls=ClassUtil.collectionParamType(colling);
			Constructor<?> constructor = ConstructUtil.getConstructors(cls).get(0);
			Object obj=InstanceUtil.getInstance(constructor,ParamUtil.getDefaultDgruments(constructor));
			((List<Object>) propertyCollection).add(index, obj);
			return (T) ((List<?>) propertyCollection).get(index);
		} else if (propertyCollection instanceof Set<?>) {
			int size=((Set<Object>) propertyCollection).size();
			if(size>index) {
				return (T) ((Set<?>) propertyCollection).toArray()[index];
			}else {
				int idx=0;
				Map<Integer, Object> map=new LinkedHashMap<>();
				for(Object inObj: ((Set<Object>) propertyCollection)) {
					map.put(idx++, inObj);
				}
				if(map.containsKey(index)) {
					return (T) map.get(index);
				}
				for(idx=0; idx<=index; idx++) {
					Class<?> cls=ClassUtil.collectionParamType(colling);
					Constructor<?> constructor = ConstructUtil.getConstructors(cls).get(0);
					Object obj=InstanceUtil.getInstance(constructor,ParamUtil.getDefaultDgruments(constructor));
					((Set<Object>) propertyCollection).add(obj);
				}
			}
			return (T) ((Set<?>) propertyCollection).toArray()[index];
		}  else {
			return null;
		}
	}
	
	
	public static <T> T getPropertyPath(Object instance, String keyPoint, BeanDefinition beanDefinition,
			ModelPropertyDiffination getterMeta, boolean isLogger) {
		switch (getterMeta.getAccess()) {
		case AUTO:
			return PropertyAccessorUtil.getProperty(instance, keyPoint, ReflectionAccess.PRIVATE);
		case READ_ONLY:
			return PropertyAccessorUtil.getProperty(instance, keyPoint, ReflectionAccess.PRIVATE);
		case READ_WRITE:
			return PropertyAccessorUtil.getProperty(instance, keyPoint, ReflectionAccess.PRIVATE);
		default:
			if (isLogger) {
				Assertion.state(false, "Can't read '" + keyPoint + "' for bean " + beanDefinition.getId() + " Model '"
						+ getterMeta.getOwner().getId() + "' is protected to read.");
			}
			return null;
		}
	}

	public static void getPropertiesPath(Object instance, Map<String, Object> properties, boolean isDefault,
			boolean isLogger) {
		Assertion.notNull(properties, "Properties should not be null.");
		for (Entry<String, Object> entry : properties.entrySet()) {
			properties.put(entry.getKey(), getPropertyPath(instance, entry.getKey(), isDefault, isLogger));
		}
	}

	public static Map<String, ?> getPropertiesPath(Object instance, boolean isDefault, boolean isLogger) {
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (String _key : getPropertiesNames(instance, Access.READ_ONLY, Access.READ_WRITE)) {
			returnMap.put(_key, getPropertyPath(instance, _key, isDefault, isLogger));
		}
		return returnMap;
	}

	public static Map<String, ?> getPropertiesPath(Object instance, String[] _keyPath, boolean isDefault,
			boolean isLogger) {
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (String _key : _keyPath) {
			returnMap.put(_key, getPropertyPath(instance, _key, isDefault, isLogger));
		}
		return returnMap;
	}

	public static Map<String, ?> getSafeProperties(Object instance, String[] _keyPath, boolean isDefault,
			boolean isLogger) {
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (String _key : _keyPath) {
			if (containsKeyPath(instance, _key, isDefault, isLogger)) {
				returnMap.put(_key, getPropertyPath(instance, _key, isDefault, isLogger));
			}
		}
		return returnMap;
	}

	/*
	 * Setting properties
	 */
	

	@SuppressWarnings("unchecked")
	private static <T> T setPropertyPathToCollection(BeanScopeFactory<?, ?> beanScopeFactory, Object object, String _keyPath, Object _val, boolean isDefault, boolean isLogger) {
		Integer index = getArrayIndex(_keyPath);
		String key = getArrayKey(_keyPath);
		AccessibleObject colling = MetaAccessorUtil.findGetterMeta(object.getClass(), key, ReflectionAccess.PRIVATE);
		Object refPoint = getPropertyPath(object, key, isDefault, isLogger);
		if(refPoint==null) {
			refPoint=InstanceUtil.getImpletationInstanse(colling instanceof Method ? ((Method) colling).getReturnType() : ((Field) colling).getType());
			PropertyAccessorUtil.setSafeProperty(object, colling, refPoint);
		}
		if (refPoint instanceof List<?>) {
			Object obj=InstanceUtil.getInstance(ClassUtil.collectionParamType(colling));
			((List<Object>) refPoint).add(index, obj);
			return (T) ((List<?>) refPoint).get(index);
		} else if (refPoint instanceof Set<?>) {
			Object obj=InstanceUtil.getInstance(ClassUtil.collectionParamType(colling));
			((Set<Object>) refPoint).add(obj);
			return (T) ((Set<?>) refPoint).toArray()[index];
		}else {
			return null;
		}
	}

	public static ModelPropertyDiffination setterPropertyDefination(BeanDefinition beanDefinition, String keyPoint) {
		if (beanDefinition == null) {
			return null;
		}
		ModelPropertyDiffinationGroup diffinationGroup = beanDefinition.getOwner().getProperty(keyPoint);
		if (diffinationGroup == null) {
			return null;
		}
		return diffinationGroup.getSetterMeta();
	}

	public static <T> T setPropertyPath(Object instance, String _keyPath, Object _val, boolean isDefault,
			boolean isLogger) {
		return setPropertyPath(BeanScopeFactoryImpl.getFactory(), instance, _keyPath, _val, isDefault, isLogger);
	}
	

	public static <T> T setPropertyPath(BeanScopeFactory<String, BeanScope> beanScopeFactory, Object instance, String _keyPath, Object _val, boolean isDefault, boolean isLogger) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		PropertyObject propertyObject = getCurrentObjectForSetter(instance, _keyPath, isDefault, isLogger);
		Assertion.state(propertyObject.getObject() != null,"Can't write '" + propertyObject.getProperty() + "' for bean : "+propertyObject.getObject());
		if(isArray(propertyObject.getProperty())) {
			return setPropertyPathToCollection(beanScopeFactory, propertyObject.getObject(), propertyObject.getProperty(), _val, isDefault, isLogger);
		}
		if(propertyObject.getProperty().endsWith(REF_BY_KEY)) {
			return setPropertyPathFromRef(beanScopeFactory, propertyObject.getObject(), propertyObject.getProperty(), _val, isDefault, isLogger);
		}
		return PropertyAccessorUtil.setProperty(propertyObject.getObject(), propertyObject.getProperty(),ReflectionAccess.PRIVATE, _val);
	}
	

	public static PropertyObject getCurrentObjectForSetter(Object instance, String _keyPath, boolean isDefault,
			boolean isLogger) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		String[] keyArray = _keyPath.split(Constants.SPLIT_DOT);
		return getCurrentObjectForGetter(instance, keyArray, isDefault, isLogger);
	}
	

	@SuppressWarnings("unchecked")
	private static <T> T setPropertyPathFromRef(BeanScopeFactory<String, BeanScope> beanScopeFactory, Object object, String property, Object _val, boolean isDefault, boolean isLogger) {
		String keyPoint = property.split(REF_BY_KEY)[0];
		if(_val==null) {
			return null;
		}
		if(_val instanceof Map) {
			Map<String,String> refMap= (Map<String, String>) _val;
			if(refMap!=null && refMap.containsKey(REF_BY_KEY)) {
				String keyRef = refMap.get(REF_BY_KEY);
				Object refObj = beanScopeFactory.getBeanObject(keyRef);
				setPropertyPath(object, keyPoint, refObj, isDefault, isLogger);
				return (T) refObj;
			}
		}
		if(_val instanceof String) {
			String keyRef=_val.toString();
			Object refObj = beanScopeFactory.getBeanObject(keyRef);
			setPropertyPath(object, keyPoint, refObj, isDefault, isLogger);
			return (T) refObj;
		}
		return null;
	}

	public static <T> T setPropertyPath(Object instance, String keyPoint, Object _val, BeanDefinition beanDefinition,
			ModelPropertyDiffination setterMeta) {
		switch (setterMeta.getAccess()) {
		case AUTO:
			return PropertyAccessorUtil.setProperty(instance, keyPoint, ReflectionAccess.PRIVATE, _val);
		case WRITE_ONLY:
			return PropertyAccessorUtil.setProperty(instance, keyPoint, ReflectionAccess.PRIVATE, _val);
		case READ_WRITE:
			return PropertyAccessorUtil.setProperty(instance, keyPoint, ReflectionAccess.PRIVATE, _val);
		default:
			Assertion.state(false, "Can't write '" + keyPoint + "' for bean " + beanDefinition.getId() + ". Model '"
					+ setterMeta.getOwner().getId() + "' is protected to write.");
			return null;
		}
	}

	public static Map<String, ?> setPropertiesPath(Object instance, Map<String, Object> _properties, boolean isDefault,
			boolean isLogger) {
		Assertion.notNull(_properties, "Properties should not be null.");
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (String _key : _properties.keySet()) {
			returnMap.put(_key, setPropertyPath(instance, _key, _properties.get(_key), isDefault, isLogger));
		}
		return returnMap;
	}

	public static Map<String, ?> setPropertiesPath(BeanScopeFactory<String, BeanScope> beanScopeFactory, Object instance,
			Map<String, Object> _properties, boolean isDefault, boolean isLogger) {
		Assertion.notNull(_properties, "Properties should not be null.");
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (String _key : _properties.keySet()) {
			returnMap.put(_key, setPropertyPath(beanScopeFactory, instance, _key, _properties.get(_key), isDefault, isLogger));
		}
		return returnMap;
	}

	public static Map<String, ?> setPropertiesPath(Object instance, String[] _keyPaths, Object[] _values,
			boolean isDefault, boolean isLogger) {
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
		for (int index = 0; index < _keyPaths.length; index++) {
			String _key = _keyPaths[index];
			Object _value = setPropertyPath(instance, _key, _values[index], isDefault, isLogger);
			returnMap.put(_key, _value);
		}
		return returnMap;
	}

	public static Map<String, ?> setPropertiesPath(Object instance, String _keyPath, Object[] _values,
			boolean isDefault, boolean isLogger) {
		return setPropertiesPath(instance, _keyPath.split("~"), _values, isDefault, isLogger);
	}

	public static Map<String, ?> setSafePropertiesPath(Object instance, String _keyPath, Object[] _values,
			boolean isDefault, boolean isLogger) {
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		String keyArray[] = _keyPath.split("~");
		for (int index = 0; index < keyArray.length; index++) {
			String _key = keyArray[index];
			if (containsKeyPath(instance, _key, isDefault, isLogger)) {
				returnMap.put(_key, setPropertyPath(instance, _key, _values[index], isDefault, isLogger));
			}
		}
		return returnMap;
	}

}
