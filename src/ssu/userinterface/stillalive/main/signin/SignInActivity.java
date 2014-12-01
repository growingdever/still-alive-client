package ssu.userinterface.stillalive.main.signin;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.common.GCMActivity;
import ssu.userinterface.stillalive.common.HTTPHelper;
import ssu.userinterface.stillalive.main.MainActivity;
import ssu.userinterface.stillalive.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends GCMActivity{

	private static final String TAG = "SignInActivity";

	EditText _editTextID;
	EditText _editTextPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);
		Log.d("test","111111111111");
		_editTextID = (EditText) findViewById(R.id.idText);
		_editTextPassword = (EditText) findViewById(R.id.passwordText);

		if (checkPlayServices()
				&& getRegistrationId(getApplicationContext()).isEmpty()) {
			registerInBackground();
		}
	}

	public void OnClickLogIn(View view) {
		String id = _editTextID.getText().toString();
		String password = _editTextPassword.getText().toString();
		//Toast.makeText(getApplicationContext(), "동작", Toast.LENGTH_LONG).show();
		if (!id.equals("") && !password.equals("")) {
			Hashtable<String, String> parameters = new Hashtable<String, String>();
			parameters.put("userid", id);
			parameters.put("password", password);
			parameters.put("gcm_reg_id", getRegistrationId(getApplicationContext()));

			HTTPHelper.GET(Config.HOST + "/auth/signin", parameters,
					new HTTPHelper.OnResponseListener() {

						@Override
						public void OnResponse(String response) {
							// TODO Auto-generated method stub
							try {
								JSONObject json = new JSONObject(response);
								if (json.getInt("result") != 1) {
									//OnFailed();
									Toast.makeText(getApplicationContext(), "Check your ID and PASSWORD", Toast.LENGTH_LONG).show();
									return;
								}

								String accessToken = json
										.getString("accessToken");
								if (accessToken == null) {
									//OnFailed();
									Toast.makeText(getApplicationContext(), "Check your ID and PASSWORD", Toast.LENGTH_LONG).show();
									return;
								}

								SharedPreferences pref = getSharedPreferences(
										"default", MODE_PRIVATE);
								SharedPreferences.Editor editor = pref.edit();
								editor.putString("accessToken", accessToken);
								editor.commit();
								Toast.makeText(getApplicationContext(), "로그인", Toast.LENGTH_LONG).show();
								OnSuccess();

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
		}
	} 

	void OnSuccess() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	}
	
	public void signin(View view){
		Intent gointent = new Intent(getApplicationContext(),SignUpActivity.class);
		startActivity(gointent);
	}

	void OnFailed() {
		Intent goback=new Intent(getApplicationContext(),SignInActivity.class);
		startActivity(goback);
	}

}
