package org.brijframework.data.factories;

import org.brijframework.data.DataInfo;
import org.brijframework.factories.Factory;

public interface DataInfoFactory< T extends DataInfo> extends Factory{

	public  T register(T datainfo);

	public  T getData(String modelKey);

}
