package com.bearcode.ovf.model.common;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * BusinessKeyObject supports BusinessKey annotations.
 * This BusinessKeyObject caches annotated methods for specific class
 * 
 * @author dev
 *
 */
public abstract class BusinessKeyObject {

	protected int createHashCode(Object thiz) {
		final List<Method> methods = BusinessKeyCache.getInstance().getBusinessKeyMethods(thiz);
		final HashCodeBuilder builder = new HashCodeBuilder();
		for (final Method method : methods) {
			try {
				builder.append(method.invoke(thiz, (Object[]) null));
			} catch (Exception e) {
				throw new RuntimeException("Cannot calculate hash, ", e);
			}
		}
		return builder.toHashCode();
	}

	protected boolean isEquals(Object thiz, Object obj) {
		if (thiz == obj) {
			return true;
		}
		if (obj == null || obj.getClass() != thiz.getClass()) {
			return false;
		}
		
		final EqualsBuilder builder = new EqualsBuilder();
		final List<Method> methods = BusinessKeyCache.getInstance().getBusinessKeyMethods(thiz);
		for (final Method method : methods) {
			try {
				final Object lhs = method.invoke(thiz, (Object[]) null);
				final Object rhs = method.invoke(obj, (Object[]) null);
				builder.append(lhs, rhs);
			} catch (Exception e) {
				throw new RuntimeException("Cannot execute equal, ", e);
			}
		}
		return builder.isEquals();
	}
}
