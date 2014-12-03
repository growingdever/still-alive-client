package ssu.userinterface.stillalive.main.searchuser;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.common.HTTPHelper;
import ssu.userinterface.stillalive.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class SearchFriendsActivity extends Activity implements OnQueryTextListener, OnItemClickListener {
	
	private static final String TAG = "SearchFriendsActivity";
	
	SearchView _searchView;
	ListView _listView;
	SearchResultAdapter _adapter;
	
	String _accessToken = "";
	
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friends);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		SharedPreferences pref = getSharedPreferences("default", MODE_PRIVATE);
		_accessToken = pref.getString("accessToken", "");		
		
		_adapter = new SearchResultAdapter(this, R.layout.search_result_list_row);
		_listView = (ListView)findViewById(R.id.search_friends_listView);
		_listView.setOnItemClickListener(this);
		_listView.setAdapter(_adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_friends, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		_searchView = (SearchView) searchItem.getActionView();
		_searchView.setOnQueryTextListener(this);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_search:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public boolean onQueryTextChange(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Log.d(TAG, "onQueryTextSubmit");
		
		final ProgressDialog progressDialog = ProgressDialog.show(SearchFriendsActivity.this,"","잠시만 기다려 주세요.",true);
		
		String searchText = query;
		Hashtable<String, String> parameters = new Hashtable<String, String>();
		parameters.put("keyword", searchText);
		parameters.put("access_token", _accessToken);
		
		HTTPHelper.GET(Config.HOST + "/users/search", parameters,
				new HTTPHelper.OnResponseListener() {
					@Override
					public void OnResponse(String response) {
						Log.i(TAG, response);
						progressDialog.dismiss();
						try {
							JSONObject json = new JSONObject(response);
							if (json.getInt("result") == 1) {
								onSuccess(json);
							}else{
								onFail(json);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						Toast.makeText(getApplicationContext(), "아이디를 클릭하여 친구 요청을 보내세요.", Toast.LENGTH_SHORT).show();
					}
				});
		_searchView.clearFocus();
		return true;
	}
	
	private void onSuccess(JSONObject json) throws JSONException {
		_adapter.clear();
		
		JSONArray jsonArray = json.getJSONArray("data");
		int size = jsonArray.length();
		for(int i = 0 ; i < size ; ++i){
			JSONObject item = jsonArray.getJSONObject(i);
			String userID = item.getString("userID");
			boolean isSent = item.getBoolean("sent");
			boolean isFriend = item.getBoolean("friend");
			int reqID = -1;
			if( !item.isNull("reqID") ) {
				reqID = item.getInt("reqID");
			}
			
			Log.d(TAG, userID + " " + reqID);
			SearchResultData data = new SearchResultData(userID, isSent, isFriend, reqID);
			_adapter.add(data);
		}
		_adapter.notifyDataSetChanged();
	}
	
	private void onFail(JSONObject json) throws JSONException {
		
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SearchResultData data = _adapter.getItem(position);
		if( data.GetIsSent() ) {
			// show cancel request dialog
			ShowCancelDialog(position);
			return;
		}
		
		requestFriend(data);
	}
	
	void ShowCancelDialog(final int index) {
		new AlertDialog.Builder(this)
		.setTitle("Cancel request")
	    .setMessage("Are you sure you want to cancel request?")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	    	
	    	@Override
	        public void onClick(DialogInterface dialog, int which) {
	    		SearchResultData data = _adapter.getItem(index);
	    		SendReject(data);
	    		data.SetIsSent(false);
	    		_adapter.notifyDataSetChanged();
	        }
	    })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	    	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		})
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .show();
	}
	
	void SendReject(final SearchResultData data) {
		Hashtable<String, String> parameters = new Hashtable<String, String>();
		parameters.put("access_token", _accessToken);
		parameters.put("req_id", data.GetReqID() + "");
		HTTPHelper.GET(Config.HOST + "/users/cancel", parameters,
				new HTTPHelper.OnResponseListener() {
					@Override
					public void OnResponse(String response) {
						Log.i(TAG, response);
						try {
							JSONObject json = new JSONObject(response);
							if (json.getInt("result") == 1) {
								Toast.makeText(getApplicationContext(),	"친구신청 완료", Toast.LENGTH_SHORT).show();
								data.SetIsSent(true);
								_adapter.notifyDataSetChanged();
							} else {
								
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	// 친구 요청
	private void requestFriend(final SearchResultData data) {
		Hashtable<String, String> parameters = new Hashtable<String, String>();
		parameters.put("access_token", _accessToken);
		parameters.put("target_userid", String.valueOf(data.GetUserID()));
		HTTPHelper.GET(Config.HOST + "/users/ask", parameters,
				new HTTPHelper.OnResponseListener() {
					@Override
					public void OnResponse(String response) {
						Log.i(TAG, response);
						try {
							JSONObject json = new JSONObject(response);
							if (json.getInt("result") == 1) {
								Toast.makeText(getApplicationContext(),	"친구신청 완료", Toast.LENGTH_SHORT).show();
								data.SetIsSent(true);
								_adapter.notifyDataSetChanged();
							} else {
								
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
}
