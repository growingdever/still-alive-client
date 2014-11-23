package ssu.userinterface.stillalive.main;

import ssu.userinterface.stillalive.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NeedToUpdateFragment extends Fragment {
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_need_to_update, container, false);
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	
	public void OnClick(View view) {
		// just example
//		MainActivity parent = (MainActivity) getActivity();
//		parent.SetState(MainActivity.STATE_MAIN);
	}
	
}
