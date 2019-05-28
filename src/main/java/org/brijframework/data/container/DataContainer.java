package org.brijframework.data.container;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.asm.container.DefaultContainer;
import org.brijframework.container.Container;
import org.brijframework.data.factories.DataFactory;
import org.brijframework.data.group.DataGroup;
import org.brijframework.group.Group;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;
import org.brijframework.util.reflect.MethodUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public class DataContainer implements DefaultContainer {

	private ConcurrentHashMap<Object, Group> cache=new  ConcurrentHashMap<>();
	private static DataContainer container;
	
	public static DataContainer getContainer() {
		if(container==null) {
			container=new DataContainer();
		}
		return container;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Container loadContainer() {
		List<Class<? extends DataFactory>> classes=new ArrayList<>();
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls->{
				if(DataFactory.class.isAssignableFrom(cls) && !cls.isInterface() && cls.getModifiers() != Modifier.ABSTRACT) {
					classes.add((Class<? extends DataFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls->{
				if(DataFactory.class.isAssignableFrom(cls) && !cls.isInterface() && cls.getModifiers() != Modifier.ABSTRACT) {
					classes.add((Class<? extends DataFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		classes.forEach((dataFactory)->{
			if(dataFactory.isAnnotationPresent(DepandOn.class)) {
			   DepandOn depandOn=dataFactory.getAnnotation(DepandOn.class);
			   loading(depandOn.depand());
			}
			loading(dataFactory);
		});
		return this;
	}
	
	
	private void loading(Class<?> cls) {
		boolean called=false;
		for(Method method:MethodUtil.getAllMethod(cls)) {
			if(method.isAnnotationPresent(Assignable.class)) {
				try {
					method.invoke(null);
					called=true;
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		if(!called) {
			try {
				DataFactory container=(DataFactory) cls.newInstance();
				container.loadFactory();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}


	@Override
	public ConcurrentHashMap<Object, Group> getCache() {
		return cache;
	}

	@Override
	public Group load(Object groupKey) {
		Group group=get(groupKey);
		if(group==null) {
			group=new DataGroup(groupKey);
			this.add(groupKey, group);
		}
		return group;
	}
}
