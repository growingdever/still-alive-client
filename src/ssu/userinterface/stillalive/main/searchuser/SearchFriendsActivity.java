package ssu.userinterface.stillalive.main.searchuser;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.common.HTTPHelper;
import ssu.userinterface.stillalive.main.Person;
import ssu.userinterface.stillalive.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friends);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		View view = findViewById(R.id.search_friends_background);
		view.setAlpha(0.7f);
		
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


	private void onSuccess(JSONObject json) throws JSONException {
		_adapter.clear();
		
		JSONArray jsonArray = json.getJSONArray("data");
		int size = jsonArray.length();
		for(int i = 0 ; i < size ; ++i){
			JSONObject item = jsonArray.getJSONObject(i);
			String userID = item.getString("userID");
			int id = item.getInt("id");
			
			Person person = new Person();
			person.setId(id);
			person.setName(userID);
			_adapter.add(person);
		}
		_adapter.notifyDataSetChanged();
		
		hideKeyboard();
	}
	
	private void onFail(JSONObject json) throws JSONException {
		
	}
	
	// 키보드 사라지게
	private void hideKeyboard(){
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	//친구 요청
	private void requestFriend(Person person){
		SharedPreferences pref = getSharedPreferences("default", MODE_PRIVATE);
		String accessToken = pref.getString("accessToken", "");
		String id = pref.getString("user_id", "");
		Hashtable<String, String> parameters = new Hashtable<String, String>();
		parameters.put("access_token", accessToken);
		parameters.put("source_userid", id);
		parameters.put("target_userid", String.valueOf(person.getName()));
		HTTPHelper.GET(Config.HOST + "/users/ask", parameters,
				new HTTPHelper.OnResponseListener() {
					@Override
					public void OnResponse(String response) {
						Log.i(TAG, response);
						try {
							//http://211.110.33.59:7778/users/ask?target_userid=loki11&source_userid=loki13&access_token=f4cfa11a-fc5b-410b-837a-c4dfddc3b423

							JSONObject json = new JSONObject(response);
							if (json.getInt("result") == 1) {
								Toast.makeText(getApplicationContext(), "핀구신청 완료", Toast.LENGTH_SHORT).show();
							}else{
								
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}



	@Override
	public boolean onQueryTextChange(String query) {
		return false;
	}



	@Override
	public boolean onQueryTextSubmit(String query) {
		final ProgressDialog progressDialog = ProgressDialog.show(SearchFriendsActivity.this,"","잠시만 기다려 주세요.",true);
		
		String searchText = query;
		Hashtable<String, String> parameters = new Hashtable<String, String>();
		parameters.put("keyword", searchText);
		
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
					}
				});
		return false;
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Person person = _adapter.getItem(position);
		requestFriend(person);
	}
}
