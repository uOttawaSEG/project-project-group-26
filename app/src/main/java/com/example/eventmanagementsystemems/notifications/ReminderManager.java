package com.example.eventmanagementsystemems.notifications;

import android.Manifest;
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
            }
        }
    }

    /**
     * Schedules a reminder notification 24 hours before the event start time.
     */
    public static void scheduleReminder(Context context, Event event) {
        try {
            // Parse the event date and time
            String dateTime = event.getDate() + " " + event.getStartTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date eventDate = format.parse(dateTime);

            if (eventDate != null) {
                // Calculate the reminder time (24 hours before event)
                long reminderTime = eventDate.getTime() - (20 * 60 * 60 * 1000);
                Log.d("ReminderManager", "Parsed eventDate: " + eventDate.toString());
                Log.d("ReminderManager", "Scheduling reminder for: " + new Date(reminderTime).toString());


                Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
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
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
                    Log.d("ReminderManager", "Reminder scheduled for: " + new Date(reminderTime).toString());
                } else {
                    Log.e("ReminderManager", "AlarmManager is null. Reminder not scheduled.");
                }
            }
        } catch (Exception e) {
            Log.e("ReminderManager", "Error scheduling reminder: " + e.getMessage(), e);
        }
    }

    /**
     * Sends a notification to the user.
     */
    public static void sendNotification(Context context, String eventTitle) {
        // Check if POST_NOTIFICATIONS permission is granted (required for Android 13+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e("ReminderManager", "POST_NOTIFICATIONS permission not granted.");
                return;
            }
        }

        // Build and display the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Event Reminder")
                .setContentText("Your event \"" + eventTitle + "\" starts in 24 hours.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(eventTitle.hashCode(), builder.build());
        Log.d("ReminderManager", "Notification sent for event: " + eventTitle);
    }
}
