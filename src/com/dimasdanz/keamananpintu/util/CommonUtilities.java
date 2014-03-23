package com.dimasdanz.keamananpintu.util;

import android.content.Context;

public final class CommonUtilities {
	private CommonUtilities() {}
	
	public static String deviceStatusUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_status";
	}
	
	public static String changeDeviceStatus(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_change_status";
	}
	
	public static String changeDeviceAttempts(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_change_attempts";
	}
	
	public static String unlockDevice(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_unlock";
	}
}