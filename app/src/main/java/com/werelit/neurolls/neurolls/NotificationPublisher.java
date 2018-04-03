package com.werelit.neurolls.neurolls;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.UUID;

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {
        //calls the system notification service
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION); //gets the notification sent
        //int id = intent.getIntExtra(NOTIFICATION_ID, 0); //gets the notification id
        notificationManager.notify(UUID.randomUUID().hashCode(), notification);

    }
}