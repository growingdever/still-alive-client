package ssu.userinterface.stillalive.main;

import java.util.Calendar;

public class UserData {
	
	String _userID;
	String _phoneNumber;
	Calendar _lastUpdateAt;
	
	
	public UserData(String userid, String phoneNumber, Calendar lastUpdateAt){
		_userID = userid;
		_phoneNumber = phoneNumber;
		_lastUpdateAt = lastUpdateAt;
	}
	
	public String GetUserID() {
		return _userID;
	}
	
	public String GetPhoneNumber() {
		return _phoneNumber;
	}
	
	public Calendar GetLastUpdateTime() {
		return _lastUpdateAt;
	}

}
