package com.sunny.learn.geofenceutil.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.os.Build


class AlarmScheduler {



    fun setRepeatAlarm(
        context: Context?,
        alarmTime: Long,
        geoFenceTitle: String?,
        RepeatTime: Long
    ) {
        val manager: AlarmManager ?= context?.let { AlarmManagerProvider().getAlarmManager(it) }
        val operation: PendingIntent? = ReminderAlarmService().getReminderPendingIntent(
            context,
            Uri.parse(geoFenceTitle)
        )
        manager?.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation)
    }

    fun cancelAlarm(context: Context?) {
        val manager: AlarmManager? = context?.let { AlarmManagerProvider().getAlarmManager(it) }
        val operation: PendingIntent? = ReminderAlarmService().getReminderPendingIntent(context)
        manager?.cancel(operation)
    }
}