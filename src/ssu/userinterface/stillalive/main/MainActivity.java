package ssu.userinterface.stillalive.main;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.common.GCMActivity;
import ssu.userinterface.stillalive.common.HTTPHelper;
import ssu.userinterface.stillalive.common.TimeChecker;
import ssu.userinterface.stillalive.main.friendlist.FriendListFragment;
import ssu.userinterface.stillalive.main.inbox.InboxActivity;
import ssu.userinterface.stillalive.main.needtoupdate.NeedToUpdateFragment;
import ssu.userinterface.stillalive.main.searchuser.SearchFriendsActivity;
import ssu.userinterface.stillalive.main.signin.SignInActivity;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Hashtable;

public class MainActivity extends GCMActivity {

	private static final String TAG = "MainActivity";	
	public static final int STATE_FRIEND_LIST = 1;
	public static final int STATE_NEED_TO_UPDATE = 2;
	private int currentState = STATE_FRIEND_LIST;

    String _accessToken;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SharedPreferences pref = getSharedPreferences("default", MODE_PRIVATE);
        _accessToken = pref.getString("accessToken", "");
		if (_accessToken.equals("")) {
			Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
			finish();
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}
        else if(id == R.id.action_find_user) {
            Intent intent = new Intent(this, SearchFriendsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_accept_request) {
        	Intent intent = new Intent(this, InboxActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_sign_out) {
            SignOut();
        }

		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();

        SharedPreferences pref = getSharedPreferences("default", MODE_PRIVATE);
        boolean shouldUpdate = pref.getBoolean("shouldUpdate", false);

		long gapTime = TimeChecker.getInstance().getCurrentGapTimeFromBefore(this);
		if( gapTime < Config.GAP_TIME && !shouldUpdate ) {
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
		
		if( currentState == STATE_NEED_TO_UPDATE ) {
			getActionBar().hide();
		} else {
			getActionBar().show();
		}
		
		if( getFragmentManager().getBackStackEntryCount() > 0 ) {
			getFragmentManager().popBackStack();
		}
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.add(R.id.main_fragment_container, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

    @Override
    public void onBackPressed() {
        if (currentState == STATE_NEED_TO_UPDATE) {
            this.finish();
        } else {
            super.onBackPressed();
        }
    }

    void SignOut() {
        Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put("access_token", _accessToken);
        HTTPHelper.GET(Config.HOST + "/auth/signout",
                parameters,
                new HTTPHelper.OnResponseListener() {
                    @Override
                    public void OnResponse(String response) {
                        SharedPreferences pref = getSharedPreferences("default", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("accessToken   ", "");
                        editor.commit();

                        TimeChecker.getInstance().removeTime(MainActivity.this);


                        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
