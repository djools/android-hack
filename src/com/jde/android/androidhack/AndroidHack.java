package com.jde.android.androidhack;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;

import com.android.internal.telephony.ITelephony;

/**
 * This snippet shows how to instanciate and use inner Android API. Some methods
 * or features are not exposed by default in SDK. Using java.lang.reflect we can
 * access them anyway.
 * 
 * Here we are showing how to get access to TelephonyManager and calling
 * getMsisdn method
 * 
 * Then we will use the aidl ITelephony interface to ask for radio state (airplane
 * mode)
 * 
 * Nevertheless, Android has also permissions mechanism preventing access to
 * functionalities that may such as creating a Phone object (from PhoneFactory)
 * which requires high level permission such as
 * android.permission.INTERACT_ACROSS_USERS.
 */
public class AndroidHack extends Activity {

	public static final String TAG = "AndroidHack";

	/*
	 * Create TelephonyManager instance and access to hidden method getMsisdn
	 * using Reflect
	 */
	private String getMsisdn() {
		String msisdn = null;

		if (AndroidClassFactory.makeTelephonyManager(AndroidHack.this)) {
			// Here we call the hidden method getMsisdn that does not take any
			// parameters.
			// Note: we need permission android.permission.READ_PHONE_STATE to
			// access it
			msisdn = (String) ReflectHelper.callMethod(
					AndroidClassFactory.getTelMgrClass(),
					AndroidClassFactory.getTelMgr(), "getMsisdn",
					(Class<?>[]) null, (Object[]) null);
			if (msisdn != null)
				Log.d(TAG, "MSISDN:" + msisdn);
		}

		return msisdn;
	}

	/*
	 * Use SDK TelephonyManager, but access hidden method using Reflect API
	 * method getMethod
	 */
	private String getSomething() {
		// TODO
		return null;
	}

	/*
	 * Get access to interface method defined in aidl. To do that, just copy the
	 * aidl from the Android framework into your code location and impor.t it
	 */
	private boolean getRadioState() {
		boolean radioOn = false;

		// Now we get TelephonyManager in a "normal" way...
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		// ...and we get the ITelephony interface we copied from Android
		// framework
		// to get access to hidden Telephony API
		ITelephony iTel = AndroidClassFactory.getITelephony(tm);
		try {
			radioOn = iTel.isRadioOn();
			Log.d(TAG, "Airplane mode:"
					+ (radioOn ? "off (Radio on)" : "on (Radio off)"));
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return radioOn;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_hack);
		ReflectHelper.setDebug(true);
		boolean radioState = getRadioState();
		String msisdn = getMsisdn();
		String something = getSomething();

		Log.d(TAG, "radio:" + (radioState ? "on" : "off"));
		Log.d(TAG, "msisdn:" + msisdn);
		Log.d(TAG, "something:" + something);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_android_hack, menu);
		return true;
	}

}
