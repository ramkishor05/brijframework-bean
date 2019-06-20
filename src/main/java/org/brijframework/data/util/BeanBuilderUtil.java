package org.brijframework.data.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.brijframework.meta.factories.asm.ClassMetaInfoFactoryImpl;
import org.brijframework.meta.info.FieldMetaInfo;
import org.brijframework.meta.util.MetaBuilderUtil;
import org.brijframework.util.accessor.MetaAccessorUtil;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.casting.CastingUtil;
import org.brijframework.util.meta.PointUtil;
import org.brijframework.util.reflect.ClassUtil;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.support.Access;
import org.brijframework.util.support.Constants;

public class BeanBuilderUtil {
	
	private static final String KEY = "KEY";
	private static final String VAL = "VAL";

	@SuppressWarnings("unchecked")
	private static <T> T findCurrentFromObject(Object instance, String _keyPath, boolean isDefault) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		FieldMetaInfo property =ClassMetaInfoFactoryImpl.getFactory().getFieldMeta(instance.getClass().getSimpleName() , _keyPath);
		Field field = property != null ? property.getTargetAsField(): FieldUtil.getField(instance.getClass(), _keyPath, Access.PRIVATE);
		Object _value = PropertyAccessorUtil.getProperty(instance, field, Access.PRIVATE);
		if (_value == null && isDefault) {
			Class<?> targetClass = property != null ? CastingUtil.getTargetClass(field, property.getType()): CastingUtil.getTargetClass(field, field.getType());
			_value = InstanceUtil.getInstance(targetClass);
			PropertyAccessorUtil.setProperty(instance, field, _value);
		}
		return (T) _value;
	}

	private static Object findCurrentFromMap(Map<Object, Object> current, String key, AccessibleObject field, boolean isDefault) {
		ParameterizedType type = field instanceof Method? (ParameterizedType)((Method) field).getGenericReturnType():(ParameterizedType)((Field) field).getGenericType();
		Class<?> keyClass = (Class<?>) type.getActualTypeArguments()[0];
		Object keyObject = CastingUtil.castObject(key, keyClass);
		Class<?> valueClass = (Class<?>) type.getActualTypeArguments()[1];
		if (!current.containsKey(keyObject)) {
			current.put(keyObject, InstanceUtil.getInstance(valueClass));
		}
		Map<String,Class<?>> returnMap=new HashMap<>();
		returnMap.put(KEY, keyClass);
		returnMap.put(VAL, valueClass);
		return current.get(keyObject);
	}

	@SuppressWarnings("unchecked")
	public static <T> T setPropertyPath(Object instance, String _keyPath, Object _value, boolean isDefault) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		String[] keyArray = _keyPath.split(Constants.SPLIT_DOT);
		Object current = instance;
		StringBuffer point = new StringBuffer();
		AccessibleObject field = null;
		for (int i = 0; i < keyArray.length - 1; i++) {
			String key = keyArray[i];
			if (key.contains(Constants.OPEN_BRAKET) && key.contains(Constants.CLOSE_BRAKET)) {
				current = findCurrentFromList(current, PointUtil.keyArray(key), PointUtil.indexArray(key), isDefault);
			} else if (current instanceof Map) {
				current = findCurrentFromMap((Map<Object, Object>) current, key, field, isDefault);
			} else {
				field = MetaAccessorUtil.setPropertyMeta(current.getClass(), key, Access.PRIVATE,null);
				current = findCurrentFromObject(current, key, isDefault);
			}
			point.append(key);
			if (i < keyArray.length - 2) {
				point.append(Constants.DOT);
			}
			if (current == null) {
				Assertion.notEmpty(current, point.toString() + " should not be null or empty");
			}
		}
		if (current != null) {
			return setProperty(current, field,Access.PRIVATE, keyArray[keyArray.length - 1], _value);
		}
		return (T) _value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T setProperty(Object current,AccessibleObject field, Access access, String key, Object value) {
		if (current instanceof Collection<?> && key.contains(Constants.OPEN_BRAKET)&& key.contains(Constants.CLOSE_BRAKET)) {
			return setPropertyArray(current, field,PointUtil.indexArray(key),value);
		} else if (current instanceof Map) {
			return setPropertyMap((Map<Object, Object>) current,field, key, value);
		} else {
			return setPropertyObject(current, access,key, value);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getPropertyPath(Object instance, String _keyPath, boolean isDefault) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		String[] keyArray = _keyPath.split(Constants.SPLIT_DOT);
		Object current = instance;
		StringBuffer point = new StringBuffer();
		AccessibleObject field = null;
		for (int i = 0; i < keyArray.length - 1; i++) {
			String key = keyArray[i];
			if (key.contains(Constants.OPEN_BRAKET) && key.contains(Constants.CLOSE_BRAKET)) {
				current = findCurrentFromList(current, PointUtil.keyArray(key), PointUtil.indexArray(key), isDefault);
			} else if (current instanceof Map) {
				current = findCurrentFromMap((Map<Object, Object>) current, key, field, isDefault);
			} else {
				field = FieldUtil.getField(current.getClass(), key, Access.PRIVATE);
				current = findCurrentFromObject(current, key, isDefault);
			}
			point.append(key);
			if (i < keyArray.length - 2) {
				point.append(Constants.DOT);
			}
			if (current == null) {
				Assertion.notEmpty(current, point.toString() + " should not be null or empty");
			}
		}
		if (current != null) {
			return getProperty(current, Access.PRIVATE, keyArray[keyArray.length - 1]);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Object current, Access access, String key) {
		if (key.contains(Constants.OPEN_BRAKET) && key.contains(Constants.CLOSE_BRAKET)) {
			return findCurrentFromList(current, PointUtil.keyArray(key), PointUtil.indexArray(key), false);
		} else if (current instanceof Map) {
			return getPropertyMap((Map<String, Object>) current, key);
		} else {
			return getPropertyObject(current, access, key);
		}
	}

	/* Start list */

	/***
	 * Find collection object
	 * 
	 * @param instance
	 * @param _keyPath
	 * @param index
	 * @param isDefault
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T findCurrentFromList(Object instance, String _keyPath, Integer index, boolean isDefault) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		FieldMetaInfo property =ClassMetaInfoFactoryImpl.getFactory().getFieldMeta(instance.getClass().getSimpleName() , _keyPath);
		Field field = property != null ? property.getTargetAsField(): FieldUtil.getField(instance.getClass(), _keyPath, Access.PRIVATE);
		Class<?> targetClass = property != null ? CastingUtil.getTargetClass(field, property.getType()): CastingUtil.getTargetClass(field, field.getType());
		Object collection = PropertyAccessorUtil.getProperty(instance, _keyPath, Access.PRIVATE);
		if (collection == null && targetClass != null && isDefault) {
			collection = InstanceUtil.getInstance(targetClass);
			PropertyAccessorUtil.setProperty(instance, field, collection);
		}
		if (collection == null) {
			return null;
		}
		return (T) findCollectionObject(collection, index, field, isDefault);
	}
	
	/**
	 * Find current object for field if default is true than create object if its
	 * not found
	 * 
	 * @param instance
	 * @param index
	 * @param field
	 * @param isDefault
	 * @return
	 */
	private static Object findCollectionObject(Object instance, Integer index, AccessibleObject field, boolean isDefault) {
		Object paramObject = getPropertyArray(instance, index);
		if (paramObject == null && isDefault) {
			Class<?> paramClass = ClassUtil.collectionParamType(field);
			paramObject = InstanceUtil.getInstance(paramClass);
			setPropertyArray(instance,field, index, paramObject);
		}
		return paramObject;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Object[] array, int index) {
		if (array == null || (array.length <= index)) {
			return null;
		}
		return (T) array[index];
	}

	@SuppressWarnings("unchecked")
	public static <T> T getProperty(List<?> array, int index) {
		if (array == null) {
			return null;
		}
		if (index < 0 || index >= array.size()) {
			return null;
		}
		return (T) array.get(index);
	}

	public static <T> T getPropertyArray(Object current, int index) {
		if (current instanceof Object[]) {
			return getProperty((Object[]) current, index);
		} else if (current instanceof List) {
			return getProperty((List<?>) current, index);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getPropertyArray(Object[] array, int index, Object value) {
		if (index < 0 && array == null || array.length <= index) {
			return null;
		}
		array[index] = value;
		return (T) value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T setProperty(List<Object> array, int index, Object value) {
		if (array == null) {
			return null;
		}
		array.add(index, value);
		return (T) value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T setPropertyArray(Object current, AccessibleObject field,int index, Object value) {
		if (current instanceof Object[]) {
			return getPropertyArray((Object[]) current, index, value);
		} else if (current instanceof List) {
			return setProperty((List<Object>) current, index, value);
		}
		return null;
	}

	/* End list */

	/*
	 * Start Map
	 */

	@SuppressWarnings("unchecked")
	private static <T> T setPropertyMap(Map<Object, Object> current,AccessibleObject field, String key, Object value) {
		ParameterizedType type = field instanceof Method? (ParameterizedType)((Method) field).getGenericReturnType():(ParameterizedType)((Field) field).getGenericType();
		Class<?> keyClass = (Class<?>) type.getActualTypeArguments()[0];
		Object keyObject = CastingUtil.castObject(key, keyClass);
		//Class<?> valueClass = (Class<?>) type.getActualTypeArguments()[1];
		return (T) current.put(keyObject, value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getPropertyMap(Map<String, Object> current, String key) {
		return (T) current.get(key);
	}
	/*
	 * End map
	 */

	/*
	 * Start Object
	 */
	public static <T> T setPropertyObject(Object current,  Access access,String keyPoint, Object _value) {
		return PropertyAccessorUtil.setProperty(current, keyPoint,access, _value);
	}

	public static <T> T getPropertyObject(Object object, Access access, String key) {
		return PropertyAccessorUtil.getProperty(object, key, access);
	}

	public static Boolean containsPathKey(Object model, String _keyPath, boolean isDefault) {
		return MetaBuilderUtil.typeOfProperty(model.getClass(), _keyPath) != null;
	}

	public static Boolean containsPathValue(Object modelBean, String _keyPath, boolean isDefault) {
		return null;
	}

	public static Class<?> typeOfPropertyPath(Object currentInstance, String _keyPath, boolean isDefault) {
		return null;
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

	public static Map<String, ?> setPropertiesPath(Object currentInstance, String _keyPath, Object[] _values,
			boolean isDefault) {
		return setPropertiesPath(currentInstance, _keyPath.split("~"), _values, isDefault);
	}

	public static Map<String, ?> setSafePropertiesPath(Object currentInstance, String _keyPath, Object[] _values,
			boolean isDefault) {
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		String keyArray[] = _keyPath.split("~");
		for (int index = 0; index < keyArray.length; index++) {
			String _key = keyArray[index];
			if (containsPathKey(currentInstance, _key, isDefault)) {
				returnMap.put(_key, setPropertyPath(currentInstance, _key, _values[index],true));
			}
		}
		return returnMap;
	}

	public static Map<String, ?> setPropertiesPath(Object currentInstance, Map<String, Object> _properties,
			boolean isDefault) {
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (String _key : _properties.keySet()) {
			returnMap.put(_key, setPropertyPath(currentInstance, _key, _properties.get(_key),true));
		}
		return returnMap;
	}

	public static Map<String, ?> getPropertiesPath(Object currentInstance, boolean isDefault) {
		List<Field> fields = FieldUtil.getAllField(currentInstance.getClass(), Access.PRIVATE);
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (Field _key : fields) {
			returnMap.put(_key.getName(), getProperty(currentInstance, Access.PRIVATE, _key.getName()));
		}
		return returnMap;
	}

	public static Map<String, ?> getPropertiesPath(Object currentInstance, String[] _keyPath, boolean isDefault) {
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (String _key : _keyPath) {
			returnMap.put(_key, getProperty(currentInstance, Access.PRIVATE, _key));
		}
		return returnMap;
	}

	public static Map<String, ?> getSafeProperties(Object currentInstance, String[] _keyPath, boolean isDefault) {
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (String _key : _keyPath) {
			if (containsPathKey(currentInstance, _key, isDefault)) {
				returnMap.put(_key, getProperty(currentInstance, Access.PRIVATE, _key));
			}
		}
		return returnMap;
	}

	public static Map<String, Boolean> containsPropertiesPath(Object currentInstance, String _keyPath,
			boolean isDefault) {
		Map<String, Boolean> returnMap = new LinkedHashMap<String, Boolean>();
		String keyArray[] = _keyPath.split("~");
		for (int index = 0; index < keyArray.length; index++) {
			String _key = keyArray[index];
			returnMap.put(_key, containsPathKey(currentInstance, _key, isDefault));
		}
		return returnMap;
	}
	/*
	 * End Object
	 */


}
