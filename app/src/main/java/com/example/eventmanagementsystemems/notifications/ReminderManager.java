package com.example.eventmanagementsystemems.notifications;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.eventmanagementsystemems.R;
import com.example.eventmanagementsystemems.entities.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReminderManager {

    private static final String CHANNEL_ID = "event_reminders";

    /**
     * Creates a notification channel required for Android 8.0 (API 26) and above.
     */
    public static void createNotificationChannel(Context context) {
        Log.d("ReminderManager", "createNotificationChannel() called.");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Event Reminders";
            String description = "Notifications for event reminders 24 hours before the event.";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                Log.d("ReminderManager", "Notification channel created.");
            } else {
                Log.e("ReminderManager", "NotificationManager is null. Channel not created.");
            }
        } else {
            Log.d("ReminderManager", "Android version < 8.0. No need to create notification channel.");
        }
    }

    /**
     * Schedules a reminder notification 24 hours before the event start time.
     */
    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleReminder(Context context, Event event) {
        Log.d("ReminderManager", "scheduleReminder() called for event: " + event.getTitle());

        try {
            // Parse the event date and time
            String dateTime = event.getDate() + " " + event.getStartTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date eventDate = format.parse(dateTime);

            Log.d("ReminderManager", "Current time: " + new Date(System.currentTimeMillis()));
            Log.d("ReminderManager", "Parsed eventDate: " + eventDate);

            if (eventDate != null) {
                // Calculate the reminder time (24 hours before event)
                // For testing purposes, set the reminder to trigger in 1 minute
                long reminderTime = System.currentTimeMillis() + (10 * 1000); // 1 minute from now
                // For actual implementation, uncomment the line below and comment out the testing line
                // long reminderTime = eventDate.getTime() - (24 * 60 * 60 * 1000);

                Log.d("ReminderManager", "Scheduling reminder for: " + new Date(reminderTime));

                // Ensure reminderTime is in the future
                if (reminderTime > System.currentTimeMillis()) {
                    Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
                    intent.setAction("com.example.eventmanagementsystemems.REMINDER_ACTION");
                    intent.putExtra("eventTitle", event.getTitle());
                    intent.putExtra("eventId", event.getEventId());

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            context,
                            event.getEventId().hashCode(), // Unique ID for the event
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    if (alarmManager != null) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
                            Log.d("ReminderManager", "Alarm scheduled using setExactAndAllowWhileIdle.");
                        } else {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
                            Log.d("ReminderManager", "Alarm scheduled using setExact.");
                        }
                        Log.d("ReminderManager", "Reminder scheduled for: " + new Date(reminderTime));
                    } else {
                        Log.e("ReminderManager", "AlarmManager is null. Reminder not scheduled.");
                    }
                } else {
                    Log.w("ReminderManager", "Reminder time is in the past. Reminder not scheduled.");
                }
            } else {
                Log.e("ReminderManager", "eventDate is null. Unable to schedule reminder.");
            }
        } catch (Exception e) {
            Log.e("ReminderManager", "Error scheduling reminder: " + e.getMessage(), e);
        }
    }

    /**
     * Cancels a scheduled reminder for the given event.
     */
    public static void cancelReminder(Context context, Event event) {
        Log.d("ReminderManager", "cancelReminder() called for event: " + event.getTitle());

        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        intent.setAction("com.example.eventmanagementsystemems.REMINDER_ACTION");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                event.getEventId().hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.d("ReminderManager", "Reminder canceled for event: " + event.getTitle());
        } else {
            Log.e("ReminderManager", "AlarmManager is null. Unable to cancel reminder.");
        }
    }

    /**
     * Sends a notification to the user.
     */
    public static void sendNotification(Context context, String eventTitle) {
        Log.d("ReminderManager", "sendNotification() called for event: " + eventTitle);

        // Check if POST_NOTIFICATIONS permission is granted (required for Android 13+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e("ReminderManager", "POST_NOTIFICATIONS permission not granted.");
                return;
            } else {
                Log.d("ReminderManager", "POST_NOTIFICATIONS permission granted.");
            }
        }

        // Build and display the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_event_reminder) // Ensure this icon exists
                .setContentTitle("Event Reminder")
                .setContentText("Your event \"" + eventTitle + "\" starts in 24 hours.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(eventTitle.hashCode(), builder.build());
        Log.d("ReminderManager", "Notification sent for event: " + eventTitle);
    }
}