package ssu.userinterface.stillalive.main.signin;

import java.util.Hashtable;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.common.GCMActivity;
import ssu.userinterface.stillalive.common.HTTPHelper;
import ssu.userinterface.stillalive.main.MainActivity;
import ssu.userinterface.stillalive.R;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends GCMActivity{
	private static final String TAG = "SignUpActivity";
	EditText _editTextID;
	EditText _editTextPassword;
	EditText _editTextNAME;
	EditText _editTextPhone;
	String phone;
	Button giveBT,receiveBT;
	public static int correctPhone;
	public static int rescheck;
	FragmentManager fm=getFragmentManager();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		giveBT=(Button)findViewById(R.id.givebt);
		correctPhone=0;
		_editTextNAME = (EditText)findViewById(R.id.nameText);
		_editTextPhone= (EditText)findViewById(R.id.phoneText);
		_editTextID = (EditText) findViewById(R.id.idText);
		_editTextPassword = (EditText) findViewById(R.id.passwordText);
		final Random randCheck = new Random();
		if (checkPlayServices()
				&& getRegistrationId(getApplicationContext()).isEmpty()) {
			registerInBackground();
		}
		
		giveBT.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rescheck=randCheck.nextInt(9000)+1000;
				Dialog_PhoneCheck dialog=new Dialog_PhoneCheck();
				dialog.show(fm, "check");
				
				phone = _editTextPhone.getText().toString();
				SmsManager sms =SmsManager.getDefault();
				sms.sendTextMessage(phone, null, Integer.toString(rescheck), null, null);
				
				
				
				
			}
		});
	}
	
	public void onclickSignUp(View view) {
		String name = _editTextNAME.getText().toString();
		String phone = _editTextPhone.getText().toString();
		String id = _editTextID.getText().toString();
		String password = _editTextPassword.getText().toString();
		
		if (!id.equals("") && !password.equals("")&&correctPhone!=0 ) {
			Hashtable<String, String> parameters = new Hashtable<String, String>();
			parameters.put("name", name);
			parameters.put("phone", phone);
			parameters.put("userid", id);
			parameters.put("password", password);
			parameters.put("gcm_reg_id", getRegistrationId(getApplicationContext()));
			
			HTTPHelper.GET(Config.HOST + "/auth/regist", parameters,
					new HTTPHelper.OnResponseListener() {

						@Override
						public void OnResponse(String response) {
							// TODO Auto-generated method stub
							
							try {
								
								JSONObject json = new JSONObject(response);
					
								if (json.getInt("result") != 1) {
									
									Toast.makeText(getApplicationContext(), "Fail Joined!, Try another ID!", Toast.LENGTH_LONG).show();
									return;
								}
								else{
								
									Toast.makeText(getApplicationContext(), "Success Joined!", Toast.LENGTH_LONG).show();
									finish();
								}
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								
								e.printStackTrace();
							}
						}
					});
		}
		else
			Toast.makeText(getApplicationContext(), "Try check your Phone number", Toast.LENGTH_LONG).show();
			
	}

	void OnSuccess() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);

		startActivity(intent);
	}
	void OnFailed() {
			
	}

}
