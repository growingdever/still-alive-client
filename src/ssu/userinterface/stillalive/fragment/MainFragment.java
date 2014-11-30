package ssu.userinterface.stillalive.fragment;

import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.common.TimeChecker;
import ssu.userinterface.stillalive.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainFragment extends Fragment {
	private static final String TAG = "MainFragment";
	private ImageButton btnAlive;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedistanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btnAlive = (ImageButton) getView().findViewById(R.id.btnAlive);
		btnAlive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TimeChecker.getInstance().setCurrentTime(getActivity());
				Toast.makeText(getActivity(), "Alive", Toast.LENGTH_SHORT).show();
				nextFragment();
			}
		});
		long gapTime = TimeChecker.getInstance().getCurrentGapTimeFromBefore(getActivity());
		Log.i(TAG, "Gap Time is "+gapTime+" and is < "+Config.GAP_TIME);
		if(gapTime< Config.GAP_TIME){
			nextFragment();
		}
	}
	
	private void nextFragment(){
		ListFragment fr  = new ListFragment();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.add(android.R.id.content, fr);
		fragmentTransaction.commit();
	}
}
