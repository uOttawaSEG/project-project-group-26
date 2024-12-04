package com.example.eventmanagementsystemems.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String eventTitle = intent.getStringExtra("eventTitle");
        Log.d("ReminderReceiver", "Reminder triggered for event: " + eventTitle);

        // Send the notification
        com.example.eventmanagementsystemems.notifications.ReminderManager.sendNotification(context, eventTitle);
    }
}
