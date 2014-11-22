package ssu.userinterface.stillalive;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends GCMActivity {

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

		if (checkPlayServices()
				&& getRegistrationId(getApplicationContext()).isEmpty()) {
			registerInBackground();
		}
	}

	public void OnClickSignIn(View view) {
		String id = _editTextID.getText().toString();
		String password = _editTextPassword.getText().toString();
		Toast.makeText(getApplicationContext(), "µø¿€", Toast.LENGTH_LONG).show();
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
									OnFailed();
									return;
								}

								String accessToken = json
										.getString("accessToken");
								if (accessToken == null) {
									OnFailed();
									return;
								}

								SharedPreferences pref = getSharedPreferences(
										"default", MODE_PRIVATE);
								SharedPreferences.Editor editor = pref.edit();
								editor.putString("accessToken", accessToken);
								editor.commit();

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

		startActivity(intent);
	}

	void OnFailed() {

	}

}
