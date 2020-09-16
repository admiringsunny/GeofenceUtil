package com.sunny.learn.geofenceutil.services

import android.R
import android.app.IntentService
import android.app.Notification;
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.sunny.learn.geofenceutil.geofence_ui.MainActivity
import android.net.Uri;
import android.app.TaskStackBuilder;
import android.provider.Settings;


class ReminderAlarmService : IntentService(ReminderAlarmService::class.java.simpleName) {

    private val NOTIFICATION_ID = 42

    //This is a deep link intent, and needs the task stack
    fun getReminderPendingIntent(
        context: Context?,
        uri: Uri?
    ): PendingIntent? {
        val action = Intent(context, ReminderAlarmService::class.java)
        action.data = uri
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun getReminderPendingIntent(context: Context?): PendingIntent? {
        val action = Intent(context, ReminderAlarmService::class.java)
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onHandleIntent(intent: Intent?) {
        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val uri: Uri? = intent?.data
        val descript: String = uri.toString()
        //Display a notification to view the task details
        val action = Intent(this, MainActivity::class.java)
        //action.setData(uri);
        val operation: PendingIntent = TaskStackBuilder.create(this)
            .addNextIntentWithParentStack(action)
            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // for oreo and above
            val geofenceChannel =
                NotificationChannel("geofence", "Geofence", NotificationManager.IMPORTANCE_HIGH)
            manager?.createNotificationChannel(geofenceChannel)
        }
        val note: Notification = NotificationCompat.Builder(this, "geofence")
            .setContentTitle("Geofence")
            .setContentText(descript)
            .setSmallIcon(R.drawable.star_on)
            .setContentIntent(operation)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setAutoCancel(true)
            .build()
        manager?.notify(NOTIFICATION_ID, note)
    }
}
