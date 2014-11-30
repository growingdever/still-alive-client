package ssu.userinterface.stillalive.main.inbox;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.main.Person;
import ssu.userinterface.stillalive.main.RequestItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class InboxAdapter extends ArrayAdapter<RequestItem> {
	
	LayoutInflater _inflater;

	public InboxAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);

		_inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
        if( view == null ) {
        	view = _inflater.inflate(R.layout.inbox_list_row, null);
            
        	RequestItem req = getItem(position);
            
            TextView textViewID = (TextView) view.findViewById(R.id.inbox_list_row_textview);
            textViewID.setText(req.GetRequestID() + " " + req.GetSenderUserID());
        }
		
        return view;
	}
}
