package ssu.userinterface.stillalive.main.searchuser;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.main.Person;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchResultAdapter extends ArrayAdapter<Person> {
	
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
            
            Person person = getItem(position);
            
            TextView textViewID = (TextView) view.findViewById(R.id.search_friends_result_list_row_textview_id);
            textViewID.setText(person.getName());
        }
		
        return view;
	}
}
