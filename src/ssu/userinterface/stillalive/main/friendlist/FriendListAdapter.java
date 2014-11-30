package ssu.userinterface.stillalive.main.friendlist;

import java.util.ArrayList;

import ssu.userinterface.stillalive.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FriendListAdapter extends ArrayAdapter<Person> {
	
	LayoutInflater _inflater;

	public FriendListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);

		_inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
        if( view == null ) {
            view = _inflater.inflate(R.layout.listview_row, null);
            
            Person person = getItem(position);
            
            TextView name = (TextView) view.findViewById(R.id.person_name);
            name.setText(person.getName());
             
            TextView time = (TextView) convertView.findViewById(R.id.person_time);
            time.setText(person.getTime());
        }
		
        return view;
	}
}
