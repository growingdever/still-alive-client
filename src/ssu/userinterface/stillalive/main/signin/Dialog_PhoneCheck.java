package ssu.userinterface.stillalive.main.signin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.R.id;
import ssu.userinterface.stillalive.R.layout;
public class Dialog_PhoneCheck extends DialogFragment {

	public Dialog onCreateDialog(Bundle saveInstanceState)
	{
		
		LayoutInflater inflater=(LayoutInflater)getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_phonecheck, null);
		final EditText edit=(EditText)view.findViewById(R.id.editcheck);
		AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
		builder.setView(view)
		.setTitle("enter your checknumber")
		.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String entercheck=edit.getText().toString();
				if(Integer.parseInt(entercheck)!= SignUpActivity.rescheck)
				{
					Toast.makeText(getActivity(), "Wrong check number", Toast.LENGTH_LONG).show();
					SignUpActivity.correctPhone=0;
					
					
				}
				else
				{
					Toast.makeText(getActivity(), "Correct check number", Toast.LENGTH_LONG).show();
					
					SignUpActivity.correctPhone=1;
				}
			}
		});
		return builder.create();
		
	}
		

}
