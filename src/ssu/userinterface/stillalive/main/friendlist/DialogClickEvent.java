package ssu.userinterface.stillalive.main.friendlist;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.common.HTTPHelper;
import ssu.userinterface.stillalive.common.HTTPHelper.OnResponseListener;
import ssu.userinterface.stillalive.main.UserData;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DialogClickEvent extends DialogFragment {
	
	UserData u;
	int checkID;
	String phoneNum;
	

	public DialogClickEvent(UserData user) {
		u = user;

	}

	public Dialog onCreateDialog(Bundle saveInstanceState) {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_click_event, null);
		RadioGroup group1 = (RadioGroup) view.findViewById(R.id.group);
		phoneNum = u.GetPhoneNumber();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbutton1:
					checkID = 1;
					break;
				case R.id.rbutton2:
					checkID = 2;
					break;
				case R.id.rbutton3:
					checkID = 3;
					break;
				}
			}
		});
		builder.setView(view)
				.setTitle("What do you do")
				.setPositiveButton("confirm",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								check(checkID);
							}
						});
		return builder.create();
	}

	public void check(int c) {
		switch (c) {

		case 1:
		{
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
					Uri.parse("tel:" + phoneNum));
			startActivity(intent);
		}
			break;
		case 2:
		{
			// 문자 메시지
			Intent intent = new Intent(Intent.ACTION_SENDTO, 
					Uri.parse("smsto:"	+ phoneNum));
			startActivity(intent);
		}
			break;
		case 3:
		{
			// 요첫 메시지
			pokeToUser(u);
		}
			break;
		}
	}
	
	void pokeToUser(UserData target){
		SharedPreferences pref = getActivity().getSharedPreferences("default", Context.MODE_PRIVATE);
		String accessToken = pref.getString("accessToken", "");
		
		Hashtable<String, String> parameters = new Hashtable<String, String>();
		parameters.put("access_token", accessToken);
		parameters.put("target_userid", target.GetUserID());
		
		HTTPHelper.GET(Config.HOST + "/users/poke", parameters, new OnResponseListener() {
			@Override
			public void OnResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					if (json.getInt("result") == 1) {
						Toast.makeText(getActivity(), "Poked!", Toast.LENGTH_SHORT).show();
					}else{
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
