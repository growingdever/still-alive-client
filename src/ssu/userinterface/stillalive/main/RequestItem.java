package ssu.userinterface.stillalive.main;

import java.util.Calendar;

public class RequestItem {
	int requestID;
	String senderUserID;
	Calendar sendedDate;
	
	public RequestItem(int reqID, String userID, Calendar date) {
		requestID = reqID;
		senderUserID = userID;
		sendedDate = date;
	}
	
	public int GetRequestID() {
		return requestID;
	}
	
	public String GetSenderUserID() {
		return senderUserID;
	}
	
	public Calendar GetSentDate() {
		return sendedDate;
	}
}
