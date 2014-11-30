package ssu.userinterface.stillalive.main;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.common.HTTPHelper;
import ssu.userinterface.stillalive.GCMActivity;
import ssu.userinterface.stillalive.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SignInActivity extends GCMActivity{

	private static final String TAG = "SignInActivity";

	EditText _editTextID;
	EditText _editTextPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);
		_editTextID = (EditText) findViewById(R.id.signin_edittext_id);
		_editTextPassword = (EditText) findViewById(R.id.signin_edittext_password);
		if (checkPlayServices()&& getRegistrationId(getApplicationContext()).isEmpty()) {
			registerInBackground();
		}
	}

	public void OnClickSignIn(View view) {
		final String id = _editTextID.getText().toString();
		String password = _editTextPassword.getText().toString();

		if (!id.equals("") && !password.equals("")) {
			Hashtable<String, String> parameters = new Hashtable<String, String>();
			parameters.put("userid", id);
			parameters.put("password", password);
			parameters.put("gcm_reg_id", getRegistrationId(getApplicationContext()));
			
			HTTPHelper.GET(Config.HOST + "/auth/signin", parameters,
					new HTTPHelper.OnResponseListener() {
						@Override
						public void OnResponse(String response) {
							Log.i(TAG, response);
							try {
								JSONObject json = new JSONObject(response);
								if (json.getInt("result") != 1) {
									OnFailed();
									return;
								}

								String accessToken = json.getString("accessToken");
								if (accessToken == null) {
									OnFailed();
									return;
								}

								SharedPreferences pref = getSharedPreferences("default", MODE_PRIVATE);
								SharedPreferences.Editor editor = pref.edit();
								editor.putString("accessToken", accessToken);
								editor.putString("user_id", id);
								editor.commit();

								OnSuccess();

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}
	}

	void OnSuccess() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	void OnFailed() {

	}

}
