package ssu.userinterface.stillalive.main.friendlist;

import java.util.ArrayList;

import ssu.userinterface.stillalive.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

	private ArrayList<Person> list;

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        //if ( convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_row, parent, false);
             
            TextView name = (TextView) convertView.findViewById(R.id.person_name);
            name.setText(list.get(position).getName());
             
            TextView time = (TextView) convertView.findViewById(R.id.person_time);
            time.setText(list.get(position).getTime());
        //}
         
        return convertView;
	}
	
	public void setData(ArrayList<Person> data){
		list = data;
	}
}
