package ssu.userinterface.stillalive.main;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.common.GCMActivity;
import ssu.userinterface.stillalive.common.TimeChecker;
import ssu.userinterface.stillalive.main.friendlist.FriendListFragment;
import ssu.userinterface.stillalive.main.needtoupdate.NeedToUpdateFragment;
import ssu.userinterface.stillalive.main.signin.SignInActivity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends GCMActivity {

	private static final String TAG = "MainActivity";
	
	public static final int STATE_FRIEND_LIST = 1;
	public static final int STATE_NEED_TO_UPDATE = 2;
	
	private Fragment fragment;
	
	private int currentState = STATE_FRIEND_LIST;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		fragment = getFragmentManager().findFragmentById(R.id.main_fragment);
		
		SharedPreferences pref = getSharedPreferences("default", MODE_PRIVATE);
		Log.d(TAG, pref.getString("accessToken", ""));
		if (pref.getString("accessToken", "").equals("")) {
			Intent i = new Intent(getApplicationContext(), SignInActivity.class);
			startActivity(i);
			return;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// aut	omatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		
		long gapTime = TimeChecker.getInstance().getCurrentGapTimeFromBefore(this);
		if( gapTime < Config.GAP_TIME ) {
			SetState(STATE_FRIEND_LIST);
		}
		else {
			SetState(STATE_NEED_TO_UPDATE);
		}
	}
	
	
	
	public void SetState(int state) {
		if( currentState == state) {
			return;
		}
		currentState = state;
		changeFragmentByState();
	}
	
	private void changeFragmentByState() {
		Fragment newFragment = null;
		
		switch( currentState ) {
		case STATE_FRIEND_LIST:
		default:
			newFragment = new FriendListFragment();
			break;
		case STATE_NEED_TO_UPDATE:
			newFragment = new NeedToUpdateFragment();
			break;
		}
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.main_fragment_container, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
