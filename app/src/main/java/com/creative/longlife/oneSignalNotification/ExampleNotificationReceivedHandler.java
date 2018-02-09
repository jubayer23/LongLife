package com.creative.longlife.oneSignalNotification;

import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

/**
 * Created by jubayer on 1/23/2018.
 */

public class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
    @Override
    public void notificationReceived(OSNotification notification) {

        String title = notification.payload.title;
        String body = notification.payload.body;
        Log.d("DEBUG_rcv",title);
        Log.d("DEBUG_rcv",body);


    }
}