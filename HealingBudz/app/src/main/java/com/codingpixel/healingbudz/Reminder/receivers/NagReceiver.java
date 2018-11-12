package com.codingpixel.healingbudz.Reminder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.codingpixel.healingbudz.Reminder.database.DatabaseHelper;
import com.codingpixel.healingbudz.Reminder.models.Reminder;
import com.codingpixel.healingbudz.Reminder.utils.NotificationUtil;


public class NagReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper database;
        database = DatabaseHelper.getInstance(context);
        int reminderId = intent.getIntExtra("NOTIFICATION_ID", 0);
        if (reminderId != 0 && database.isNotificationPresent(reminderId)) {
            Reminder reminder = database.getNotification(reminderId);
            NotificationUtil.createNotification(context, reminder);
        }
        database.close();
    }
}