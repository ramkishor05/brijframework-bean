package org.brijframework.data.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.brijframework.meta.factories.asm.PropertyMetaFactoryImpl;
import org.brijframework.meta.impl.PropertyMeta;
import org.brijframework.meta.util.MetaBuilderUtil;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.casting.CastingUtil;
import org.brijframework.util.meta.PointUtil;
import org.brijframework.util.reflect.ClassUtil;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.support.Access;
import org.brijframework.util.support.Constants;

public class DataBuilderUtil {
	/**
	 * Find implement class from given class for create object
	 * 
	 * @param field
	 * @param type
	 * @return Class
	 */
	public static Class<?> getTargetClass(Field field, Class<?> type) {
		if (type == null) {
			return null;
		}
		if (!type.getName().equals(Type.class.getName())
				&& !(type.isInterface() || Modifier.isAbstract(type.getModifiers()))) {
			return type;
		}
		if (Map.class.isAssignableFrom(type)) {
			return HashMap.class;
		} else if (List.class.isAssignableFrom(type)) {
			return ArrayList.class;
		} else if (Set.class.isAssignableFrom(type)) {
			return HashSet.class;
		} else if (Collection.class.isAssignableFrom(type)) {
			return ArrayList.class;
		}
		if (type.getName().equals(Type.class.getName())) {
			return field.getType();
		}
		return type;
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
	private static Object findCurrentObject(Object instance, Integer index, Field field, boolean isDefault) {
		Object paramObject = getPropertyArray(instance, index, Access.PRIVATE);
		if (paramObject == null && isDefault) {
			Class<?> paramClass = ClassUtil.collectionParamType(field);
			paramObject = InstanceUtil.getInstance(paramClass);
			setPropertyArray(instance, index, Access.PRIVATE, paramObject);
		}
		return paramObject;
	}

	/***
	 * Find collection object
	 * 
	 * @param instance
	 * @param key
	 * @param index
	 * @param isDefault
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T findCurrentFromList(Object instance, String key, Integer index, boolean isDefault) {
		Assertion.notEmpty(key, "Key should not be null or empty");
		PropertyMeta property = PropertyMetaFactoryImpl.getFactory()
				.getPropertyInfo(instance.getClass().getSimpleName() + "_" + key);
		Field field = property != null ? property.getTargetAsField()
				: FieldUtil.getField(instance.getClass(), key, Access.PRIVATE);
		Class<?> targetClass = property != null ? getTargetClass(field, property.getType())
				: getTargetClass(field, field.getType());
		Object collection = PropertyAccessorUtil.getProperty(instance, key, Access.PRIVATE);
		if (collection == null && targetClass != null && isDefault) {
			collection = InstanceUtil.getInstance(targetClass);
			PropertyAccessorUtil.setProperty(instance, field, collection);
		}
		if (collection == null) {
			return null;
		}
		return (T) findCurrentObject(collection, index, field, isDefault);
	}

	@SuppressWarnings("unchecked")
	private static <T> T findCurrentFromObject(Object instance, String _keyPath, boolean isDefault) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		PropertyMeta property = PropertyMetaFactoryImpl.getFactory()
				.getPropertyInfo(instance.getClass().getSimpleName() + "_" + _keyPath);
		Field field = property != null ? property.getTargetAsField()
				: FieldUtil.getField(instance.getClass(), _keyPath, Access.PRIVATE);
		Object _value = PropertyAccessorUtil.getProperty(instance, field, Access.PRIVATE);
		if (_value == null && isDefault) {
			Class<?> targetClass = property != null ? getTargetClass(field, property.getType())
					: getTargetClass(field, field.getType());
			_value = InstanceUtil.getInstance(targetClass);
			PropertyAccessorUtil.setProperty(instance, field, _value);
		}
		return (T) _value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T getCurrentObject(Object instance, String[] keyArray, boolean isDefault) {
		Object current = instance;
		StringBuffer point = new StringBuffer();
		Field field = null;
		for (int i = 0; i < keyArray.length - 1; i++) {
			String key = keyArray[i];
			if (key.contains(Constants.OPEN_BRAKET) && key.contains(Constants.CLOSE_BRAKET)) {
				current = findCurrentFromList(current, PointUtil.keyArray(key), PointUtil.indexArray(key), isDefault);
			} else if (current instanceof Map) {
				current = findCurrentFromMap((Map) current, key, field, isDefault);
			} else {
				field = FieldUtil.getField(current.getClass(), key, Access.PRIVATE);
				current = findCurrentFromObject(current, key, isDefault);
			}
			point.append(key);
			if (i < keyArray.length - 2) {
				point.append(".");
			}
			if (current == null) {
				Assertion.notEmpty(current, point.toString() + " should not be null or empty");
			}
		}
		return (T) current;
	}

	private static Object findCurrentFromMap(Map<Object, Object> current, String key, Field field, boolean isDefault) {
		ParameterizedType type = (ParameterizedType) field.getGenericType();
		Class<?> keyClass = (Class<?>) type.getActualTypeArguments()[0];
		Object keyObject = CastingUtil.castObject(key, keyClass);
		Class<?> valueClass = (Class<?>) type.getActualTypeArguments()[1];
		if (!current.containsKey(keyObject)) {
			current.put(keyObject, InstanceUtil.getInstance(valueClass));
		}
		return current.get(keyObject);
	}

	@SuppressWarnings("unchecked")
	public static <T> T setPropertyPath(Object instance, String _keyPath, Object _value, boolean isDefault) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		String[] keyArray = _keyPath.split(Constants.SPLIT_DOT);
		Object current = getCurrentObject(instance, keyArray, isDefault);
		if (current != null) {
			return setProperty(current, Access.PRIVATE, keyArray[keyArray.length - 1], _value);
		}
		return (T) _value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T setProperty(Object current, Access access, String key, Object value) {
		if (current instanceof Collection<?> && key.contains(Constants.OPEN_BRAKET)
				&& key.contains(Constants.CLOSE_BRAKET)) {
			return getPropertyArray(current, PointUtil.indexArray(key), access);
		} else if (current instanceof Map) {
			return setPropertyMap((Map<String, Object>) current, key, value);
		} else {
			return setPropertyObject(current, access, key, value);
		}
	}

	public static <T> T getPropertyPath(Object instance, String _keyPath, boolean isDefault) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		String[] keyArray = _keyPath.split(Constants.SPLIT_DOT);
		Object current = getCurrentObject(instance, keyArray, isDefault);
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
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Object[] array, int index, Access access) {
		if (array == null || (array.length <= index)) {
			return null;
		}
		return (T) array[index];
	}

	@SuppressWarnings("unchecked")
	public static <T> T getProperty(List<?> array, int index, Access access) {
		if (array == null) {
			return null;
		}
		if (index < 0 || index >= array.size()) {
			return null;
		}
		return (T) array.get(index);
	}

	public static <T> T getPropertyArray(Object current, int index, Access access) {
		if (current instanceof Object[]) {
			return getProperty((Object[]) current, index, access);
		} else if (current instanceof List) {
			return getProperty((List<?>) current, index, access);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getPropertyArray(Object[] array, int index, Access access, Object value) {
		if (index < 0 && array == null || array.length <= index) {
			return null;
		}
		array[index] = value;
		return (T) value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T setProperty(List<Object> array, int index, Access access, Object value) {
		if (array == null) {
			return null;
		}
		array.add(index, value);
		return (T) value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T setPropertyArray(Object current, int index, Access access, Object value) {
		if (current instanceof Object[]) {
			return getPropertyArray((Object[]) current, index, access, value);
		} else if (current instanceof List) {
			return setProperty((List<Object>) current, index, access, value);
		}
		return null;
	}

	/* End list */

	/*
	 * Start Map
	 */

	@SuppressWarnings("unchecked")
	private static <T> T setPropertyMap(Map<String, Object> current, String key, Object value) {
		return (T) current.put(key, value);
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
	public static <T> T setPropertyObject(Object current, Access access, String keyPoint, Object _value) {
		return PropertyAccessorUtil.setProperty(current, keyPoint, access, _value);
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
				returnMap.put(_key, setProperty(currentInstance, Access.PRIVATE, _key, _values[index]));
			}
		}
		return returnMap;
	}

