/*******************************************************************************
 * Copyright (c) 2014 Dimas Rullyan Danu
 * 
 * Kendali Pintu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Kendali Pintu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Kendali Pintu. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.dimasdanz.kendalipintu;

import com.dimasdanz.kendalipintu.R;
import com.dimasdanz.kendalipintu.util.AdminLoginDialog;
import com.dimasdanz.kendalipintu.util.SharedPreferencesManager;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener, OnPreferenceClickListener{
	private String hostname_key = SharedPreferencesManager.property_hostname;
	private String notification_key = SharedPreferencesManager.property_receiveNotifications;
	private String admin_username_key = SharedPreferencesManager.property_adminUsername;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PreferenceManager.setDefaultValues(getActivity(), R.xml.settings, true);
 
        SharedPreferences prefs = SharedPreferencesManager.getSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);
        addPreferencesFromResource(R.xml.settings);
        findPreference(hostname_key).setSummary(SharedPreferencesManager.getHostnamePrefs(getActivity()));
        findPreference(admin_username_key).setSummary(SharedPreferencesManager.getAdminIdPrefs(getActivity()));
        if(SharedPreferencesManager.getNotificationPrefs(getActivity())){
			findPreference(notification_key).setSummary(getActivity().getString(R.string.toast_notification_setting_on));
		}else{
			findPreference(notification_key).setSummary(getActivity().getString(R.string.toast_notification_setting_off));
		}
        findPreference(admin_username_key).setOnPreferenceClickListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if(key.equals(hostname_key)){
			findPreference(key).setSummary(SharedPreferencesManager.getHostnamePrefs(getActivity()));
		}else if(key.equals(notification_key)){
			if(SharedPreferencesManager.getNotificationPrefs(getActivity())){
				findPreference(key).setSummary(getActivity().getString(R.string.toast_notification_setting_on));
			}else{
				findPreference(key).setSummary(getActivity().getString(R.string.toast_notification_setting_off));
			}
		}else if(key.equals(admin_username_key)){
			findPreference(admin_username_key).setSummary(SharedPreferencesManager.getAdminIdPrefs(getActivity()));
		}
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if(preference.getKey().equals(admin_username_key)){
			new AdminLoginDialog(getActivity(), null);
		}
		return false;
	}
	
}
