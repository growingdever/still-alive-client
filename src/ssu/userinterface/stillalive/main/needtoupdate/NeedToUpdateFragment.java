package ssu.userinterface.stillalive.main.needtoupdate;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.common.TimeChecker;
import ssu.userinterface.stillalive.main.MainActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class NeedToUpdateFragment extends Fragment implements OnClickListener {
	
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
		btnAlive.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnAlive:
			OnClickButtonAlive();
			break;
		}
	}
	
	void OnClickButtonAlive() {
		TimeChecker.getInstance().setCurrentTime(getActivity());
		SetStateToMain();		
	}
	
	void SetStateToMain() {
		MainActivity parent = (MainActivity) getActivity();
		parent.SetState(MainActivity.STATE_FRIEND_LIST);
	}
	
}
