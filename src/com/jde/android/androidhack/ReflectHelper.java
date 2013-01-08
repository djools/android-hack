package com.jde.android.androidhack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.util.Log;

/**
 * Helper class providing ways to create and access objects/classes/methods
 * using java.lang.reflect API
 */
public class ReflectHelper {

	private static boolean debug = true;

	public static void setDebug(boolean v) {
		debug = v;
	}

	/**
	 * getClass() - returns a Class<?> from its name
	 * 
	 * @param packageName
	 *            name of the containing package
	 * @param className
	 *            name of the class
	 */
	public static Class<?> getClass(String packageName, String className) {
		Class<?> c = null;

		try {
			StringBuilder sb = new StringBuilder(packageName);
			sb.append(".");
			sb.append(className);

			c = Class.forName(sb.toString());
		} catch (ClassNotFoundException e) {
			if (debug)
				Log.e(AndroidHack.TAG, "getClass", e);
		}
		return c;
	}

	/**
	 * createObject() - instanciates and returns a Object
	 * 
	 * @param c
	 *            Class type of the object
	 * @param paramTypes
	 *            Types of the parameters (null if none)
	 * @param params
	 *            Parameters (null if none)
	 */
	public static Object createObject(Class<?> c, Class<?>[] paramTypes,
			Object[] params) throws IllegalArgumentException {
		Object o = null;

		try {
			Constructor<?> cst = c.getConstructor(paramTypes);
			o = cst.newInstance(params);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			if (debug)
				Log.e(AndroidHack.TAG, "createObject", e);
		}

		return o;
	}

	/**
	 * createObject() - instanciates and returns a Object
	 * 
	 * @param packageName
	 *            name of the containing package
	 * @param className
	 *            name of the class
	 * @param paramTypes
	 *            Types of the parameters (null if none)
	 * @param params
	 *            Parameters (null if none)
	 */
	public static Object createObject(String packageName, String className,
			Class<?>[] paramTypes, Object[] params)
			throws IllegalArgumentException {
		Object o = null;

		try {
			Class<?> c = getClass(packageName, className);
			Constructor<?> cst = c.getConstructor(paramTypes);
			o = cst.newInstance(params);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			if (debug)
				Log.e(AndroidHack.TAG, "createObject", e);
		}

		return o;
	}

	/**
	 * callMethod() - calls a method from its name a returns Object result if
	 * any
	 * 
	 * @param cl
	 *            Parent class
	 * @param obj
	 *            Object on which the method is applied
	 * @param mtd
	 *            Method name
	 * @param paramTypes
	 *            Types of the parameters (null if none)
	 * @param params
	 *            Parameters (null if none)
	 */
	public static Object callMethod(Class<?> cl, Object obj, String mtd,
			Class<?>[] paramTypes, Object[] params)
			throws IllegalArgumentException {
		Object ret = null;

		try {
			Method m = cl.getMethod(mtd, paramTypes);
			ret = m.invoke(obj, (Object[]) params);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			if (debug)
				Log.e(AndroidHack.TAG, "callMethod", e);
		}

		return ret;
	}
}
