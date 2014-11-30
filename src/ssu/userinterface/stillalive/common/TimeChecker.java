/*
 * Time을 체크하는 클래스
 * 
 */

package ssu.userinterface.stillalive.common;

import android.content.Context;
import android.content.SharedPreferences;

public class TimeChecker {
	private String PREFERENCE_NAME = "MY_PREF";
	private String PREFERENCE_TIME_ATTR = "TIME";
	private static TimeChecker _singleton;
	public static TimeChecker getInstance() {
		if (_singleton == null) {
			_singleton = new TimeChecker();
		}
		return _singleton;
	}

	private TimeChecker() {
	
	}
	
	public void setCurrentTime(Context context){
		SharedPreferences setting = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		long current = System.currentTimeMillis();
		SharedPreferences.Editor editor = setting.edit();
		editor.putLong(PREFERENCE_TIME_ATTR, current);
		editor.commit();
	}
	
	public long getCurrentGapTimeFromBefore(Context context){
		SharedPreferences setting = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		long beforeTime = setting.getLong(PREFERENCE_TIME_ATTR, 0L);
		long current = System.currentTimeMillis();
		return current-beforeTime;
	}
}
