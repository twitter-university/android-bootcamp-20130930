package com.twitter.yamba;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;


public class NotificationReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {

        int count = intent.getIntExtra("count", -1);
        if (count == -1) return;

        // Notify
        PendingIntent operation = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class).
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(context).setContentTitle("New tweet!").
                setContentText("You have " + count + " new tweets").
                setSmallIcon(android.R.drawable.stat_notify_chat).
                setContentIntent(operation).setAutoCancel(true).getNotification();
        notificationManager.notify(42, notification);

        // Vibrate
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if(vibrator.hasVibrator()) {
            vibrator.vibrate(1000);
        }
    }
}
