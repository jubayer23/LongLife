package com.creative.longlife.service;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.creative.longlife.HomeActivity;
import com.creative.longlife.LoginActivity;
import com.creative.longlife.NotificationActivity;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Notification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

/**
 * Created by jubayer on 1/23/2018.
 */

public class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    private Application application;

    public ExampleNotificationOpenedHandler(Application application) {
        this.application = application;
    }
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;

        String title = result.notification.payload.title;
        String body = result.notification.payload.body;
        Log.d("DEBUG_open",title);
        Log.d("DEBUG_open",body);
        String customKey;

        if (data != null) {
           Log.d("DEBUG_2",data.toString());
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

        /**
         * If User is already logged-in then redirect user to the Notification Page
         * */
        if (MydApplication.getInstance().getPrefManger().getUserProfile() != null) {
            //clearNotifications(application);
            OneSignal.clearOneSignalNotifications();
            Intent intent = new Intent(application, NotificationActivity.class);
            intent.putExtra(GlobalAppAccess.KEY_CALL_FROM,GlobalAppAccess.TAG_NOTIFICATION);
            application.startActivity(intent);
        }else{
            Intent intent = new Intent(application, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(GlobalAppAccess.KEY_CALL_FROM,GlobalAppAccess.TAG_NOTIFICATION);
            application.startActivity(intent);
        }

        // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.
        // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);

        // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
        //   if you are calling startActivity above.
     /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
     */
    }
    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}