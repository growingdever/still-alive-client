package ssu.userinterface.stillalive.gcm;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.main.MainActivity;
import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class GcmIntentService extends IntentService {
	
	public static final int NOTIFICATION_ID = 1;
	private static final String TAG = "GcmIntentService";
	SharedPreferences updateCK;
	
	
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super(TAG);
       
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
       updateCK= this.getSharedPreferences("key", Context.MODE_PRIVATE);
       SharedPreferences.Editor editor=updateCK.edit();
      
        if (!extras.isEmpty()) {
            if (extras.getString("type").equals("1")) {
            	editor.putInt("checkID", 0);
                editor.commit();
            	sendNotification("Request ALIVE: " + extras.getString("senderUserID")+"has request to your alive");
                
            } 
            else if (extras.getString("type").equals("2")) {
                sendNotification("Friend Invitation: " + extras.getString("senderUserID")+"has requested to you");
            }
           
        }
        
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 
        		0, 
        		new Intent(this, MainActivity.class), 
        		0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("GCM Notification")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
