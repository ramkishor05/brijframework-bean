package org.brijframework.bean;

import java.util.Map;

import org.brijframework.GenericBean;
import org.brijframework.bean.util.BeanObjectUtil;
import org.brijframework.util.printer.GraphPrinter;

/***
 * 
 * @author Ram Kishor
 *
 */
public interface BeanObject extends GenericBean {

	public default Object getCurrentInstance() {
		return this;
	}
	/***
	 * this provides us to set value of key for object.
	 * @param _key
	 * @param _value
	 * @return
	 */
	public default <T> T setProperty(String _keyPath, T _value) {
		return BeanObjectUtil.setPropertyPath(getCurrentInstance(),_keyPath, _value,true);
	};

	/****
	 * this provides us to get value of key for object.
	 * @param _key
	 * @return
	 */
	public  default <T> T getProperty(String _keyPath) {
		return BeanObjectUtil.getPropertyPath(getCurrentInstance(),_keyPath,false);
	}
	
	/***
	 * this provides us to check key which is contains or not for object.
	 * @param _key
	 * @return
	 */
	public default Boolean containsKey(String _keyPath) {
		return BeanObjectUtil.containsPathKey(getCurrentInstance(),_keyPath,false);
	}

	/****
	 * this provides us to check value of key which is contains or not for object.
	 * @param _key
	 * @return
	 */
	public default Boolean containsValue(String _keyPath) {
		return BeanObjectUtil.containsPathValue(getCurrentInstance(),_keyPath,false);
	}
	
	/***
	 * this provides us to get type of key for object.
	 * @param _key
	 * @return
	 */
	public default Class<?> typeOfProperty(String _keyPath){
		return BeanObjectUtil.typeOfPropertyPath(getCurrentInstance(),_keyPath,false);
	}
	
	/***
	 * this provides us to set values of keys for object.
	 * 
	 * @param _keys
	 * @param _values
	 * @return
	 */
	public default Map<String, ?> setProperties(String[] _keyPaths, Object... _values){
		return BeanObjectUtil.setPropertiesPath(getCurrentInstance(),_keyPaths,_values,false);
	}

	/***
	 * this provides us to set values of keys for object.
	 * 
	 * @param _keys
	 * @param _values
	 * @return
	 */
	public default Map<String, ?> setProperties(String _keys, Object... _values){
		return BeanObjectUtil.setPropertiesPath(getCurrentInstance(),_keys,_values,true);
	}

	/***
	 * this provides us to set values of keys which is contains or not for object.
	 * 
	 * @param _keys
	 * @param _values
	 * @return
	 */
	public default Map<String, ?> setSafeProperties(String _keyPath, Object... _values){
		return BeanObjectUtil.setSafePropertiesPath(getCurrentInstance(),_keyPath,_values,true);
	}

	/***
	 * this provides us to set values of keys for object.
	 * 
	 * @param _properties
	 * @return
	 */
	public default Map<String, ?> setProperties(Map<String, Object> _properties){
		return BeanObjectUtil.setPropertiesPath(getCurrentInstance(),_properties,true);
	}
	
	/***
	 * this provides us to fill values of keys for object.
	 * 
	 * @param _properties
	 * @return
	 */
	public default void fillProperties(Map<String, Object> _properties) {
		_properties.putAll(BeanObjectUtil.setPropertiesPath(getCurrentInstance(),_properties,true));
	}

	/***
	 * this provides us to get properties for object.
	 * @return
	 */
	public default Map<String, ?> getProperties(){
		return BeanObjectUtil.getPropertiesPath(getCurrentInstance(),false);
	}

	/***
	 * this provides us to get properties for object.
	 * @param _keys
	 * @return
	 */
	public default Map<String, ?> getProperties(String... _keyPath){
		return BeanObjectUtil.getPropertiesPath(getCurrentInstance(),_keyPath,false);
	}

	/***
	 * this provides us to get safe properties for object.
	 * @param _keys
	 * @return
	 */
	public default Map<String, ?> getSafeProperties(String... _keyPath){
		return BeanObjectUtil.getSafeProperties(getCurrentInstance(),_keyPath,false);
	}

	/***
	 * this provides us to check keys for object which is cantains or not.
	 * @param _keys
	 * @return
	 */
	public default Map<String, Boolean> containsProperties(String _keyPath){
		return BeanObjectUtil.containsPropertiesPath(getCurrentInstance(),_keyPath,false);
	}
	
	public default void printObject() {
		GraphPrinter.getPrinter(this).printToScreen();
	}

}
