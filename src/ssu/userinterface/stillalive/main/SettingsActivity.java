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
		Preference prefNoti = findPreference("notifications_ringtone");
		updateSummaryByLegacyValue(prefStateMessage);
		updateSummaryByLegacyValue(prefNoti);

		prefStateMessage.setOnPreferenceChangeListener(this);
		prefNoti.setOnPreferenceChangeListener(this);
	}
	
	void updateSummaryByLegacyValue(Preference preference) {
		updateSummary(preference, 
				PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
	}
	
	void updateSummary(Preference preference, Object value) {
		String stringValue = (String)value;
		
		if (preference.getTitleRes() == R.string.pref_title_notifications) {
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
		} else {
			preference.setSummary(stringValue);
		}
	}

	private void bindPreferenceSummaryToValue(Preference preference) {
		preference.setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
		updateSummary(preference, value);
		
		String stringValue = value.toString();
		if( preference.getTitleRes() == R.string.pref_title_state_message ) {
			Hashtable<String, Object> parameters = new Hashtable<String, Object>();
			parameters.put("access_token", _accessToken);
			parameters.put("message", stringValue);
			HTTPHelper.POST(Config.HOST + "/users/state_message",
					"application/json",
					parameters, 
					new OnResponseListener() {
				
				@Override
				public void OnResponse(String response) {
					try {
						JSONObject json = new JSONObject(response);
						if( json.getInt("result") == 1) {
							Toast.makeText(getApplicationContext(), "Changed!", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "Server error...", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}

		return true;
	}
}
