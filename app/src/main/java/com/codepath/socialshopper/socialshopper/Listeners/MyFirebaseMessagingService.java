package com.codepath.socialshopper.socialshopper.Listeners;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.codepath.socialshopper.socialshopper.Activities.NotificationActivity;
import com.codepath.socialshopper.socialshopper.Activities.PickUpListActivity;
import com.codepath.socialshopper.socialshopper.Activities.TrackStatusActivity;
import com.codepath.socialshopper.socialshopper.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


/**
 * Created by saripirala on 10/11/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String YES_ACTION = "YES_ACTION";
    private static final String NO_ACTION = "NO_ACTION";
    private static final String VIEW_ACTION = "VIEW_ACTION";
    private static int NOTIFICATION_ID = (int) System.currentTimeMillis();
    public final String TAG = "SocShpMsg";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        Map<String,String> data = remoteMessage.getData();
        Log.i(TAG+"Pshrcv", data.toString());
        String recipient = data.get("recipient");
        if(recipient.equals("SHOPPER")) {
            String messageContent = data.get("payload");
            String listId = data.get("listid");
            displayNotification(messageContent, listId);
        }
        else if(recipient.equals("REQUESTER")) {
            displayStatusTrackingNotification();
        }
    }

    private void displayStatusTrackingNotification() {
        Intent intent = getStatusTrackingNotificationIntent();
        intent.putExtra("Status", "test");
        intent.setAction(VIEW_ACTION);
    }

    private Intent getStatusTrackingNotificationIntent() {
        return new Intent(this, TrackStatusActivity.class);
    }

    private void displayNotification(String message, String listId){

        //TODO Use LocalBroadcastManager to display notification as a dialog box when the app is in foreground(makes more sense)

        Intent yesIntent = getNotificationIntent();
        yesIntent.putExtra("list_id", listId);
        yesIntent.setAction(YES_ACTION);


        Intent noIntent = getNotificationIntent();
        noIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        noIntent.setAction(NO_ACTION);

        int NOTIFICATION_ID = (int) System.currentTimeMillis();

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("New Pick Up !")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
    //            .setContentIntent(PendingIntent.getActivity(this, 0, getNotificationIntent(), PendingIntent.FLAG_CANCEL_CURRENT))
                .addAction(new NotificationCompat.Action(
                        R.drawable.ic_accept,
                        "Yes",
                        PendingIntent.getActivity(this, 1, yesIntent, PendingIntent.FLAG_CANCEL_CURRENT)))
                .addAction(new NotificationCompat.Action(
                        R.drawable.ic_deny,
                        "No",
                        PendingIntent.getActivity(this, 0, new Intent(this, NotificationActivity.class), 0)));


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }


    private Intent getNotificationIntent() {
        Intent intent = new Intent(this, PickUpListActivity.class);
        return intent;
    }

}
