package ssu.userinterface.stillalive.gcm;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.main.MainActivity;
import ssu.userinterface.stillalive.main.inbox.InboxActivity;

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


    public GcmIntentService() {
        super(TAG);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty()) {
            String messageType = extras.getString("type");
            if (messageType.equals("1")) {
                SendUpdatingRequestNotification(extras.getString("senderUserID"));

                SharedPreferences pref = getSharedPreferences("default", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("shouldUpdate", true);
                editor.commit();
            } else if (extras.getString("type").equals("2")) {
                SendFriendRequestNotification(extras.getString("senderUserID"));
            }

        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    void SendUpdatingRequestNotification(String senderID) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String msg = senderID + " want to know your safety";

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("StillAlive")
                        .setTicker("StillAlive")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    void SendFriendRequestNotification(String senderID) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String msg = senderID + " like to be your friend";

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0,
                new Intent(this, InboxActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("StillAlive")
                        .setTicker("StillAlive")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
