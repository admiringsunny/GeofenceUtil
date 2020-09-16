package com.sunny.learn.geofenceutil.services

import android.app.AlarmManager
import android.content.Context


class AlarmManagerProvider {
    private val TAG = AlarmManagerProvider::class.java.simpleName
    private var sAlarmManager: AlarmManager? = null

    @Synchronized
    fun injectAlarmManager(alarmManager: AlarmManager?) {
        check(sAlarmManager == null) { "Alarm Manager Already Set" }
        sAlarmManager = alarmManager
    }

    /*package*/
    @Synchronized
    fun getAlarmManager(context: Context): AlarmManager? {
        if (sAlarmManager == null) {
            sAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        }
        return sAlarmManager
    }
}