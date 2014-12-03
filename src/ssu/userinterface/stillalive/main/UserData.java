package ssu.userinterface.stillalive.main;

import java.util.Calendar;

public class UserData {
	
	String _userID;
	String _phoneNumber;
	String _stateMessage;
	Calendar _lastUpdateAt;
	
	
	public UserData(String userid, String phoneNumber, String stateMessage, Calendar lastUpdateAt){
		_userID = userid;
		_phoneNumber = phoneNumber;
		_stateMessage = stateMessage;
		_lastUpdateAt = lastUpdateAt;
	}
	
	public String GetUserID() {
		return _userID;
	}
	
	public String GetPhoneNumber() {
		return _phoneNumber;
	}
	
	public String GetStateMessage() {
		return _stateMessage;
	}
	
	public Calendar GetLastUpdateTime() {
		return _lastUpdateAt;
	}

}