	public static Map<String, ?> setPropertiesPath(Object currentInstance, Map<String, Object> _properties,
			boolean isDefault) {
		Map<String, ?> returnMap = new LinkedHashMap<String, Object>();
		for (String _key : _properties.keySet()) {
			returnMap.put(_key, setProperty(currentInstance, Access.PRIVATE, _key, _properties.get(_key)));
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

	/*
	 * 
	 * public static Class<?> getCurrentClass(Class<?> cls, String _keyPath) {
	 * String[] keyArray=_keyPath.split(Constants.SPLIT_DOT); Class<?> keyClass=cls;
	 * for (int i = 0; i < keyArray.length-1; i++) { String key = keyArray[i];
	 * keyClass=getProperty(keyClass, key); } return keyClass; }
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T getCurrentInstance(Object
	 * object,String _keyPath, boolean isDefault) { if(object instanceof Map) {
	 * return getCurrentFromMapped((Map<String, Object> )object,_keyPath,isDefault);
	 * }else { return getCurrentFromInstance(object, _keyPath,isDefault); } }
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T getCurrentInstance(Object
	 * object,Access access, String _keyPath) { if(object instanceof Map) { return
	 * getCurrentFromMapped((Map<String, Object> )object, access,_keyPath); }else {
	 * return getCurrentFromInstance(object,access, _keyPath); } }
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T
	 * getCurrentFromInstance(Object object,Access access,String _keyPath) {
	 * String[] keyArray=_keyPath.split(Constants.SPLIT_DOT); Object current=object;
	 * for (int i = 0; i < keyArray.length-1; i++) { String key = keyArray[i];
	 * current=getProperty(current, access,key); } return (T) current; }
	 * 
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T
	 * getCurrentFromInstance(Object object,String _keyPath, boolean isDefault) {
	 * String[] keyArray=_keyPath.split(Constants.SPLIT_DOT); Object current=object;
	 * StringBuffer point=new StringBuffer(); for (int i = 0; i < keyArray.length-1;
	 * i++) { String key = keyArray[i]; if(key.contains(Constants.OPEN_BRAKET)) {
	 * key=key.split("\\"+Constants.OPEN_BRAKET)[0]; } Object
	 * temp=getProperty(current, Access.PRIVATE,key); if(temp==null && isDefault) {
	 * Class<?> cls=getProperty(current.getClass(),key);
	 * temp=InstanceUtil.getInstance(cls); setProperty(current, Access.PRIVATE, key,
	 * temp); } if(i < keyArray.length-2) { point.append("."); } current=temp; }
	 * if(_keyPath.endsWith(Constants.CLOSE_BRAKET)) { String key = _keyPath;
	 * if(_keyPath.contains(Constants.OPEN_BRAKET)) {
	 * key=_keyPath.split("\\"+Constants.OPEN_BRAKET)[0]; }
	 * current=getProperty(current, Access.PRIVATE,key); } return (T) current; }
	 * 
	 * 
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T
	 * getCurrentFromMapped(Map<String, Object> object,String _keyPath, boolean
	 * isDefault) { String[] keyArray=_keyPath.split(Constants.SPLIT_DOT); Object
	 * current=object; for (int i = 0; i < keyArray.length-1; i++) { String key =
	 * keyArray[i]; PropertyMeta property=
	 * PropertyMetaFactoryImpl.getFactory().getPropertyInfo(current.getClass().
	 * getSimpleName(),key); Access access = property!=null
	 * ?property.getAccess():Access.PUBLIC; current=getProperty(current,access,key);
	 * } return (T) current; }
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T
	 * getSupperFromMapped(Map<String, Object> object,String _keyPath, boolean
	 * isDefault) { String[] keyArray=_keyPath.split(Constants.SPLIT_DOT); Object
	 * current=object; for (int i = 0; i < keyArray.length-2; i++) { String key =
	 * keyArray[i]; PropertyMeta property=
	 * PropertyMetaFactoryImpl.getFactory().getPropertyInfo(current.getClass().
	 * getSimpleName(),key); Access access = property!=null
	 * ?property.getAccess():Access.PUBLIC; current=getProperty(current,access,key);
	 * } return (T) current; }
	 * 
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T
	 * getCurrentFromMapped(Map<String, Object> object,Access access,String
	 * _keyPath) { String[] keyArray=_keyPath.split(Constants.SPLIT_DOT); Object
	 * current=object; for (int i = 0; i < keyArray.length-1; i++) { String key =
	 * keyArray[i]; current=getProperty(current,access,key); } return (T) current; }
	 * 
	 * public static Class<?> getProperty(Class<?> _class, String _field) {
	 * if(Map.class.isAssignableFrom(_class)) { return HashMap.class; }else
	 * if(List.class.isAssignableFrom(_class)) { return ArrayList.class; }else
	 * if(Set.class.isAssignableFrom(_class)) { return HashSet.class; }else
	 * if(Collection.class.isAssignableFrom(_class)) { return ArrayList.class; }else
	 * { Field field=FieldUtil.getField(_class, _field, Access.PRIVATE);
	 * if(field==null) { return null; } PropertyMeta property=
	 * PropertyMetaFactoryImpl.getFactory().getPropertyInfo(_class.getSimpleName(),
	 * _field); if(property!=null && property.getType()!=null &&
	 * !property.getType().equals(Type.class)) { return property.getType(); }
	 * if(Map.class.isAssignableFrom(field.getType())) { return HashMap.class; }else
	 * if(List.class.isAssignableFrom(field.getType())) { return ArrayList.class;
	 * }else if(Set.class.isAssignableFrom(field.getType())) { return HashSet.class;
	 * }else if(Collection.class.isAssignableFrom(field.getType())) { return
	 * ArrayList.class; } return field.getType(); } }
	 * 
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T getProperty(Object
	 * current,Access access, String key) { if(current instanceof Object[]) { return
	 * getPropertyArray((Object[]) current,access,key); }else if(current instanceof
	 * Collection) { return getPropertyCollection((Collection<?>)
	 * current,access,key); }else if(current instanceof Map) { return
	 * getPropertyMap((Map<String, Object>) current,key); }else { return
	 * getPropertyObject(current,access, key); } }
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T setProperty(Object
	 * current,Access access, String key,Object value) { if(current instanceof
	 * Object[]) { return setPropertyArray((Object[]) current,access,key,value);
	 * }else if(current instanceof Collection) { return
	 * setPropertyCollection((Collection<?>) current,access,key,value); }else
	 * if(current instanceof Map) { return setPropertyMap((Map<String, Object>)
	 * current,key,value); }else { return setPropertyObject(current,access,
	 * key,value); } }
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T setProperty(Object
	 * superInstance,Object[] array,Integer index,Access access,Object value) {
	 * if(array.length<=index) { return null; } array[index]=value; return (T)
	 * value; }
	 * 
	 * public static <T> T setProperty(Object superInstance,Object[] array,Integer
	 * index,Access access,String key,Object value) { if(array.length<=index) {
	 * return null; } return setProperty(array[index],access,key,value); }
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T setProperty(Object
	 * superInstance,Set<Object> array,Integer index, Access access, String
	 * key,Object value) { int count=-1; for (Iterator<?> iterator =
	 * array.iterator(); iterator.hasNext();) { if(++count==index) {
	 * setProperty(iterator.next(), access, key, value); } } return (T) value; }
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T setProperty(Object
	 * superInstance,List<Object> array,Integer index, Access access, String
	 * key,Object value) { if(array.size()>index) { setProperty(array.get(index),
	 * access, key, value); }else { } return (T) value; }
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T setProperty(Object
	 * superInstance,Object current,Integer index,Access access, String key,Object
	 * value) { if(current instanceof Object[]) { if(key==null) { return
	 * setProperty(superInstance,(Object[]) current, index, access, value); }else {
	 * return setProperty(superInstance,(Object[]) current, index, access, key,
	 * value); } }else if(current instanceof List) { return
	 * setProperty(superInstance,(List<Object>) current, index, access, key, value);
	 * }else if(current instanceof Set) { return
	 * setProperty(superInstance,(Set<Object>) current, index, access, key, value);
	 * } return null; }
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T setProperty(Object
	 * current, String key,Access access,Object value) { if(current instanceof
	 * Object[]) { return setPropertyArray((Object[]) current,access, key,value);
	 * }else if(current instanceof Collection) { return
	 * setPropertyCollection((Collection<?>) current,access,key,value); }else
	 * if(current instanceof Map) { return setPropertyMap((Map<String, Object>)
	 * current,key,value); }else { return setPropertyObject(current, access, key,
	 * value); } }
	 * 
	 * 
	 * @SuppressWarnings("unchecked") private static <T> T setPropertyArray(Object[]
	 * current, Access access, String key, Object value) { Collection<Object>
	 * objects=new ArrayList<>(); for(Object object:current){
	 * objects.add(setProperty(object,access, key,value)); }; return (T) objects; }
	 * 
	 * @SuppressWarnings("unchecked") private static <T> T
	 * setPropertyCollection(Collection<?> current,Access access, String key, Object
	 * value) { Collection<Object> objects=new ArrayList<>();
	 * current.forEach(object->{ objects.add(setProperty(object,access, key,value));
	 * }); return (T) objects; }
	 * 
	 * @SuppressWarnings("unchecked") private static <T> T
	 * setPropertyMap(Map<String, Object> current, String key, Object value) {
	 * return (T) current.put(key, value); }
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T getPropertyMap(Map<String,
	 * Object> current,String key ) { return (T) current.get(key); }
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T
	 * getPropertyCollection(Collection<?> collection,Access access,String key ) {
	 * Collection<Object> objects=new ArrayList<>(); collection.forEach(object->{
	 * objects.add(getProperty(object,access, key)); }); return (T) objects; }
	 * 
	 * @SuppressWarnings("unchecked") public static <T> T getPropertyArray(Object[]
	 * collection,Access access,String key ) { Collection<Object> objects=new
	 * ArrayList<>(); for(Object object:collection){
	 * objects.add(getProperty(object,access, key)); }; return (T)
	 * objects.toArray(); }
	 * 
	 * public static <T> T getPropertyObject(Object object,Access access,String key
	 * ) { return PropertyAccessorUtil.getProperty(object, key,access); }
	 * 
	 * public static <T> T setPropertyObject(Object current,Access access, String
	 * keyPoint, Object _value) { return PropertyAccessorUtil.setProperty(current,
	 * keyPoint,access,_value); }
	 * 
	 */

}
