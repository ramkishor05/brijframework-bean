package org.brijframework.bean.factories.asm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.brijframework.bean.definition.BeanDefinition;
import org.brijframework.bean.factories.BeanScopeFactory;
import org.brijframework.bean.factories.definition.impl.BeanDefinitionFactoryImpl;
import org.brijframework.bean.scope.BeanScope;
import org.brijframework.bean.scope.monitor.factories.PrototypeScopeMonitorFactroy;
import org.brijframework.bean.scope.monitor.factories.RequestScopeMonitorFactroy;
import org.brijframework.bean.scope.monitor.factories.SessionScopeMonitorFactroy;
import org.brijframework.bean.util.BeanScopeUtil;
import org.brijframework.container.Container;
import org.brijframework.factories.impl.AbstractFactory;
import org.brijframework.group.Group;
import org.brijframework.util.accessor.LogicAccessorUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.printer.LoggerConsole;
import org.brijframework.util.reflect.ClassUtil;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.MethodUtil;
import org.brijframework.util.support.Constants;
import org.brijframework.util.support.ReflectionAccess;
import org.brijframework.util.text.StringUtil;

public abstract class AbstractBeanScopeFactory<K, T extends BeanScope> extends AbstractFactory<K, T> implements BeanScopeFactory<K, T>{

	@Override
	public boolean contains(K key) {
		return find(key)!=null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getBeanScopeForObject(Object object) {
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
	
	@Override
	public Object getObjectForRef(String key) {
		for(BeanScope beanScope:getCache().values()) {
			if(beanScope.getBeanDefinition().getId().equals(key)) {
				return beanScope.getScopeObject();
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
					if(beanScope.getBeanDefinition().getId().equals(key)) {
						return beanScope.getScopeObject();
					}
				}
			}
		}
		return null;
	}
	
	public BeanDefinition getBeanDefinitionOfObject(Object object) {
		T beanScopeOfObject = getBeanScopeForObject(object);
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
		T find=find(key);
		if(find!=null) {
			return find;
		}
		T beanScope=createBeanScope(definition);
		beanScope.setBeanDefinition(definition);
		Object scopeObject=createBean(definition);
		beanScope.setScopeObject(scopeObject);
		beanScope.setId(key.toString());
		register(key, beanScope);
		System.out.println(key+"=Properties="+definition.getProperties());
		BeanScopeUtil.setPropertiesPath(scopeObject,definition.getProperties(),true);
		return beanScope;
	}
	
	protected abstract T createBeanScope(BeanDefinition definition);
	
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

	private Object createBean(BeanDefinition datainfo) {
		if(StringUtil.isNonEmpty(datainfo.getFactoryClass())) {
			Class<?> factory=ClassUtil.getClass(datainfo.getFactoryClass());
			Object current=null;
			for(String key:datainfo.getFactoryMethod().split(Constants.SPLIT_DOT)) {
				Method findMethod = MethodUtil.findMethod(factory, key, ReflectionAccess.PRIVATE);
				current= LogicAccessorUtil.callLogic(current, findMethod);
			}
			return current;
		}else {
			return InstanceUtil.getInstance(datainfo.getOwner().getType(), datainfo.getConstructor().getValues());
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
		LoggerConsole.screen("Bean : ", "BeanScope :"+key);
	}

	@Override
	protected void postregister(K key, T value) {
		
	}
}
