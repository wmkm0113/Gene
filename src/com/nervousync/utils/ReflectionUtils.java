/*
 * Copyright © 2003 - 2010 Nervousync Studio, Inc. All rights reserved.
 * This software is the confidential and proprietary information of 
 * Nervousync Studio, Inc. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the 
 * license agreement you entered into with Nervousync Studio.
 */
package com.nervousync.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nervousync.matcher.Assert;

/**
 * @author Steven Wee	<a href="mailto:wmkm0113@Hotmail.com">wmkm0113@Hotmail.com</a>
 * @version $Revision: 1.0 $ $Date: Jan 13, 2010 4:26:58 PM $
 */
public final class ReflectionUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);
	
	private ReflectionUtils() {
		
	}
	
	public static String parseFieldName(String methodName) {
		String fieldName = null;
		
		if (methodName != null) {
			if (methodName.startsWith("get") || methodName.startsWith("set")) {
				fieldName = methodName.substring(3);
			} else if (methodName.startsWith("is")) {
				fieldName = methodName.substring(2);
			}
		}
		
		if (fieldName != null) {
			fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
		}
		
		return fieldName;
	}
	
	public static String parseMethodName(String fieldName, Class<?> fieldClass, MethodType methodType) {
		String methodName = null;
		
		if (fieldName != null && fieldClass != null && methodType != null) {
			switch (methodType) {
			case GetMethod:
				if (boolean.class.equals(fieldClass)) {
					methodName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				} else {
					methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				}
				break;
			case SetMethod:
				methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				break;
			}
		}
		
		return methodName;
	}
	
	public static Map<String, Object> parseEnum(Class<?> enumClass) {
		Map<String, Object> enumMap = new HashMap<String, Object>();
		
		if (enumClass != null && enumClass.isEnum()) {
			Object[] constants = enumClass.getEnumConstants();
			
			for (Object object : constants) {
				enumMap.put(object.toString(), object);
			}
		}
		
		return enumMap;
	}
	
	public static List<String> getAllDeclaredFieldNames(Class<?> clazz) {
		if (clazz == null) {
			return new ArrayList<String>(0);
		}
		
		List<String> fieldList = new ArrayList<String>();
		
		for (Field field : clazz.getDeclaredFields()) {
			if ((field.getModifiers() & Modifier.STATIC) == 0) {
				fieldList.add(field.getName());
			}
		}
		
		if (clazz.getSuperclass() != null) {
			List<String> superFieldList = ReflectionUtils.getAllDeclaredFieldNames(clazz.getSuperclass());
			
			for (String fieldName : superFieldList) {
				if (!fieldList.contains(fieldName)) {
					fieldList.add(fieldName);
				}
			}
		}
		
		return fieldList;
	}
	
	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with
	 * the supplied <code>name</code>. Searches all superclasses up to {@link Object}.
	 * @param clazz the class to introspect
	 * @param name the name of the field
	 * @return the corresponding Field object, or <code>null</code> if not found
	 */
	public static Field findField(Class<?> clazz, String name) {
		return findField(clazz, name, null);
	}

	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with
	 * the supplied <code>name</code> and/or {@link Class type}. Searches all
	 * superclasses up to {@link Object}.
	 * @param clazz the class to introspect
	 * @param name the name of the field (may be <code>null</code> if type is specified)
	 * @param type the type of the field (may be <code>null</code> if name is specified)
	 * @return the corresponding Field object, or <code>null</code> if not found
	 */
	public static Field findField(Class<?> clazz, String name, Class<?> type) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if ((name == null || name.equals(field.getName()))
						&& (type == null || type.equals(field.getType()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * Set the field represented by the supplied {@link Field field object} on
	 * the specified {@link Object target object} to the specified
	 * <code>value</code>. In accordance with
	 * {@link Field#set(Object, Object)} semantics, the new value is
	 * automatically unwrapped if the underlying field has a primitive type.
	 * <p>Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException(Exception)}.
	 * @param field the field to set
	 * @param target the target object on which to set the field
	 * @param value the value to set; may be <code>null</code>
	 */
	public static void setField(Field field, Object target, Object value) {
		try {
			field.set(target, value);
		}
		catch (IllegalAccessException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException(
					"Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}

	/**
	 * Get the field represented by the supplied {@link Field field object} on
	 * the specified {@link Object target object}. In accordance with
	 * {@link Field#get(Object)} semantics, the returned value is
	 * automatically wrapped if the underlying field has a primitive type.
	 * <p>Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException(Exception)}.
	 * @param fieldName the name of field to get
	 * @param target the target object from which to get the field
	 * @return the field's current value
	 */
	public static Object getFieldValue(String fieldName, Object target) {
		if (fieldName == null || target == null) {
			return null;
		}
		try {
			Method getMethod = ReflectionUtils.retrieveMethod(fieldName, target.getClass(), MethodType.GetMethod);
			if (getMethod != null) {
				return getMethod.invoke(target, new Object[]{});
			} else {
				Field field = getFieldIfAvailable(target.getClass(), fieldName);
				return ReflectionUtils.getFieldValue(field, target);
			}
		}
		catch (Exception ex) {
			handleReflectionException(ex);
			throw new IllegalStateException(
					"Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}

	/**
	 * Get the field represented by the supplied {@link Field field object} on
	 * the specified {@link Object target object}. In accordance with
	 * {@link Field#get(Object)} semantics, the returned value is
	 * automatically wrapped if the underlying field has a primitive type.
	 * <p>Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException(Exception)}.
	 * @param field the field to get
	 * @param target the target object from which to get the field
	 * @return the field's current value
	 */
	public static Object getFieldValue(Field field, Object target) {
		if (field == null || target == null) {
			return null;
		}
		try {
			makeAccessible(field);
			return field.get(target);
		}
		catch (IllegalAccessException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException(
					"Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}

	public static Object executeMethod(String methodName, Object target) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return executeMethod(methodName, target, new Class[]{}, new Object[]{});
	}
	
	public static Object executeMethod(String methodName, Object target, Class<?>[] paramClasses, Object... args) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = ReflectionUtils.findMethod(target.getClass(), methodName, paramClasses);
		if (method == null) {
			throw new IllegalArgumentException("Method named : " + methodName + " does not exists");
		}
		ReflectionUtils.makeAccessible(method);
		
		Object returnObj = null;
		if (method.getReturnType().equals(void.class)) {
			method.invoke(target, args);
		} else {
			returnObj = method.invoke(target, args);
		}
		return returnObj;
	}
	
	public static <T> Constructor<T> findConstructor(Class<T> clazz) 
			throws SecurityException, NoSuchMethodException {
		return clazz.getDeclaredConstructor(new Class[0]);
	}

	public static <T> Constructor<T> findConstructor(Class<T> clazz, Class<?>[] paramTypes) 
			throws SecurityException, NoSuchMethodException {
		return clazz.getDeclaredConstructor(paramTypes);
	}

	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied name
	 * and no parameters. Searches all superclasses up to <code>Object</code>.
	 * <p>Returns <code>null</code> if no {@link Method} can be found.
	 * @param clazz the class to introspect
	 * @param name the name of the method
	 * @return the Method object, or <code>null</code> if none found
	 */
	public static Method findMethod(Class<?> clazz, String name) {
		return findMethod(clazz, name, new Class[0]);
	}

	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied name
	 * and parameter types. Searches all superclasses up to <code>Object</code>.
	 * <p>Returns <code>null</code> if no {@link Method} can be found.
	 * @param clazz the class to introspect
	 * @param name the name of the method
	 * @param paramTypes the parameter types of the method
	 * (may be <code>null</code> to indicate any signature)
	 * @return the Method object, or <code>null</code> if none found
	 */
	public static Method findMethod(Class<?> clazz, String name, Class<?>[] paramTypes) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (name.equals(method.getName()) &&
						(paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * Invoke the specified {@link Method} against the supplied target object
	 * with no arguments. The target object can be <code>null</code> when
	 * invoking a static {@link Method}.
	 * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @return the invocation result, if any
	 * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
	 */
	public static Object invokeMethod(Method method, Object target) {
		return invokeMethod(method, target, null);
	}

	/**
	 * Invoke the specified {@link Method} against the supplied target object
	 * with the supplied arguments. The target object can be <code>null</code>
	 * when invoking a static {@link Method}.
	 * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @param args the invocation arguments (may be <code>null</code>)
	 * @return the invocation result, if any
	 */
	public static Object invokeMethod(Method method, Object target, Object[] args) {
		try {
			return method.invoke(target, args);
		}
		catch (Exception ex) {
			handleReflectionException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}

	/**
	 * Invoke the specified JDBC API {@link Method} against the supplied
	 * target object with no arguments.
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @return the invocation result, if any
	 * @throws SQLException the JDBC API SQLException to rethrow (if any)
	 * @see #invokeJdbcMethod(java.lang.reflect.Method, Object, Object[])
	 */
	public static Object invokeJdbcMethod(Method method, Object target) throws SQLException {
		return invokeJdbcMethod(method, target, null);
	}

	/**
	 * Invoke the specified JDBC API {@link Method} against the supplied
	 * target object with the supplied arguments.
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @param args the invocation arguments (may be <code>null</code>)
	 * @return the invocation result, if any
	 * @throws SQLException the JDBC API SQLException to rethrow (if any)
	 * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
	 */
	public static Object invokeJdbcMethod(Method method, Object target, Object[] args) throws SQLException {
		try {
			return method.invoke(target, args);
		}
		catch (IllegalAccessException ex) {
			handleReflectionException(ex);
		}
		catch (InvocationTargetException ex) {
			if (ex.getTargetException() instanceof SQLException) {
				throw (SQLException) ex.getTargetException();
			}
			handleInvocationTargetException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}

	/**
	 * Handle the given reflection exception. Should only be called if
	 * no checked exception is expected to be thrown by the target method.
	 * <p>Throws the underlying RuntimeException or Error in case of an
	 * InvocationTargetException with such a root cause. Throws an
	 * IllegalStateException with an appropriate message else.
	 * @param ex the reflection exception to handle
	 */
	public static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: " + ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method: " + ex.getMessage());
		}
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		handleUnexpectedException(ex);
	}

	/**
	 * Handle the given invocation target exception. Should only be called if
	 * no checked exception is expected to be thrown by the target method.
	 * <p>Throws the underlying RuntimeException or Error in case of such
	 * a root cause. Throws an IllegalStateException else.
	 * @param ex the invocation target exception to handle
	 */
	public static void handleInvocationTargetException(InvocationTargetException ex) {
		rethrowRuntimeException(ex.getTargetException());
	}

	/**
	 * Rethrow the given {@link Throwable exception}, which is presumably the
	 * <em>target exception</em> of an {@link InvocationTargetException}.
	 * Should only be called if no checked exception is expected to be thrown by
	 * the target method.
	 * <p>Rethrows the underlying exception cast to an {@link RuntimeException}
	 * or {@link Error} if appropriate; otherwise, throws an
	 * {@link IllegalStateException}.
	 * @param ex the exception to rethrow
	 * @throws RuntimeException the rethrown exception
	 */
	public static void rethrowRuntimeException(Throwable ex) {
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		handleUnexpectedException(ex);
	}

	/**
	 * Rethrow the given {@link Throwable exception}, which is presumably the
	 * <em>target exception</em> of an {@link InvocationTargetException}.
	 * Should only be called if no checked exception is expected to be thrown by
	 * the target method.
	 * <p>Rethrows the underlying exception cast to an {@link Exception} or
	 * {@link Error} if appropriate; otherwise, throws an
	 * {@link IllegalStateException}.
	 * @param ex the exception to rethrow
	 * @throws Exception the rethrown exception (in case of a checked exception)
	 */
	public static void rethrowException(Throwable ex) throws Exception {
		if (ex instanceof Exception) {
			throw (Exception) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		handleUnexpectedException(ex);
	}

	/**
	 * Throws an IllegalStateException with the given exception as root cause.
	 * @param ex the unexpected exception
	 */
	private static void handleUnexpectedException(Throwable ex) {
		// Needs to avoid the chained constructor for JDK 1.4 compatibility.
		IllegalStateException isex = new IllegalStateException("Unexpected exception thrown");
		isex.initCause(ex);
		throw isex;
	}

	/**
	 * Determine whether the given method explicitly declares the given exception
	 * or one of its superclasses, which means that an exception of that type
	 * can be propagated as-is within a reflective invocation.
	 * @param method the declaring method
	 * @param exceptionType the exception to throw
	 * @return <code>true</code> if the exception can be thrown as-is;
	 * <code>false</code> if it needs to be wrapped
	 */
	public static boolean declaresException(Method method, Class<?> exceptionType) {
		Assert.notNull(method, "Method must not be null");
		Class<?>[] declaredExceptions = method.getExceptionTypes();
		for (int i = 0; i < declaredExceptions.length; i++) {
			Class<?> declaredException = declaredExceptions[i];
			if (declaredException.isAssignableFrom(exceptionType)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Determine whether the given field is a "public static final" constant.
	 * @param field the field to check
	 */
	public static boolean isPublicStaticFinal(Field field) {
		int modifiers = field.getModifiers();
		return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
	}

	/**
	 * Make the given field accessible, explicitly setting it accessible if necessary.
	 * The <code>setAccessible(true)</code> method is only called when actually necessary,
	 * to avoid unnecessary conflicts with a JVM SecurityManager (if active).
	 * @param field the field to make accessible
	 * @see java.lang.reflect.Field#setAccessible
	 */
	public static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers()) ||
				!Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * Make the given method accessible, explicitly setting it accessible if necessary.
	 * The <code>setAccessible(true)</code> method is only called when actually necessary,
	 * to avoid unnecessary conflicts with a JVM SecurityManager (if active).
	 * @param method the method to make accessible
	 * @see java.lang.reflect.Method#setAccessible
	 */
	public static void makeAccessible(Method method) {
		if (!Modifier.isPublic(method.getModifiers()) ||
				!Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
			method.setAccessible(true);
		}
	}

	/**
	 * Make the given constructor accessible, explicitly setting it accessible if necessary.
	 * The <code>setAccessible(true)</code> method is only called when actually necessary,
	 * to avoid unnecessary conflicts with a JVM SecurityManager (if active).
	 * @param ctor the constructor to make accessible
	 * @see java.lang.reflect.Constructor#setAccessible
	 */
	public static void makeAccessible(Constructor<?> ctor) {
		if (!Modifier.isPublic(ctor.getModifiers()) ||
				!Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) {
			ctor.setAccessible(true);
		}
	}


	/**
	 * Perform the given callback operation on all matching methods of the
	 * given class and superclasses.
	 * <p>The same named method occurring on subclass and superclass will
	 * appear twice, unless excluded by a {@link MethodFilter}.
	 * @param targetClass class to start looking at
	 * @param mc the callback to invoke for each method
	 * @see #doWithMethods(Class, MethodCallback, MethodFilter)
	 */
	public static void doWithMethods(Class<?> targetClass, MethodCallback mc) throws IllegalArgumentException {
		doWithMethods(targetClass, mc, null);
	}

	/**
	 * Perform the given callback operation on all matching methods of the
	 * given class and superclasses.
	 * <p>The same named method occurring on subclass and superclass will
	 * appear twice, unless excluded by the specified {@link MethodFilter}.
	 * @param targetClass class to start looking at
	 * @param mc the callback to invoke for each method
	 * @param mf the filter that determines the methods to apply the callback to
	 */
	public static void doWithMethods(Class<?> targetClass, MethodCallback mc, MethodFilter mf)
			throws IllegalArgumentException {

		// Keep backing up the inheritance hierarchy.
		do {
			Method[] methods = targetClass.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				if (mf != null && !mf.matches(methods[i])) {
					continue;
				}
				try {
					mc.doWith(methods[i]);
				}
				catch (IllegalAccessException ex) {
					throw new IllegalStateException(
							"Shouldn't be illegal to access method '" + methods[i].getName() + "': " + ex);
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		while (targetClass != null);
	}

	/**
	 * Get all declared methods on the leaf class and all superclasses.
	 * Leaf class methods are included first.
	 */
	public static Method[] getAllDeclaredMethods(Class<?> leafClass) throws IllegalArgumentException {
		final List<Method> list = new ArrayList<Method>(32);
		doWithMethods(leafClass, new MethodCallback() {
			public void doWith(Method method) {
				list.add(method);
			}
		});
		return (Method[]) list.toArray(new Method[list.size()]);
	}


	/**
	 * Invoke the given callback on all fields in the target class,
	 * going up the class hierarchy to get all declared fields.
	 * @param targetClass the target class to analyze
	 * @param fc the callback to invoke for each field
	 */
	public static void doWithFields(Class<?> targetClass, FieldCallback fc) throws IllegalArgumentException {
		doWithFields(targetClass, fc, null);
	}

	/**
	 * Invoke the given callback on all fields in the target class,
	 * going up the class hierarchy to get all declared fields.
	 * @param targetClass the target class to analyze
	 * @param fc the callback to invoke for each field
	 * @param ff the filter that determines the fields to apply the callback to
	 */
	public static void doWithFields(Class<?> targetClass, FieldCallback fc, FieldFilter ff)
			throws IllegalArgumentException {

		// Keep backing up the inheritance hierarchy.
		do {
			// Copy each field declared on this class unless it's static or file.
			Field[] fields = targetClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				// Skip static and final fields.
				if (ff != null && !ff.matches(fields[i])) {
					continue;
				}
				try {
					fc.doWith(fields[i]);
				}
				catch (IllegalAccessException ex) {
					throw new IllegalStateException(
							"Shouldn't be illegal to access field '" + fields[i].getName() + "': " + ex);
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		while (targetClass != null && targetClass != Object.class);
	}

	/**
	 * Given the source object and the destination, which must be the same class
	 * or a subclass, copy all fields, including inherited fields. Designed to
	 * work on objects with public no-arg constructors.
	 * @throws IllegalArgumentException if the arguments are incompatible
	 */
	public static void shallowCopyFieldState(final Object src, final Object dest) throws IllegalArgumentException {
		if (src == null) {
			throw new IllegalArgumentException("Source for field copy cannot be null");
		}
		if (dest == null) {
			throw new IllegalArgumentException("Destination for field copy cannot be null");
		}
		if (!src.getClass().isAssignableFrom(dest.getClass())) {
			throw new IllegalArgumentException("Destination class [" + dest.getClass().getName() +
					"] must be same or subclass as source class [" + src.getClass().getName() + "]");
		}
		doWithFields(src.getClass(), new FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				makeAccessible(field);
				Object srcValue = field.get(src);
				field.set(dest, srcValue);
			}
		}, COPYABLE_FIELDS);
	}
	
	public static Field getFieldIfAvailable(Class<?> clazz, String fieldName) {
		if (clazz == null) {
			return null;
		}
		
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			return getFieldIfAvailable(clazz.getSuperclass(), fieldName);
		}
	}
	
	public static void setField(Object target, Map<String, ?> parameterMap) {
		if (target == null || parameterMap == null) {
			return;
		}
		
		Iterator<String> iterator = parameterMap.keySet().iterator();
		
		while (iterator.hasNext()) {
			String fieldName = iterator.next();
			Object value = parameterMap.get(fieldName);
			Object fieldValue = null;
			if (value.getClass().isArray()) {
				if (((Object[])value).length == 1) {
					fieldValue = ((Object[])value)[0];
				} else {
					fieldValue = value;
				}
			} else {
				fieldValue = value;
			}
			
			setField(fieldName, target, fieldValue);
		}
	}
	
	public static boolean setField(String fieldName, Object target, Object value) {
		try {
			Method setMethod = ReflectionUtils.retrieveMethod(fieldName, target.getClass(), MethodType.SetMethod);
			if (setMethod != null) {
				setMethod.invoke(target, new Object[]{value});
				return true;
			} else {
				Field field = getFieldIfAvailable(target.getClass(), fieldName);
				if (field == null) {
					return false;
				}
				
				Object object = null;
				
				if (value != null) {
					Class<?> clazz = field.getType();
					if (!value.getClass().equals(clazz)) {
						int length = 0;
						if (value.getClass().isArray()) {
							length = ((String[])value).length;
						} else {
							length = 1;
						}
						
						if (clazz.isArray()) {
							Class<?> arrayClass = clazz.getComponentType();
							object = Array.newInstance(arrayClass, length);
							
							if (arrayClass.isPrimitive()) {
								if (arrayClass.equals(int.class)) {
									if (length == 1) {
										Array.set(object, 0, Integer.valueOf(value.toString()).intValue());
									} else {
										for (int i = 0 ; i < length ; i++) {
											Array.set(object, i, Integer.valueOf(((String[])value)[i]).intValue());
										}
									}
								} else if (arrayClass.equals(double.class)) {
									if (length == 1) {
										Array.set(object, 0, Double.valueOf(value.toString()).doubleValue());
									} else {
										for (int i = 0 ; i < length ; i++) {
											Array.set(object, i, Double.valueOf(((String[])value)[i]).doubleValue());
										}
									}
								} else if (arrayClass.equals(float.class)) {
									if (length == 1) {
										Array.set(object, 0, Float.valueOf(value.toString()).floatValue());
									} else {
										for (int i = 0 ; i < length ; i++) {
											Array.set(object, i, Float.valueOf(((String[])value)[i]).floatValue());
										}
									}
								} else if (arrayClass.equals(long.class)) {
									if (length == 1) {
										Array.set(object, 0, Long.valueOf(value.toString()).longValue());
									} else {
										for (int i = 0 ; i < length ; i++) {
											Array.set(object, i, Long.valueOf(((String[])value)[i]).longValue());
										}
									}
								} else if (arrayClass.equals(short.class)) {
									if (length == 1) {
										Array.set(object, 0, Short.valueOf(value.toString()).shortValue());
									} else {
										for (int i = 0 ; i < length ; i++) {
											Array.set(object, i, Short.valueOf(((String[])value)[i]).shortValue());
										}
									}
								} else if (arrayClass.equals(boolean.class)) {
									if (length == 1) {
										Array.set(object, 0, new Boolean(value.toString()).booleanValue());
									} else {
										for (int i = 0 ; i < length ; i++) {
											Array.set(object, i, new Boolean(((String[])value)[i]).booleanValue());
										}
									}
								}
							} else {
								if (arrayClass.equals(String.class)) {
									if (length == 1) {
										Array.set(object, 0, value.toString());
									} else {
										object = (String[])value;
									}
								} else if (arrayClass.equals(Integer.class)) {
									if (length == 1) {
										Array.set(object, 0, Integer.valueOf(value.toString()));
									} else {
										for (int i = 0 ; i < length ; i++) {
											Array.set(object, i, Integer.valueOf(((String[])value)[i]));
										}
									}
								} else if (arrayClass.equals(Float.class)) {
									if (length == 1) {
										Array.set(object, 0, Float.valueOf(value.toString()));
									} else {
										for (int i = 0 ; i < length ; i++) {
											Array.set(object, i, Float.valueOf(((String[])value)[i]));
										}
									}
								} else if (arrayClass.equals(Double.class)) {
									if (length == 1) {
										Array.set(object, 0, Double.valueOf(value.toString()));
									} else {
										for (int i = 0 ; i < length ; i++) {
											Array.set(object, i, Double.valueOf(((String[])value)[i]));
										}
									}
								} else if (arrayClass.equals(Long.class)) {
									if (length == 1) {
										Array.set(object, 0, Long.valueOf(value.toString()));
									} else {
										for (int i = 0 ; i < length ; i++) {
											Array.set(object, i, Long.valueOf(((String[])value)[i]));
										}
									}
								} else if (arrayClass.equals(Boolean.class)) {
									if (length == 1) {
										Array.set(object, 0, new Boolean(value.toString()));
									} else {
										for (int i = 0 ; i < length ; i++) {
											Array.set(object, i, new Boolean(((String[])value)[i]));
										}
									}
								} else {
									if (length == 1) {
										Array.set(object, 0, value);
									} else {
										for (int i = 0 ; i < length ; i++) {
											Array.set(object, i, ((Object[])value)[i]);
										}
									}
								}
							}
						} else if (clazz.isPrimitive()) {
							//	Basic data type	int, double, float, long, short
							if (clazz.equals(int.class)) {
								object = Integer.valueOf(value.toString()).intValue();
							} else if (clazz.equals(double.class)) {
								object = Double.valueOf(value.toString()).doubleValue();
							} else if (clazz.equals(float.class)) {
								object = Float.valueOf(value.toString()).floatValue();
							} else if (clazz.equals(long.class)) {
								object = Long.valueOf(value.toString()).longValue();
							} else if (clazz.equals(short.class)) {
								object = Short.valueOf(value.toString()).shortValue();
							} else if (clazz.equals(boolean.class)) {
								object = new Boolean(value.toString()).booleanValue();
							}
						} else {
							if (clazz.equals(Integer.class)) {
								object = Integer.valueOf(value.toString());
							} else if (clazz.equals(Float.class)) {
								object = Float.valueOf(value.toString());
							} else if (clazz.equals(Double.class)) {
								object = Double.valueOf(value.toString());
							} else if (clazz.equals(Long.class)) {
								object = Long.valueOf(value.toString());
							} else if (clazz.equals(Short.class)) {
								object = Short.valueOf(value.toString());
							} else if (clazz.equals(Boolean.class)) {
								object = new Boolean(value.toString());
							} else {
								object = value;
							}
						}
					} else {
						object = value;
					}
				}
				
				makeAccessible(field);
				setField(field, target, object);
				return true;
			}
		} catch (Exception e) {
			if (ReflectionUtils.LOGGER.isDebugEnabled()) {
				ReflectionUtils.LOGGER.debug("Convert to Object set field value error! ", e);
				if (value != null) {
					ReflectionUtils.LOGGER.debug(fieldName + "" + value.getClass().getName());
				}
			}
			return false;
		}
	}
	
	private static Method retrieveMethod(String fieldName, Class<?> targetClass, MethodType methodType) {
		Field field = ReflectionUtils.getFieldIfAvailable(targetClass, fieldName);
		if (field == null) {
			return null;
		}
		
		String methodName = null;
		
		switch(methodType) {
		case GetMethod:
			if (boolean.class.equals(field.getType())) {
				methodName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			} else {
				methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			}
			break;
		case SetMethod:
			methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			break;
		}
		
		Method method = null;
		if (methodName != null) {
			if (MethodType.GetMethod.equals(methodType)) {
				method = ReflectionUtils.findMethod(targetClass, methodName);
			} else if (MethodType.SetMethod.equals(methodType)) {
				method = ReflectionUtils.findMethod(targetClass, methodName, new Class<?>[]{field.getType()});
			}
		}
		return method;
	}

	/**
	 * Action to take on each method.
	 */
	public static interface MethodCallback {

		/**
		 * Perform an operation using the given method.
		 * @param method the method to operate on
		 */
		void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
	}


	/**
	 * Callback optionally used to method fields to be operated on by a method callback.
	 */
	public static interface MethodFilter {

		/**
		 * Determine whether the given method matches.
		 * @param method the method to check
		 */
		boolean matches(Method method);
	}


	/**
	 * Callback interface invoked on each field in the hierarchy.
	 */
	public static interface FieldCallback {

		/**
		 * Perform an operation using the given field.
		 * @param field the field to operate on
		 */
		void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;
	}


	/**
	 * Callback optionally used to filter fields to be operated on by a field callback.
	 */
	public static interface FieldFilter {

		/**
		 * Determine whether the given field matches.
		 * @param field the field to check
		 */
		boolean matches(Field field);
	}


	/**
	 * Pre-built FieldFilter that matches all non-static, non-final fields.
	 */
	public static FieldFilter COPYABLE_FIELDS = new FieldFilter() {
		public boolean matches(Field field) {
			return !(Modifier.isStatic(field.getModifiers()) ||
					Modifier.isFinal(field.getModifiers()));
		}
	};
	
	public enum MethodType {
		GetMethod, SetMethod
	}
}
