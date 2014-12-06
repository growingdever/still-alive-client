package ssu.userinterface.stillalive.main.friendlist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ssu.userinterface.stillalive.common.AuthManager;
import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.common.HTTPHelper;
import ssu.userinterface.stillalive.common.TimeChecker;
import ssu.userinterface.stillalive.common.HTTPHelper.OnResponseListener;
import ssu.userinterface.stillalive.main.SettingsActivity;
import ssu.userinterface.stillalive.main.UserData;
import ssu.userinterface.stillalive.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FriendListFragment extends Fragment implements OnClickListener, OnItemClickListener {
	
	private static final String TAG = "ListFragment";
	
	private ListView friendListView;
	private FriendListAdapter friendListAdapter;

	private Button btnAlive;
	
	String _accessToken;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedistanceState) {
		return inflater.inflate(R.layout.fragment_friend_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		SharedPreferences pref = getActivity().getSharedPreferences("default", Context.MODE_PRIVATE);
		_accessToken = pref.getString("accessToken", "");
		
		btnAlive = (Button) getView().findViewById(R.id.btnAlive);
		btnAlive.setOnClickListener(this);
		
		
		friendListAdapter = new FriendListAdapter(this.getActivity(), R.layout.friend_list_row);
		
		friendListView = (ListView) getView().findViewById(R.id.listView);
		friendListView.setAdapter(friendListAdapter);
		friendListView.setOnItemClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		getListFromServer();
		
		btnAlive.setOnClickListener(this);
	}

	//친구 상태 조회
	private void getListFromServer(){
		final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),"","잠시만 기다려 주세요.",true);
		
		SharedPreferences pref = getActivity().getSharedPreferences("default", Context.MODE_PRIVATE);
		String accessToken = pref.getString("accessToken", "");

		Hashtable<String, String> parameters = new Hashtable<String, String>();
		parameters.put("access_token", accessToken);
		HTTPHelper.GET(Config.HOST + "/list", parameters, new OnResponseListener() {
			@Override
			public void OnResponse(String response) {
				progressDialog.dismiss();
				
				JSONObject json = null;
				int result = 0;
				try {
					json = new JSONObject(response);
					result = json.getInt("result");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				switch( result ) {
				case Config.RESULT_CODE_SUCCESS:
					try {
						OnSuccessGetFriendList(json);
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(getActivity(), "Server error...", Toast.LENGTH_SHORT).show();
					} catch (ParseException e) {
						e.printStackTrace();
						Toast.makeText(getActivity(), "Server error...", Toast.LENGTH_SHORT).show();
					}
					break;
					
				case Config.RESULT_CODE_NOT_VALID_ACCESS_TOKEN:
				case Config.RESULT_CODE_EXPIRED_ACCESS_TOKEN:
					AuthManager.ShowAuthFailAlert(getActivity());
					break;
					
				default:
					Toast.makeText(getActivity(), "Server error...", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}
	
	private void updateToServer(){
		Hashtable<String, String> parameters = new Hashtable<String, String>();
		parameters.put("access_token", _accessToken);
		
		HTTPHelper.GET(Config.HOST + "/update", parameters, new OnResponseListener() {
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
					OnSuccessUpdate();
					break;
					
				case Config.RESULT_CODE_NOT_VALID_ACCESS_TOKEN:
				case Config.RESULT_CODE_EXPIRED_ACCESS_TOKEN:
					AuthManager.ShowAuthFailAlert(getActivity());
					break;
					
				default:
					Toast.makeText(getActivity(), "Server error...", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}
	
	private void OnSuccessGetFriendList(JSONObject json) throws JSONException, ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date localTime = new Date(System.currentTimeMillis());
		
		friendListAdapter.clear();
	    
		JSONArray data = json.getJSONArray("data");
		int length = data.length();
		for(int i = 0 ; i < length ; ++i){
			JSONObject item = data.getJSONObject(i);
			String userID = item.getString("userID");
			String stateMessage = item.getString("stateMessage");
			String phoneNumber = "01000000000";
			if( ! item.isNull("phoneNumber") ) {
				phoneNumber = item.getString("phoneNumber"); 
			}
			
			Date date = formatter.parse(item.getString("updatedAt"));
			Date localeDate = new Date(date.getTime() + TimeZone.getDefault().getOffset(localTime.getTime()));
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(localeDate);
			
			UserData person = new UserData(userID, phoneNumber, stateMessage, calendar);
			friendListAdapter.add(person);
		}
		
		friendListAdapter.notifyDataSetChanged();
	}

	void OnSuccessUpdate() {
		btnAlive.setOnClickListener(null);
		
		SharedPreferences pref = getActivity().getSharedPreferences("default", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("shouldUpdate", false);
        editor.commit();
		
		// start animation
		Animation scaleAnimatiton = AnimationUtils.loadAnimation(this.getActivity(), R.anim.scale_alive_button_no_delay);
		ImageView imageViewButtonBack = (ImageView) getView().findViewById(R.id.fragment_friend_list_alive_button_background);
		imageViewButtonBack.startAnimation(scaleAnimatiton);
		
		Animation rotation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotate_alive_button);
		btnAlive.startAnimation(rotation);
		rotation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				btnAlive.setOnClickListener(FriendListFragment.this);
			}
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		UserData user = friendListAdapter.getItem(position);
		DialogClickEvent event = new DialogClickEvent(user);
		event.show(getFragmentManager(), null);
	}
	
	@Override
	public void onClick(View v) {
		if( v.getId() == R.id.btnAlive ) {
			TimeChecker.getInstance().setCurrentTime(getActivity());
			getListFromServer();
			updateToServer();
		}
	}
}
