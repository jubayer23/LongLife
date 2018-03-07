package com.creative.longlife.receiver;

/**
 * Created by jubayer on 1/23/2018.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.creative.longlife.R;
import com.creative.longlife.ReminderActivity;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Reminder;

import java.util.Collections;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    private Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();

        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("body");
        String time = intent.getStringExtra("time");


        // notification icon
        final int icon = R.mipmap.ic_launcher_5;

        Intent intent1 = new Intent(context, ReminderActivity.class);
        intent1.putExtra(GlobalAppAccess.KEY_CALL_FROM,GlobalAppAccess.TAG_ALARM_RECEIVER);
        intent1.putExtra("time",time);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent1,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext);

        //final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
        //        + "://" + mContext.getPackageName() + "/raw/notification");

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound);
        playNotificationSound();
    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                //.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setStyle(inboxStyle)
                //.setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher_5)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(GlobalAppAccess.NOTIFICATION_ID, notification);
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            // Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
            //        + "://" + mContext.getPackageName() + "/raw/notification");
            // Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            // r.play();

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(mContext, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}