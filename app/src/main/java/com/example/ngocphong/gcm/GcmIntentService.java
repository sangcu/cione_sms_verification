package com.example.ngocphong.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by NgocPhong on 4/13/2015.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    public static final String TAG = "GcmIntentService";
    private static final int MAX_SMS_MESSAGE_LENGTH = 160;
    private static final Object SMS_PORT = null;
    private static final Context SMS_SENT = null;
    private static final Context SMS_DELIVERED = null;
    NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;
    public   static String content = "Welcome Cione Your code is : ";
    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.

                // sendNotification("Received: " + extras.toString());
                // sent message to user

                UserToken usertoken = jsontoObject(extras.toString());
                sendSms(usertoken.phone, content + usertoken.code, false);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
    // parse json string to Usertoken
    private UserToken jsontoObject(String jsonstring) {
        UserToken usertoken;
        Gson gson = new Gson();
        usertoken = gson.fromJson(jsonstring, UserToken.class);
        return usertoken;
    }

    // send sms
    private void sendSms(String phonenumber, String message, boolean isBinary) {
        SmsManager manager = SmsManager.getDefault();

        //  PendingIntent piSend = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
        //  PendingIntent piDelivered = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);

        if (isBinary) {
            byte[] data = new byte[message.length()];

            for (int index = 0; index < message.length() && index < MAX_SMS_MESSAGE_LENGTH; ++index) {
                data[index] = (byte) message.charAt(index);
            }

            //  manager.sendDataMessage(phonenumber, null, (short) SMS_PORT, data,piSend, piDelivered);
        } else {
            int length = message.length();

            if (length > MAX_SMS_MESSAGE_LENGTH) {
                ArrayList<String> messagelist = manager.divideMessage(message);

                manager.sendMultipartTextMessage(phonenumber, null, messagelist, null, null);
            } else {
                //  manager.se
                manager.sendTextMessage(phonenumber, null, message, null, null);
            }
        }
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_gcm)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
