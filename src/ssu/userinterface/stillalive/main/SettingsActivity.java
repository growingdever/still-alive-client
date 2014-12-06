package ssu.userinterface.stillalive.main;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.common.AuthManager;
import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.common.HTTPHelper;
import ssu.userinterface.stillalive.common.HTTPHelper.OnResponseListener;


public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
	
	private static final String TAG = "SettingsActivity"; 

	String _accessToken;
	

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		SharedPreferences pref = getSharedPreferences("default", Context.MODE_PRIVATE);
		_accessToken = pref.getString("accessToken", "");
		
		setupSimplePreferencesScreen();
	}

	private void setupSimplePreferencesScreen() {
		addPreferencesFromResource(R.xml.pref_general);

		PreferenceCategory fakeHeader = new PreferenceCategory(this);
		fakeHeader.setTitle(R.string.pref_header_notifications);
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.pref_notification);

		Preference prefStateMessage = findPreference("state_message");
		Preference prefNoti = findPreference("notifications_setting");
		Preference prefRingtone = findPreference("notifications_ringtone");
		updateSummaryByLegacyValue(prefStateMessage);
		updateSummaryByLegacyValue(prefNoti);
		updateSummaryByLegacyValue(prefRingtone);

		prefStateMessage.setOnPreferenceChangeListener(this);
		prefNoti.setOnPreferenceChangeListener(this);
		prefRingtone.setOnPreferenceChangeListener(this);
	}
	
	void updateSummaryByLegacyValue(Preference preference) {
		if( preference.getTitleRes() == R.string.pref_title_state_message ) {
			updateSummary(preference, 
					PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));	
		} else if( preference.getTitleRes() == R.string.pref_title_ringtone ) {
			updateSummary(preference, 
					PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
		}
	}
	
	void updateSummary(Preference preference, Object value) {
		if (preference.getTitleRes() == R.string.pref_title_ringtone) {
			String stringValue = (String)value;
			if (TextUtils.isEmpty(stringValue)) {
				preference.setSummary(R.string.pref_ringtone_silent);
			} else {
				Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));

				if (ringtone == null) {
					preference.setSummary(null);
				} else {
					String name = ringtone.getTitle(preference.getContext());
					preference.setSummary(name);
				}
			}
		} else if(preference.getTitleRes() == R.string.pref_title_notifications) {
			
		} else {
			String stringValue = (String)value;
			preference.setSummary(stringValue);
		}
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
		updateSummary(preference, value);
		
		if( preference.getTitleRes() == R.string.pref_title_state_message ) {
			String stringValue = value.toString();
			SendStateMessage(stringValue);
		}

		return true;
	}
	
	void SendStateMessage(String message) {
		Hashtable<String, Object> parameters = new Hashtable<String, Object>();
		parameters.put("access_token", _accessToken);
		parameters.put("message", message);
		HTTPHelper.POST(Config.HOST + "/users/state_message",
				"application/json",
				parameters, 
				new OnResponseListener() {
			
			@Override
			public void OnResponse(String response) {
				int result = 0;
				try {
					JSONObject json = new JSONObject(response);
					result = json.getInt("result");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				switch( result ) {
				case Config.RESULT_CODE_SUCCESS:
					Toast.makeText(getApplicationContext(), "Changed!", Toast.LENGTH_SHORT).show();
					break;
					
				case Config.RESULT_CODE_NOT_VALID_ACCESS_TOKEN:
				case Config.RESULT_CODE_EXPIRED_ACCESS_TOKEN:
					AuthManager.ShowAuthFailAlert(SettingsActivity.this);
					break;
					
				default:
					Toast.makeText(getApplicationContext(), "Server error...", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}
}
