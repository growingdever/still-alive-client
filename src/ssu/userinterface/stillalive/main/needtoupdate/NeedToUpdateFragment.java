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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class NeedToUpdateFragment extends Fragment implements OnClickListener, AnimationListener {
	
	private static final String TAG = "NeedToUpdateFragment";
	
	Button _buttonAlive;
	ImageView _imageViewButtonBack;
	
	Animation _scaleAnimatiton;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedistanceState) {
		return inflater.inflate(R.layout.fragment_need_to_update, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		_scaleAnimatiton = AnimationUtils.loadAnimation(this.getActivity(), R.anim.scale_alive_button);
		_scaleAnimatiton.setAnimationListener(this);
		_imageViewButtonBack = (ImageView) getView().findViewById(R.id.fragment_need_to_update_button_background);
		_imageViewButtonBack.startAnimation(_scaleAnimatiton);
		
		Animation rotation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotate_alive_button);
		rotation.setRepeatCount(Animation.INFINITE);
		_buttonAlive = (Button) getView().findViewById(R.id.btnAlive);
		_buttonAlive.setOnClickListener(this);
		_buttonAlive.startAnimation(rotation);
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

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		_imageViewButtonBack.startAnimation(_scaleAnimatiton);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	
}
