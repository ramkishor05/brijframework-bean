package org.brijframework.bean.factories.asm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.factories.BeanScopeFactory;
import org.brijframework.bean.factories.definition.impl.BeanDefinitionFactoryImpl;
import org.brijframework.bean.factories.impl.BeanScopeFactoryImpl;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.bean.scope.monitor.factories.PrototypeScopeMonitorFactroy;
import org.brijframework.bean.scope.monitor.factories.RequestScopeMonitorFactroy;
import org.brijframework.bean.scope.monitor.factories.SessionScopeMonitorFactroy;
import org.brijframework.factories.impl.AbstractFactory;
import org.brijframework.group.Group;
import org.brijframework.model.diffination.PropertyModelMetaDataGroup;
import org.brijframework.util.accessor.LogicAccessorUtil;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.ClassUtil;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.MethodUtil;
import org.brijframework.util.support.Access;
import org.brijframework.util.support.Constants;
import org.brijframework.util.text.StringUtil;

public abstract class AbstractBeanScopeFactory extends AbstractFactory<String, BeanScope> implements BeanScopeFactory{

	@Override
	public boolean contains(String key) {
		return find(key)!=null;
	}
	
	public <T> T getBeanScope(String name) {
		BeanDefinition beanMetaData = BeanDefinitionFactoryImpl.getFactory().find(name);
		if(beanMetaData==null) {
			return null;
		}
		String uniqueID = BeanScopeFactoryImpl.getFactory().getUniqueID(beanMetaData);
		return getBeanScope(beanMetaData, uniqueID);
	}

	@SuppressWarnings("unchecked")
	public <T> T getBeanScope(BeanDefinition beanMetaData, String uniqueID) {
		BeanScope find = BeanScopeFactoryImpl.getFactory().find(uniqueID);
		if(find!=null) {
			return (T) find.getScopeObject();
		}
		BeanScope register = BeanScopeFactoryImpl.getFactory().register(uniqueID, beanMetaData);
		return (T) register.getScopeObject();
	}
	
	public BeanScope register(String key,BeanDefinition beanMetaData) {
		BeanScope value=find(key);
		Assertion.isTrue(value!=null,"Bean already exist in cache with : "+key);
		value=new BeanScope(beanMetaData);
		Object scopeObject=buildScopeObject(key,beanMetaData);
		value.setScopeObject(scopeObject);
		value.setId(key);
		return register(key, value);
	}

	public String getUniqueID(BeanDefinition datainfo) {
		switch (datainfo.getScope()) {
		case SINGLETON:
			return datainfo.getId();
		case SESSION:
			return datainfo.getId()+SessionScopeMonitorFactroy.factory().currentService().getId();
		case REQUEST:
			return datainfo.getId()+RequestScopeMonitorFactroy.factory().currentService().getId();
		case PROTOTYPE:
			return datainfo.getId()+PrototypeScopeMonitorFactroy.factory().currentService().getId();
		default:
			return datainfo.getId();
		}
	}

	@SuppressWarnings("rawtypes")
	private Object buildScopeObject(String uniqueID, BeanDefinition datainfo) {
		Assertion.isTrue(datainfo==null,"BeanMetaData not found in cache with : "+uniqueID);
		Assertion.isTrue(datainfo.getOwner()==null,"BeanMetaData Owner not found in cache with : "+uniqueID);
		Object bean=createBean(datainfo);
		if(bean==null) {
			return null;
		}
		for(Entry<String, Object> entry:datainfo.getProperties().entrySet()){
			Object value=entry.getValue();
			PropertyModelMetaDataGroup fieldGroup=datainfo.getOwner().getProperties().get(entry.getKey());
			if(value instanceof Map && ((Map) value).containsKey("@ref")) {
				String ref=(String) ((Map) value).get("@ref");
				value=getBeanScope(ref);
			}
			Assertion.notNull(fieldGroup, datainfo.getOwner().getName()+" : No such type of property contains : "+entry.getKey()+" for "+datainfo.getId());
			Assertion.notNull(fieldGroup.getSetterMeta(), datainfo.getOwner().getName()+" : Not allowed to set such type of property contains : "+entry.getKey());
			PropertyAccessorUtil.setProperty(bean, fieldGroup.getSetterMeta().getType(), value);
		};
		return bean;
	}

	private Object createBean(BeanDefinition datainfo) {
		if(StringUtil.isNonEmpty(datainfo.getFactoryClass())) {
			Class<?> factory=ClassUtil.getClass(datainfo.getFactoryClass());
			Object current=null;
			for(String key:datainfo.getFactoryMethod().split(Constants.SPLIT_DOT)) {
				Method findMethod = MethodUtil.findMethod(factory, key, Access.PRIVATE);
				current= LogicAccessorUtil.callLogic(current, findMethod);
			}
			return current;
		}else {
			return InstanceUtil.getInstance(datainfo.getOwner().getType(), datainfo.getOwner().getConstructor().getValues());
		}
	}
	
	@Override
	public void loadContainer(String key, BeanScope value) {
		if (getContainer() == null) {
			return;
		}
		Group group = getContainer().load(value.getBeanDefinition().getOwner().getName());
		if(!group.containsKey(value.getId())) {
			group.add(key, value);
		}else {
			group.update(key, value);
		}
	}
	
	@Override
	public BeanScope find(Class<? extends Object> beanClass) {
		List<BeanScope> findAll = findAll(beanClass);
		if(findAll.isEmpty()) {
			return null;
		}
		Assertion.isTrue(findAll.size()>1, beanClass.getSimpleName()+" nonunique bean exist.");
		return findAll.get(0);
	}

	@Override
	public List<BeanScope> findAll(Class<? extends Object> beanClass) {
		List<BeanScope> list=new ArrayList<BeanScope>();
		for(BeanScope beanScope:getCache().values()) {
			if(beanClass.isAssignableFrom(beanScope.getBeanDefinition().getOwner().getType())) {
				list.add(beanScope);
			}
		}
		return list;
	}
	
	@Override
	protected void preregister(String key, BeanScope value) {
	}

	@Override
	protected void postregister(String key, BeanScope value) {
	}

}
