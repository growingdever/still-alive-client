package ssu.userinterface.stillalive.main.needtoupdate;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.common.TimeChecker;
import ssu.userinterface.stillalive.main.MainActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NeedToUpdateFragment extends Fragment implements OnClickListener, AnimationListener {
	
	private static final String TAG = "NeedToUpdateFragment";
	Button _buttonAlive;
	ImageView _imageViewButtonBack;
	TextView _message;
	Animation _scaleAnimation;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_need_to_update, container, false);
	}

	
    @Override
    public void onResume() {
        super.onResume();

        long gap = TimeChecker.getInstance().getCurrentGapTimeFromBefore(getActivity());
        if (gap <= 1000 * 600 & gap >= 0) {
            _message.setText("Hi there!");
        } else if (gap >= 1000 * 600 && gap <= 900 * 1000) {
            _message.setText("Hah?");
        } else if (gap >= 900 * 1000 && gap <= 1500 * 1000) {
            _message.setText("Anybody there?");
        } else if (gap >= 1500 * 1000 && gap <= 1800 * 1000) {
            _message.setText("A-yo man, are you alive?");
        } else {
            _message.setText("Please tell me your safety!");
        }
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        View layout = getView().findViewById(R.id.fragment_need_to_update_container);
        layout.setOnClickListener(this);

        _scaleAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.scale_alive_button);
        _scaleAnimation.setAnimationListener(this);
		_imageViewButtonBack = (ImageView) getView().findViewById(R.id.fragment_need_to_update_button_background);
		_imageViewButtonBack.startAnimation(_scaleAnimation);
		_message = (TextView)getView().findViewById(R.id.message);
		Animation rotation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotate_alive_button);
		rotation.setRepeatCount(Animation.INFINITE);

        _buttonAlive = (Button) getView().findViewById(R.id.btnAlive);
		_buttonAlive.setOnClickListener(this);
		_buttonAlive.startAnimation(rotation);

        _imageViewButtonBack.setOnClickListener(this);
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAlive:
            case R.id.fragment_need_to_update_button_background:
                OnClickButtonAlive();
                break;
        }
    }

    void OnClickButtonAlive() {
        SharedPreferences pref = getActivity().getSharedPreferences("default", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("shouldUpdate", false);
        editor.commit();

		TimeChecker.getInstance().setCurrentTime(getActivity());
		SetStateToMain();
	}
	
	void SetStateToMain() {
		MainActivity parent = (MainActivity) getActivity();
		parent.SetState(MainActivity.STATE_FRIEND_LIST);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		_imageViewButtonBack.startAnimation(_scaleAnimation);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}
	
}
