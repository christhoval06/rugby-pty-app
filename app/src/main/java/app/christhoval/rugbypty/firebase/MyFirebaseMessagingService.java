package app.christhoval.rugbypty.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import app.christhoval.rugbypty.RugbyPTY;
import app.christhoval.rugbypty.activities.Main;
import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.models.Notification;
import app.christhoval.rugbypty.mvp.notifications.NotificationsFragment;
import app.christhoval.rugbypty.utilities.Debug;
import app.christhoval.rugbypty.utilities.db.RUGBYPTYDB;


/**
 * Created by christhoval on 06/27/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Debug.i("onMessageReceived, foreground notification");
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        Debug.i("From ---> " + remoteMessage.getFrom());
        Debug.i("MessageId ---> " + remoteMessage.getMessageId());
        Debug.i("To ---> " + remoteMessage.getTo());
        Debug.i("SentTime ---> " + remoteMessage.getSentTime());
        Debug.i("Ttl ---> " + remoteMessage.getTtl());
        Debug.i("CollapseKey ---> " + remoteMessage.getCollapseKey());
        Debug.i("MessageType ---> " + remoteMessage.getMessageType());
        Debug.i("Data ---> " + remoteMessage.getData());
        Debug.i("Notification : Body ---> " + remoteMessage.getNotification().getBody());

        switch (remoteMessage.getFrom()) {
            case "/topics/notifications":
                displayNotification(remoteMessage.getNotification(), remoteMessage.getData());
                sendNewPromoBroadcast(remoteMessage);
                break;
        }
    }
    // [END receive_message]


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessage FCM message body received.
     */

    private void sendNotification(RemoteMessage remoteMessage) {
        Debug.i("sendNotification, background notification");
        Intent intent = new Intent(this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }


    private void sendNewPromoBroadcast(RemoteMessage remoteMessage) {

        Intent intent = new Intent(NotificationsFragment.ACTION_NOTIFY_NEW_MATCH);
        intent.putExtra(RUGBYPTYDB.NOTIFICATION_TITLE_COLUMN, remoteMessage.getNotification().getTitle());
        intent.putExtra(RUGBYPTYDB.NOTIFICATION_DESCRIPTION_COLUMN, remoteMessage.getNotification().getBody());
        for (String key : remoteMessage.getData().keySet()) {
            intent.putExtra(key, remoteMessage.getData().get(key));
        }
        RugbyPTY rugbyPTY = (RugbyPTY) getApplication();
        RUGBYPTYDB db = rugbyPTY.getDb();
        db.setNotification(Notification.fromIntent(intent));

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

    }


    private void displayNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Intent intent = new Intent(this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}

/*
Problem solved:

If we are using FCM and we want the message sent from SOAPUI, SAP, PI, etc and that the message is received by the foreground or background being APP and the style is always the same is to be used only "data". Passeth by the method onMessageReceived.

{ "data": { "Title": "Firebase notification" "Detail": "I am firebase notification."   },   "To": "efaOvIXDbik: APA91bEkNUVWNaoA ...."   }

If we use "notification" messages they are going to be different if the app is in foreground or background

{ "To": "bk3RNwTe3H0: CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1 ..."

"Notification": {    "Body": "great match!"    "Title": "Portugal vs. Denmark"    "Icon": "MyIcon"    "Sound": "mySound" } }
 */