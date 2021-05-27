package org.brijframework.bean.util;

public class PropertyObject {

	private Object object;
	private String property;
	private DataType dataType;
	private int index;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public static enum DataType {
		ARRAY, LIST, SET, MAP;
	}
}
