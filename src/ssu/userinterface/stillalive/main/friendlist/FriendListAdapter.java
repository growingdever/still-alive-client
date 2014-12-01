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
            
            UserData userdata = getItem(position);
            
            TextView name = (TextView) view.findViewById(R.id.person_name);
            name.setText(userdata.GetUserID());
            
            Calendar now = Calendar.getInstance();
            Calendar last = userdata.GetLastUpdateTime();
            long diff = now.getTimeInMillis() - last.getTimeInMillis();
            
            TextView time = (TextView) view.findViewById(R.id.person_time);
            if( diff / 1000 > 3600) {
            	time.setText("More than an hour");
            }
            else {
            	time.setText((diff / 1000) + " seconds");
            }
            
        }
		
        return view;
	}
}
