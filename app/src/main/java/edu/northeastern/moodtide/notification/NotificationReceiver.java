package edu.northeastern.moodtide.notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import edu.northeastern.moodtide.R;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "255DAILY")
                .setSmallIcon(R.drawable.home)
                .setContentTitle("How are you feeling")
                .setContentText("Don't forget to log your emotions today")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Show the notification
        notificationManager.notify(0, builder.build());
        Log.e("NOTI","notification sent");
        scheduleNextAlarm(context);
    }
    public static void scheduleNextAlarm(Context context) {
        // Calculate the time for the next alarm (e.g., 24 hours from now)
        long triggerTimeMillis = System.currentTimeMillis() + AlarmManager.INTERVAL_DAY;

        // Set up the alarm intent
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        // Get the AlarmManager service and schedule the next alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
        }
    }
}

