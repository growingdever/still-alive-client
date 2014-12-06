package ssu.userinterface.stillalive.common;

public class Config {
	public static final String HOST = "http://211.110.33.59:7778";
	public static final long GAP_TIME = 3010*1000; // 화면 전환 interval Time

    public static final int RESULT_CODE_SUCCESS = 1;
    public static final int RESULT_CODE_ALREADY_EXIST_USERID = 3;
    public static final int RESULT_CODE_NOT_VALID_ACCESS_TOKEN = 6;
    public static final int RESULT_CODE_EXPIRED_ACCESS_TOKEN = 7;
}
