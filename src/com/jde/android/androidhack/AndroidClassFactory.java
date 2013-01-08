package com.jde.android.androidhack;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class AndroidClassFactory {
	private static Class<?> mTelMgrClass;
	private static Object mTelMgr;

	/**
	 * makeTelephonyManager() - instanciates a TelephonyManager using
	 * java.lang.Reflect
	 * 
	 * @param c
	 *            The application context
	 * @return true if creation is successful, false otherwise
	 */
	public static boolean makeTelephonyManager(Context c) {
		// types
		Class<?>[] types = new Class<?>[1];
		types[0] = Context.class;

		// params
		Object[] params = new Object[1];
		params[0] = c;

		mTelMgrClass = ReflectHelper.getClass("android.telephony",
				"TelephonyManager");
		if (mTelMgrClass == null)
			return false;

		mTelMgr = ReflectHelper.createObject(mTelMgrClass, types, params);
		if (mTelMgr == null)
			return false;

		return true;
	}

	/**
	 * getTelephonyManager() - returns TelephonyManager in a common way (from
	 * telephony service)
	 * 
	 * @return TelephonyManager
	 */
	public static TelephonyManager getTelephonyManager(Context c) {
		return (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * getITelephony() - returns ITelephony interface
	 * 
	 * @param tm
	 *            The TelephonyManager
	 * @return the ITelephony object if retrieved, null otherwise
	 */
	public static ITelephony getITelephony(TelephonyManager tm) {
		ITelephony ts = null;

		try {
			Class<?> c = Class.forName(tm.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			ts = (ITelephony) m.invoke(tm);
		} catch (Exception e) {
			Log.e(AndroidHack.TAG, "getITelephony", e);
		}

		return ts;
	}

	public static Object getTelMgr() {
		return mTelMgr;
	}

	public static Class<?> getTelMgrClass() {
		return mTelMgrClass;
	}
}
