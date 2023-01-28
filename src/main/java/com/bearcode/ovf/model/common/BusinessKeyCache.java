package com.bearcode.ovf.model.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessKeyCache {

	private static BusinessKeyCache cache;
	
	private final Map<Class<?>, List<Method>> cacheBusinessAnotations;
	
	private BusinessKeyCache() {
		cacheBusinessAnotations = new HashMap<Class<?>, List<Method>>();
	}
	
	public synchronized static BusinessKeyCache getInstance() {
		if (cache == null) {
			cache = new BusinessKeyCache();
		}
		return cache;
	}
	
	public synchronized List<Method> getBusinessKeyMethods(Object thiz) {
		final Class<? extends Object> klazz = thiz.getClass();
		List<Method> methods = cacheBusinessAnotations.get(klazz);
		if (methods == null) {
			// collect all methods with BusinessKey annotation
			methods = new ArrayList<Method>();

			final Method[] rawMethods = klazz.getMethods();
			for (Method method : rawMethods) {
				if (method.isAnnotationPresent(BusinessKey.class)) {
					method.setAccessible(true);
					methods.add(method);
				}
			}

			methods = Collections.unmodifiableList(methods);
			cacheBusinessAnotations.put(klazz, methods);
		}
		return methods;
	}
}
