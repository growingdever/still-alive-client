package ssu.userinterface.stillalive.main;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ssu.userinterface.common.Config;
import ssu.userinterface.common.HTTPHelper;
import ssu.userinterface.listview.Person;
import ssu.userinterface.stillalive.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchFriendsActivity extends Activity{
	private static final String TAG = "SearchFriendsActivity";
	private EditText etSearch;
	private Button btnSearch;
	private ListView listView;
	
	private ArrayAdapter<String> adapter;
	private ArrayList<Person> friends = new ArrayList<Person>();
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friends);
		
		etSearch = (EditText)findViewById(R.id.et_search);
		btnSearch = (Button)findViewById(R.id.btn_search);
		listView = (ListView)findViewById(R.id.listView);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Person person = friends.get(position);
				id = person.getId();
				Toast.makeText(SearchFriendsActivity.this, id+"에게 친구 신청", Toast.LENGTH_SHORT).show();
				requestFriend(person);
			}
			
		});
		
		// 아이디 검색
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final ProgressDialog progressDialog = ProgressDialog.show(SearchFriendsActivity.this,"","잠시만 기다려 주세요.",true);
				
				String searchText = etSearch.getText().toString();
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
			}
		});
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		listView.setAdapter(adapter);
	}
	
	private void onSuccess(JSONObject json) throws JSONException {
		adapter.clear();
		friends.clear();
		
		JSONArray jsonArray = json.getJSONArray("data");
		int size = jsonArray.length();
		for(int i = 0 ; i < size ; ++i){
			JSONObject item = jsonArray.getJSONObject(i);
			String userID = item.getString("userID");
			int id = item.getInt("id");
			
			Person person = new Person();
			person.setId(id);
			person.setName(userID);
			
			friends.add(person);
		}
		
		
		ArrayList<String> data = new ArrayList<String>();
		for(int i = 0; i < friends.size() ; ++i){
			data.add(friends.get(i).getName());
		}
		
		adapter.addAll(data);
		adapter.notifyDataSetChanged();
		
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
}
