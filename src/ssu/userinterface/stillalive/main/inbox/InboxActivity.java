package ssu.userinterface.stillalive.main.inbox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.common.AuthManager;
import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.common.HTTPHelper;
import ssu.userinterface.stillalive.common.HTTPHelper.OnResponseListener;
import ssu.userinterface.stillalive.main.RequestItem;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class InboxActivity extends Activity implements OnItemClickListener {
	
	private static final String TAG = "InboxActivity";
	
	
	String _accessToken;
	ListView _listView;
	InboxAdapter _adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		SharedPreferences pref = getSharedPreferences("default", MODE_PRIVATE);
		_accessToken = pref.getString("accessToken", "");
		
		
		_adapter = new InboxAdapter(this, R.layout.inbox_list_row);
		
		_listView = (ListView) findViewById(R.id.inbox_listView);
		_listView.setAdapter(_adapter);
		_listView.setOnItemClickListener(this);
	    
		GetListFromServer();
	}
	
	private void GetListFromServer(){
		Hashtable<String, String> parameters = new Hashtable<String, String>();
		parameters.put("access_token", _accessToken);
		HTTPHelper.GET(Config.HOST + "/users/received_requests", parameters, new OnResponseListener() {
			@Override
			public void OnResponse(String response) {
                try {
					JSONObject json = new JSONObject(response);
					int result = json.getInt("result");
					switch( result ) {
					case Config.RESULT_CODE_SUCCESS:
						OnSuccessToLoadList(json);
						break;
						
					case Config.RESULT_CODE_NOT_VALID_ACCESS_TOKEN:
					case Config.RESULT_CODE_EXPIRED_ACCESS_TOKEN:
						AuthManager.ShowAuthFailAlert(InboxActivity.this);
						break;
						
					default:
						Toast.makeText(InboxActivity.this, "Server error...", Toast.LENGTH_SHORT).show();
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	void OnSuccessToLoadList(JSONObject json) throws JSONException, ParseException {
		_adapter.clear();
		
		//2014-11-30T17:44:10.000Z
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		JSONArray data = json.getJSONArray("data");
		int length = data.length();
		for(int i = 0 ; i < length ; ++i){
			//{"id":2,"userID":"loki12","updatedAt":"2014-11-23T07:51:57.000Z"}
			JSONObject item = data.getJSONObject(i);
			int id = item.getInt("request_id");
			String userID = item.getString("sender_userid");
			Calendar calendar = Calendar.getInstance();
			Date tmp = formatter.parse(item.getString("date"));
			calendar.setTime(tmp);
			
			RequestItem request = new RequestItem(id, userID, calendar);
			_adapter.add(request);
			
		}
		
		_adapter.notifyDataSetChanged();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		final RequestItem item = _adapter.getItem(position);
		Hashtable<String, String> parameters = new Hashtable<String, String>();
		parameters.put("access_token", _accessToken);
		parameters.put("req_id", item.GetRequestID() + "");
		HTTPHelper.GET(Config.HOST + "/users/accept", parameters, new OnResponseListener() {
			@Override
			public void OnResponse(String response) {
				_adapter.remove(item);
                _adapter.notifyDataSetChanged();
			}
		});
	}
		

}
