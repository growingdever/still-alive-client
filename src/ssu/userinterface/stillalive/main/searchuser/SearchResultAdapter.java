package ssu.userinterface.stillalive.main.searchuser;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.main.UserData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchResultAdapter extends ArrayAdapter<SearchResultData> {
	
	LayoutInflater _inflater;

	public SearchResultAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);

		_inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
        if( view == null ) {
            view = _inflater.inflate(R.layout.search_result_list_row, null);
        }
        

        SearchResultData data = getItem(position);
        
        TextView textViewID = (TextView) view.findViewById(R.id.search_friends_result_list_row_textview_id);
        textViewID.setText(data.GetUserID());
        
        TextView textViewState = (TextView) view.findViewById(R.id.search_friends_result_list_row_textview_state);
        if( data.GetIsFriend() ) {
        	textViewState.setText("your friend");
        	textViewState.setTextColor(getContext().getResources().getColor(R.color.list_row_text_color_state_invalid));
        }
        else if( data.GetIsSent() ) {
        	textViewState.setText("already sent");
        	textViewState.setTextColor(getContext().getResources().getColor(R.color.list_row_text_color_state_invalid));
        }
        else {
        	textViewState.setText("come on!");
        	textViewState.setTextColor(getContext().getResources().getColor(R.color.list_row_text_color_state_valid));
        }
		
        return view;
	}
}
