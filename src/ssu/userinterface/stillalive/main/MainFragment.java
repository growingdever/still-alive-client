package ssu.userinterface.stillalive.main;

import ssu.userinterface.stillalive.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MainFragment extends Fragment {
	
	ListView listView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		listView = (ListView) getView().findViewById(R.id.main_listview_friend);
	}
	
	public void OnClick(View view) {
		// just example
//		MainActivity parent = (MainActivity) getActivity();
//		parent.SetState(MainActivity.STATE_NEED_TO_UPDATE);
	}
	
}
