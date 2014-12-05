package ssu.userinterface.stillalive.main.inbox;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.main.RequestItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;

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
        }

        RequestItem req = getItem(position);

        TextView textViewID = (TextView) view.findViewById(R.id.inbox_list_row_textview);
        textViewID.setText(req.GetSenderUserID());


        Calendar now = Calendar.getInstance();
        Calendar last = req.GetSentDate();
        long diff = now.getTimeInMillis() - last.getTimeInMillis();

        TextView textViewSentAt = (TextView) view.findViewById(R.id.inbox_list_row_sent_at);

        long sec = diff / 1000;
        if( sec >= 86400 ) {
            textViewSentAt.setText("1d+");
        } else if( sec >= 3600) {
            long hour = sec / 3600;
            textViewSentAt.setText(hour + "h");
        } else if( sec >= 60 ) {
            long min = sec / 60;
            textViewSentAt.setText(min + "m");
        } else {
            textViewSentAt.setText(sec + "s");
        }

        return view;
	}
}
