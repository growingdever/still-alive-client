package ssu.userinterface.stillalive.main.searchuser;

public class SearchResultData {
	
	String _userID;
	boolean _isSent;
	boolean _isFriend;
	int _reqID;
	
	
	public SearchResultData(String id, boolean sent, boolean friend, int reqID) {
		_userID = id;
		_isSent = sent;
		_isFriend = friend;
		_reqID = reqID;
	}
	
	public String GetUserID() {
		return _userID;
	}
	
	public boolean GetIsSent() {
		return _isSent;
	}
	
	public void SetIsSent(boolean isSent) {
		_isSent = isSent;
	}
	
	public boolean GetIsFriend() {
		return _isFriend;
	}
	
	public int GetReqID() {
		return _reqID;
	}

}
