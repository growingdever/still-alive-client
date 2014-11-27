package ssu.userinterface.common;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

public class HTTPHelper {
	private static final String TAG = "HTTPHelper";
	HttpClient httpClient;

	private final static HTTPHelper instance = new HTTPHelper();

	public static HTTPHelper GetInstance() {
		return instance;
	}

	private HTTPHelper() {
		httpClient = new DefaultHttpClient();
	}

	public static void GET(String url, Hashtable<String, String> parameters, final OnResponseListener callback) {
		StringBuilder stringBuilder = new StringBuilder(url);
		if (parameters.size() > 0) {
			stringBuilder.append('?');

			Iterator<Hashtable.Entry<String, String>> it = parameters.entrySet().iterator();
			while (it.hasNext()) {
				Hashtable.Entry<String, String> entry = it.next();
				stringBuilder.append(entry.getKey());
				stringBuilder.append('=');
				stringBuilder.append(entry.getValue());
				if (it.hasNext()) {
					stringBuilder.append('&');
				}
			}
		}

		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String result = "";

				HttpClient httpClient = HTTPHelper.GetInstance().httpClient;

				String url = params[0];
				HttpGet request = new HttpGet(url);
				Log.i(TAG, url);
				try {
					HttpResponse response = httpClient.execute(request);
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						// 결과를 처리합니다.
						result = EntityUtils.toString(entity);
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				callback.OnResponse(result);
			}

		}.execute(stringBuilder.toString());
	}

	public interface OnResponseListener {
		public void OnResponse(String response);
	}

}
