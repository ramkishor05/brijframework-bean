package org.brijframework.bean.factories.asm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.factories.BeanScopeFactory;
import org.brijframework.bean.factories.definition.impl.BeanDefinitionFactoryImpl;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.bean.scope.monitor.factories.PrototypeScopeMonitorFactroy;
import org.brijframework.bean.scope.monitor.factories.RequestScopeMonitorFactroy;
import org.brijframework.bean.scope.monitor.factories.SessionScopeMonitorFactroy;
import org.brijframework.container.Container;
import org.brijframework.factories.impl.AbstractFactory;
import org.brijframework.group.Group;
import org.brijframework.model.diffination.ModelPropertyDiffinationGroup;
import org.brijframework.util.accessor.LogicAccessorUtil;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.ClassUtil;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.MethodUtil;
import org.brijframework.util.support.Access;
import org.brijframework.util.support.Constants;
import org.brijframework.util.text.StringUtil;

public abstract class AbstractBeanScopeFactory<K, T extends BeanScope> extends AbstractFactory<K, T> implements BeanScopeFactory<K, T>{

	@Override
	public boolean contains(K key) {
		return find(key)!=null;
	}
	
	@SuppressWarnings("unchecked")
	public T getBeanScopeOfObject(Object object) {
		for(BeanScope beanScope:getCache().values()) {
			if(object==beanScope.getScopeObject()) {
				return (T) beanScope;
			}
		}
		Container container =getContainer();
		if(container==null) {
			return null;
		}
		for(Group group:container.getCache().values()) {
			for(Object objectScope:group.getCache().values()) {
				if(objectScope instanceof BeanScope) {
					BeanScope beanScope=(BeanScope) objectScope;
					if(object==beanScope.getScopeObject()) {
						return (T) beanScope;
					}
				}
			}
		}
		return null;
	}
	
	public BeanDefinition getBeanDefinitionOfObject(Object object) {
		T beanScopeOfObject = getBeanScopeOfObject(object);
		return beanScopeOfObject!=null ? beanScopeOfObject.getBeanDefinition(): null;
	}
	
	@SuppressWarnings("unchecked")
	public T getBeanScope(K name) {
		BeanDefinition beanMetaData = BeanDefinitionFactoryImpl.getFactory().find(name.toString());
		if(beanMetaData==null) {
			return null;
		}
		K uniqueID = (K) getUniqueID(beanMetaData);
		return getBeanScope(beanMetaData, uniqueID);
	}

	@SuppressWarnings("unchecked")
	public T getBeanScope(BeanDefinition definition, K uniqueID) {
		T find = find(uniqueID);
		if(find!=null) {
			return (T) find;
		}
		BeanScope register = register(uniqueID, definition);
		return (T) register;
	}
	
	public BeanScope register(K key, BeanDefinition definition) {
		T value=find(key);
		Assertion.isTrue(value!=null,"Bean already exist in cache with : "+key);
		value=create(definition);
		value.setBeanDefinition(definition);
		Object scopeObject=buildScopeObject(key,definition);
		value.setScopeObject(scopeObject);
		value.setId(key.toString());
		return register(key, value);
	}
	
	protected abstract T create(BeanDefinition definition);
	
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object buildScopeObject(K uniqueID, BeanDefinition datainfo) {
		Assertion.isTrue(datainfo==null,"BeanMetaData not found in cache with : "+uniqueID);
		Assertion.isTrue(datainfo.getOwner()==null,"BeanMetaData Owner not found in cache with : "+uniqueID);
		Object bean=createBean(datainfo);
		if(bean==null) {
			return null;
		}
		for(Entry<String, Object> entry:datainfo.getProperties().entrySet()){
			Object value=entry.getValue();
			ModelPropertyDiffinationGroup fieldGroup=datainfo.getOwner().getProperties().get(entry.getKey());
			if(value instanceof Map && ((Map) value).containsKey("@ref")) {
				String ref=(String) ((Map) value).get("@ref");
				value=getBeanScope((K)ref);
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
	public void loadContainer(K key, T value) {
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
	public T find(Class<? extends Object> beanClass) {
		List<T> findAll = findAll(beanClass);
		if(findAll.isEmpty()) {
			return null;
		}
		Assertion.isTrue(findAll.size()>1, beanClass.getSimpleName()+" nonunique bean exist.");
		return findAll.get(0);
	}

	@Override
	public List<T> findAll(Class<? extends Object> beanClass) {
		List<T> list=new ArrayList<T>();
		for(T beanScope:getCache().values()) {
			if(beanClass.isAssignableFrom(beanScope.getBeanDefinition().getOwner().getType())) {
				list.add(beanScope);
			}
		}
		return list;
	}
	
	@Override
	protected void preregister(K key, T value) {
	}

	@Override
	protected void postregister(K key, T value) {
	}

}
