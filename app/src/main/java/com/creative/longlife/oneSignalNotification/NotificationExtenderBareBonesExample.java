package com.creative.longlife.oneSignalNotification;

/**
 * Created by jubayer on 1/22/2018.
 */

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Notification;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.List;

public class NotificationExtenderBareBonesExample extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        // Read properties from result.

        //Log.d("DEBUG",receivedResult.g)
        // Return true to stop the notification from displaying.
        JSONObject data = receivedResult.payload.additionalData;
        String title = receivedResult.payload.title;
        String body = receivedResult.payload.body;
        String img_url = "";
        Log.d("DEBUG_process",title);
        Log.d("DEBUG_process",body);
        if (data != null) {
            try {
                img_url = data.getString("image");
            } catch (JSONException e) {
                img_url = "";
                e.printStackTrace();
            }
        }

        Notification notification = new Notification(title,body,img_url);

        List<Notification> notifications = MydApplication.getInstance().getPrefManger().getNotifications();
        notifications.add(notification);
        MydApplication.getInstance().getPrefManger().setNotifications(notifications);

        int newNotificationCounter = MydApplication.getInstance().getPrefManger().getNewNotificationCounter();
        newNotificationCounter = newNotificationCounter + 1;
        MydApplication.getInstance().getPrefManger().setNewNotificationCounter(newNotificationCounter);

        OverrideSettings overrideSettings = new OverrideSettings();
        /*overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Green on Android 5.0+ devices.
                return builder.setColor(new BigInteger("FF00FF00", 16).intValue());
            }
        };*/

        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        return true;
    }
}