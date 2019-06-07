package com.paysafetestapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.paysafetestapp.PaysafeApplication;

/**
 * @author ManishaR
 * 
 */
public class AppSettings {

	private static final String PREFERENCES = "Preferences";

	private static SharedPreferences getSharedPreferences(Context context) {

		return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
	}

	public static void setBoolean(String setting, boolean value) {
		SharedPreferences settings = getSharedPreferences(PaysafeApplication.mApplicationContext);
		Editor edit = settings.edit();
		edit.putBoolean(setting, value);
		edit.apply();
	}

	public static boolean getBoolean(String setting, boolean def) {
		SharedPreferences settings = getSharedPreferences(PaysafeApplication.mApplicationContext);
		return settings.getBoolean(setting, def);
	}
	
}
