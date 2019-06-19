package org.brijframework.data.factories;

import java.util.List;

import org.brijframework.data.info.ClassDataInfo;

public interface ClassDataInfoFactory<T extends ClassDataInfo> extends DataInfoFactory<T>{

	List<T> getDataList(String model);

	T register(ClassDataInfo dataSetup);

}
