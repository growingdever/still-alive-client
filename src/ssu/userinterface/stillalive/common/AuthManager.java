package ssu.userinterface.stillalive.common;

import ssu.userinterface.stillalive.main.signin.SignInActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class AuthManager {
	
	public static void ShowNeedToSignInAlert(final Context context) {
		new AlertDialog.Builder(context)
		.setTitle("You have not signed in")
	    .setMessage("Please sign in to use our app.")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	    	
	    	@Override
	        public void onClick(DialogInterface dialog, int which) {
	    		Intent intent = new Intent(context, SignInActivity.class);
	    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(intent);
				((Activity) context).finish();
	        }
	    })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .show();
	}

	public static void ShowAuthFailAlert(final Context context) {
		new AlertDialog.Builder(context)
		.setTitle("You have signed on other devices")
	    .setMessage("If you want to use this device, you need to re-sign in.")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	    	
	    	@Override
	        public void onClick(DialogInterface dialog, int which) {
	    		Intent intent = new Intent(context, SignInActivity.class);
	    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(intent);
				((Activity) context).finish();
	        }
	    })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .show();
	}
	
}
