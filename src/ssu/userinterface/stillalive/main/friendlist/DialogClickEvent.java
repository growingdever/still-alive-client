package ssu.userinterface.stillalive.main.friendlist;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.main.UserData;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

public class DialogClickEvent extends DialogFragment{
	UserData u;
	int checkID;
    public DialogClickEvent(UserData user)
    {
    	u=user;
    	
    }
	
	public Dialog onCreateDialog(Bundle saveInstanceState){
		
		
		
		LayoutInflater inflater=(LayoutInflater)getActivity().getLayoutInflater();
		final View view =inflater.inflate(R.layout.dialog_click_event, null);
		RadioGroup group1 = (RadioGroup) view.findViewById(R.id.group);
		
		AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
		group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() 
		{
			  @Override
			  public void onCheckedChanged(RadioGroup group, int checkedId) 
			  {
				 
				  switch (checkedId) 
			    {
			      case R.id.rbutton1:
			    			 
			    	  checkID=1;  
			          break;
			        
			      case R.id.rbutton2:
			    	  checkID=2;  
			          break;
			          
			      case R.id.rbutton3:
			    	  checkID=3;  
			          break;
			  		    }
			  		  }
			  		});
		builder.setView(view)
		.setTitle("What do you do")
		.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				check(checkID,u);

			}
		});
		return builder.create() ;
}
	public void check(int c,UserData user)
	{
		switch(c){
		
		case 1:
			String num=user.GetPhoneNumber();
			Intent second_intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("tel:"+num));
			Log.d("test",num);
			startActivity(second_intent);
			break;
		case 2:
			//문자 메시지
			break;
		case 3:
			// 카톡?
			break;
		}
	}}


