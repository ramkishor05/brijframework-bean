package org.brijframework.bean.factories.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.brijframework.bean.config.impl.ResourcesBeanConfig;
import org.brijframework.bean.constant.BeanConstants;
import org.brijframework.bean.factories.impl.BeanSetupFactoryImpl;
import org.brijframework.bean.setup.impl.BeanSetupImpl;
import org.brijframework.resources.factory.json.JsonResourceFactory;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.InstanceUtil;
import org.json.JSONException;

public class JsonBeanSetupFactory  extends BeanSetupFactoryImpl{

	private static JsonBeanSetupFactory factory;

	@Assignable
	public static JsonBeanSetupFactory getFactory() {
		if(factory==null) {
			factory=new JsonBeanSetupFactory();
		}
		return factory;
	}
	

	@SuppressWarnings("unchecked")
	public List<ResourcesBeanConfig> configration() {
		Object resources=getContainer().getContext().getProperties().get(BeanConstants.APPLICATION_BOOTSTRAP_CONFIG_BEANS_JSON_LOCATION);
		if (resources==null) {
			System.err.println("Bean configration not found :"+BeanConstants.APPLICATION_BOOTSTRAP_CONFIG_BEANS_JSON_LOCATION);
			return null;
		}
		System.err.println("Bean configration found :"+BeanConstants.APPLICATION_BOOTSTRAP_CONFIG_BEANS_JSON_LOCATION);
		if(resources instanceof List) {
			return build((List<Map<String, Object>>)resources);
		}else if(resources instanceof Map) {
			return build((Map<String, Object>)resources);
		}else {
			Map<String,Object> resourcesMap=new HashMap<>();
			resourcesMap.put("location", resources);
			resourcesMap.put("enable", getContainer().getContext().getProperties().get(BeanConstants.APPLICATION_BOOTSTRAP_CONFIG_BEANS_JSON_ENABLE));
			System.err.println("Invalid bean configration : "+resources);
			return build(resourcesMap);
		}
	}
	
	private List<ResourcesBeanConfig> build(Map<String, Object> resource) {
		List<ResourcesBeanConfig> configs=new ArrayList<ResourcesBeanConfig>();
		configs.add(InstanceUtil.getInstance(ResourcesBeanConfig.class, resource));
		return configs;
	}

	private List<ResourcesBeanConfig> build(List<Map<String, Object>> resources) {
		List<ResourcesBeanConfig> configs=new ArrayList<ResourcesBeanConfig>();
		for(Map<String, Object> resource:resources) {
			configs.add(InstanceUtil.getInstance(ResourcesBeanConfig.class, resource));
		}
		return configs;
	}
	
	
	@Override
	public JsonBeanSetupFactory loadFactory() {
		List<ResourcesBeanConfig> configs=configration();
		if(configs==null) {
			System.err.println("Invalid model configration : "+configs);
			return this;
		}
		for(ResourcesBeanConfig modelConfig:configs) {
			if(!modelConfig.isEnable()) {
				System.err.println("Bean configration disabled found :"+modelConfig.getLocation());
			
			}
			Collection<JsonResource> resources=JsonResourceFactory.factory().getResources(modelConfig.getLocation());
			for(JsonResource resource:resources) {
				if(resource.isJsonList()) {
				   try {
					  register(resource.toObjectList());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if(resource.isJsonObject()) {
					try {
						register(resource.toObjectMap());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public void register(List<Object> resources) {
		Assertion.notNull(resources, "Invalid resources");
		for(Object object:resources) {
			if(object instanceof Map) {
				register((Map<String, Object>)object);
			}
		}
	}

	public void register(Map<String, Object> resourceMap) {
		Assertion.notNull(resourceMap, "Invalid target :"+resourceMap);
		BeanSetupImpl metaSetup=InstanceUtil.getInstance(BeanSetupImpl.class,resourceMap);
		this.register(metaSetup);
	}
}
