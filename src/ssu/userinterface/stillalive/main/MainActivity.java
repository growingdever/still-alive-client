package ssu.userinterface.stillalive.main;

import ssu.userinterface.stillalive.GCMActivity;
import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.SignInActivity;
import ssu.userinterface.stillalive.R.id;
import ssu.userinterface.stillalive.R.layout;
import ssu.userinterface.stillalive.R.menu;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends GCMActivity {

	static final String TAG = "MainActivity";
	
	public static final int STATE_MAIN = 0;
	public static final int STATE_NEED_TO_UPDATE = 1;
	
	

	Context context;
	Fragment fragment;
	int currentState;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = getApplicationContext();
	
		SharedPreferences pref = getSharedPreferences("default", MODE_PRIVATE);
		Log.d(TAG, pref.getString("accessToken", ""));
		if (pref.getString("accessToken", "").equals("")) {
			Intent i = new Intent(context, SignInActivity.class);
			startActivity(i);
			return;
		}
		
		fragment = getFragmentManager().findFragmentById(R.id.main_fragment);
		
		currentState = STATE_MAIN;
		ChangeFragmentByState();
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
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void SetState(int state) {
		if( state == currentState ) {
			return;
		}
		
		currentState = state;
		ChangeFragmentByState();
	}
	
	private void ChangeFragmentByState() {
		Fragment newFragment = null;
		
		switch( currentState ) {
		case STATE_NEED_TO_UPDATE:
			break;
		case STATE_MAIN:
		default:
			newFragment = new MainFragment();
			break;
		}
		
		if( newFragment == null ) {
			return;
		}
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(fragment.getId(), newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

}
