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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends GCMActivity {

    private static final String TAG = "SignUpActivity";

    EditText _editTextName;
    EditText _editTextID;
    EditText _editTextPassword;
    EditText _editTextPasswordCheck;
    EditText _editTextPhoneNumber;
    Button _buttonCreateAccount;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        _editTextName = (EditText) findViewById(R.id.signup_edittext_name);
        _editTextID = (EditText) findViewById(R.id.signup_edittext_id);
        _editTextPassword = (EditText) findViewById(R.id.signup_edittext_password);
        _editTextPasswordCheck = (EditText) findViewById(R.id.signup_edittext_password_check);
        _editTextPhoneNumber = (EditText) findViewById(R.id.signup_edittext_phonenumber);
        _buttonCreateAccount = (Button) findViewById(R.id.signup_button_create_count);

        _editTextName.setNextFocusDownId(R.id.signup_edittext_id);
        _editTextID.setNextFocusDownId(R.id.signup_edittext_password);
        _editTextPassword.setNextFocusDownId(R.id.signup_edittext_password_check);
        _editTextPasswordCheck.setNextFocusDownId(R.id.signup_edittext_phonenumber);

        _editTextPhoneNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    _buttonCreateAccount.callOnClick();
                    return true;
                }
                return false;
            }
        });

        if (checkPlayServices() && getRegistrationId(getApplicationContext()).isEmpty()) {
            registerInBackground();
        }
	}
	
	public void OnClickCreateAccount(View view) {
        String password = _editTextPassword.getText().toString();
        String passwordCheck = _editTextPasswordCheck.getText().toString();
        if( password.compareTo(passwordCheck) != 0 ) {
            Toast.makeText(this, "Please enter the same password", Toast.LENGTH_SHORT).show();
            return;
        }

		String name = _editTextName.getText().toString();
        if( name.equals("") ) {
            Toast.makeText(this, "Please enter the proper name", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = _editTextID.getText().toString();
        if( id.equals("") ) {
            Toast.makeText(this, "Please enter the proper name", Toast.LENGTH_SHORT).show();
            return;
        }

		String phone = _editTextPhoneNumber.getText().toString();
        if( phone.equals("") ) {
            Toast.makeText(this, "Please enter the proper phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Config.HOST + "/auth/regist/validate/id";
        Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put("userid", id);
        HTTPHelper.GET(url, parameters,
                new HTTPHelper.OnResponseListener() {
                    @Override
                    public void OnResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            if( json.getInt("result") == Config.RESULT_CODE_SUCCESS ) {
                                StartCertification();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Already exist ID", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
	}

    void StartCertification() {
        Random randCheck = new Random();
        int cert = randCheck.nextInt(9000)+1000;
        PhoneCheckDialog dialog = PhoneCheckDialog.NewInstance(new Runnable() {
            @Override
            public void run() {
                PostDataToServer();
            }
        }, cert);

        FragmentManager fragmentManager = getFragmentManager();
        dialog.show(fragmentManager, "PhoneCheckDialog");

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(_editTextPhoneNumber.getText().toString(),
                null,
                Integer.toString(cert),
                null,
                null);
    }

    void PostDataToServer() {
        String name = _editTextName.getText().toString();
        String id = _editTextID.getText().toString();
        String password = _editTextPassword.getText().toString();
        String phone = _editTextPhoneNumber.getText().toString();

        Hashtable<String, Object> parameters = new Hashtable<String, Object>();
        parameters.put("nickname", name);
        parameters.put("userid", id);
        parameters.put("password", password);
        parameters.put("phone_number", phone);
        parameters.put("gcm_reg_id", getRegistrationId(getApplicationContext()));

        HTTPHelper.POST(Config.HOST + "/auth/regist",
                "application/json",
                parameters,
                new HTTPHelper.OnResponseListener() {

                    @Override
                    public void OnResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            int result = json.getInt("result");
                            if (result == 1) {
                                String accessToken = json.getString("accessToken");
                                SharedPreferences pref = getSharedPreferences("default", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("accessToken", accessToken);
                                editor.commit();

                                OnSuccess();
                            } else {
                                OnFailed(result);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

	void OnSuccess() {
		Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
	}

	void OnFailed(int check) {
        switch(check) {
            case Config.RESULT_CODE_ALREADY_EXIST_USERID:
                Toast.makeText(getApplicationContext(), "Already exist ID", Toast.LENGTH_SHORT).show();
                break;
        }
	}

}
