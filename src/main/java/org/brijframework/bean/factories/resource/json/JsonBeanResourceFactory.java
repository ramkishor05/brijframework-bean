package org.brijframework.bean.factories.resource.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.brijframework.bean.config.impl.BeanConfigration;
import org.brijframework.bean.constant.BeanConstants;
import org.brijframework.bean.factories.resource.asm.AbstractBeanResourceFactory;
import org.brijframework.bean.resource.impl.BeanResourceImpl;
import org.brijframework.model.factories.resource.impl.DefaultTypeModelResourceFactory;
import org.brijframework.resources.factory.json.JsonResourceFactory;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.support.enums.Scope;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.support.ordering.OrderOn;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.printer.LoggerConsole;
import org.brijframework.util.reflect.ClassUtil;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.text.StringUtil;
import org.json.JSONException;

@OrderOn(2)
public class JsonBeanResourceFactory  extends AbstractBeanResourceFactory{

	private static JsonBeanResourceFactory factory;

	@SingletonFactory
	public static JsonBeanResourceFactory getFactory() {
		if(factory==null) {
			factory=new JsonBeanResourceFactory();
		}
		return factory;
	}

	@SuppressWarnings("unchecked")
	public List<BeanConfigration> configration() {
		Object resources=getEnvProperty(BeanConstants.APPLICATION_CONFIG_BEANS);
		if (resources==null) {
			LoggerConsole.screen("BeanConfigration","Bean configration not found :"+BeanConstants.APPLICATION_CONFIG_BEANS);
			return null;
		}
		LoggerConsole.screen("BeanConfigration","Bean configration found :"+BeanConstants.APPLICATION_CONFIG_BEANS);
		if(resources instanceof List) {
			return build((List<Map<String, Object>>)resources);
		}else if(resources instanceof Map) {
			return build((Map<String, Object>)resources);
		}else {
			Map<String,Object> resourcesMap=new HashMap<>();
			resourcesMap.put("location", resources);
			resourcesMap.put("enable", getEnvProperty(BeanConstants.APPLICATION_CONFIG_BEANS_ENABLE));
			return build(resourcesMap);
		}
	}
	
	private List<BeanConfigration> build(Map<String, Object> resource) {
		List<BeanConfigration> configs=new ArrayList<BeanConfigration>();
		configs.add(InstanceUtil.getInstance(BeanConfigration.class, resource));
		return configs;
	}

	private List<BeanConfigration> build(List<Map<String, Object>> resources) {
		List<BeanConfigration> configs=new ArrayList<BeanConfigration>();
		for(Map<String, Object> resource:resources) {
			configs.add(InstanceUtil.getInstance(BeanConfigration.class, resource));
		}
		return configs;
	}
	
	
	@Override
	public JsonBeanResourceFactory loadFactory() {
		List<BeanConfigration> configs=configration();
		if(configs==null) {
			LoggerConsole.screen("BeanConfigration","Invalid bean configration : "+configs);
			return this;
		}
		for(BeanConfigration modelConfig:configs) {
			if(!modelConfig.isEnable()) {
				LoggerConsole.screen("BeanConfigration","Bean configration disabled found :"+modelConfig.getLocation());
			}
			Collection<JsonResource> resources=JsonResourceFactory.factory().getResources(modelConfig.getLocation());
			registerResource(resources);
		}
		return this;
	}

	private void registerResource(Collection<JsonResource> resources) {
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
	
	@SuppressWarnings("unchecked")
	public void register(List<Object> resources) {
		Assertion.notNull(resources, "Invalid resources");
		for(Object object:resources) {
			if(object instanceof Map) {
				register((Map<String, Object>)object);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void register(Map<String, Object> resourceMap) {
		Assertion.notNull(resourceMap, "Invalid resource :"+resourceMap);
		BeanResourceImpl beanResource=InstanceUtil.getInstance(BeanResourceImpl.class, resourceMap);
		String id=(String) resourceMap.remove("id");
		String type=(String) resourceMap.remove("type");
		String model=(String) resourceMap.remove("model");
		String name=(String) resourceMap.remove("name");
		String scope=(String) resourceMap.get("scope");
		String factoryClass=(String) resourceMap.get("factory-class");
		String factoryMethod=(String) resourceMap.get("factory-method");
		if(StringUtil.isEmpty(scope)) {
			scope=Scope.SINGLETON.toString();
		}
		Assertion.isTrue(StringUtil.isEmpty(type)&& StringUtil.isEmpty(model), "Invalid type or model for BeanResource");
		if(StringUtil.isNonEmpty(type)) {
			Class<?> typeClass=ClassUtil.getClass(type);
			Assertion.notEmpty(typeClass, "Not found type for BeanResource");
			if(StringUtil.isEmpty(name)) {
				name=typeClass.getSimpleName();
			}
			if(StringUtil.isEmpty(model)) {
				model=typeClass.getSimpleName();
				DefaultTypeModelResourceFactory.getFactory().createOrload(typeClass);
			}
		}
		Assertion.notEmpty(id, "Invalid id for BeanResource");
		beanResource.setId(id);
		beanResource.setName(name);
		beanResource.setScope(scope);
		beanResource.setType(type);
		beanResource.setModel(model);
		beanResource.setFactoryClass(factoryClass);
		beanResource.setFactoryMethod(factoryMethod);
	    beanResource.getProperties().putAll((Map<? extends String, ? extends Object>)resourceMap.get("properties"));
		this.register(beanResource.getId(),beanResource);
	}
}
