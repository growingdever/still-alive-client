package ssu.userinterface.stillalive.gcm;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.common.Config;
import ssu.userinterface.stillalive.main.MainActivity;
import ssu.userinterface.stillalive.main.inbox.InboxActivity;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GcmIntentService";


    public GcmIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        
        if (!extras.isEmpty()) {
            int messageType = Integer.parseInt(extras.getString("type"));
            if (messageType == Config.PUSH_MESSAGE_TYPE_POKE) {
            	String senderID = extras.getString("senderUserID");
           		SendUpdatingRequestNotification(senderID);

                SharedPreferences pref = getSharedPreferences("default", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("shouldUpdate", true);
                editor.commit();
            } else if (messageType == Config.PUSH_MESSAGE_TYPE_FRIEND_REQUEST) {
            	String senderID = extras.getString("senderUserID");
           		SendFriendRequestNotification(senderID);
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    void SendUpdatingRequestNotification(String senderID) {
    	boolean useNotification = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_setting", true);
    	if( !useNotification ) {
    		return;
    	}
    	
        Uri ringtoneURI = Uri.parse(PreferenceManager.getDefaultSharedPreferences(this).getString("notifications_ringtone", ""));
        boolean useVibrate = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_vibrate", true);
        String msg = senderID + " want to know your safety";
        
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("StillAlive")
                        .setTicker("StillAlive")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);
        if( useVibrate ) {
        	builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        }
        
        Notification notification = builder.build();
        notification.sound = ringtoneURI;
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    void SendFriendRequestNotification(String senderID) {
    	boolean useNotification = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_setting", true);
    	if( !useNotification ) {
    		return;
    	}
    	
    	Uri ringtoneURI = Uri.parse(PreferenceManager.getDefaultSharedPreferences(this).getString("notifications_ringtone", ""));
    	boolean useVibrate = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_vibrate", true);
        String msg = senderID + " like to be your friend";
        
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0,
                new Intent(this, InboxActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("StillAlive")
                        .setTicker("StillAlive")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);
        if( useVibrate ) {
        	builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        }
        
        Notification notification = builder.build();
        notification.sound = ringtoneURI;
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
