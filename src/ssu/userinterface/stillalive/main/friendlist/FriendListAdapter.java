package ssu.userinterface.stillalive.main.friendlist;

import java.util.Calendar;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.main.UserData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FriendListAdapter extends ArrayAdapter<UserData> {
	
	LayoutInflater _inflater;

	public FriendListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);

		_inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
        if( view == null ) {
            view = _inflater.inflate(R.layout.friend_list_row, null);
        }
        
        UserData userdata = getItem(position);
        
        TextView textViewUserID = (TextView) view.findViewById(R.id.friend_list_row_textview_userid);
        textViewUserID.setText(userdata.GetUserID());
        
        TextView textViewStateMessage = (TextView) view.findViewById(R.id.friend_list_row_textview_state_message);
        textViewStateMessage.setText(userdata.GetStateMessage());
        
        Calendar now = Calendar.getInstance();
        Calendar last = userdata.GetLastUpdateTime();
        long diff = now.getTimeInMillis() - last.getTimeInMillis();
        
        TextView time = (TextView) view.findViewById(R.id.friend_list_row_textview_pass_time);
        
        long sec = diff / 1000;
        if( sec >= 86400 ) {
        	time.setText("1d+");
        } else if( sec >= 3600) {
        	long hour = sec / 3600;
        	time.setText(hour + "h");
        } else if( sec >= 60 ) {
        	long min = sec / 60;
        	time.setText(min + "m");
        } else {
        	time.setText(sec + "s");
        }
		
        return view;
	}
}
