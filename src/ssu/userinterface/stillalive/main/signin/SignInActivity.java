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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends GCMActivity {

	private static final String TAG = "SignInActivity";

	EditText _editTextID;
	EditText _editTextPassword;
    Button _buttonSignIn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);

		_editTextID = (EditText) findViewById(R.id.signin_edittext_id);
		_editTextPassword = (EditText) findViewById(R.id.signin_edittext_password);
        _editTextPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    _buttonSignIn.callOnClick();
                    return true;
                }
                return false;
            }
        });

        _buttonSignIn = (Button) findViewById(R.id.signin_button_signin);

		if (checkPlayServices() && getRegistrationId(getApplicationContext()).isEmpty()) {
			registerInBackground();
		}
	}

	public void OnClickSignIn(View view) {
		String id = _editTextID.getText().toString();
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
							try {
								JSONObject json = new JSONObject(response);
								int check=json.getInt("result");
								if (check == 2) {
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
								editor.commit();
								
								OnSuccess();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}
	}

    public void OnClickSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

	void OnSuccess() {
		Toast.makeText(getApplicationContext(), "Success login", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	}
	
	public void OnFailed()
	{
		Toast.makeText(getApplicationContext(), "Fail to login", Toast.LENGTH_LONG).show();
		Toast.makeText(getApplicationContext(), "Check your ID and PASSWORD", Toast.LENGTH_LONG).show();
	}
}
