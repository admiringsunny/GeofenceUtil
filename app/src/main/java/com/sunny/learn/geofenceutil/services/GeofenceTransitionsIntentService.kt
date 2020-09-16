package com.sunny.learn.geofenceutil.services

import android.app.IntentService
import android.content.Intent
import android.text.TextUtils
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import java.util.*
import kotlin.collections.ArrayList


class GeofenceTransitionsIntentService :
    IntentService(GeofenceTransitionsIntentService::class.java.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            var geofencingEvent: GeofencingEvent = GeofencingEvent.fromIntent(intent)
            if (geofencingEvent.hasError()) {
                return
            }

            val geofenceTransition: Int = geofencingEvent.geofenceTransition
            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                val triggeringGeofences = geofencingEvent.triggeringGeofences
                val geofenceTransitionDetails = getGeofenceTransitionDetails(triggeringGeofences)
                val currentTime = Calendar.getInstance()
                val mRepeatTime: Long = 3600000L;
                AlarmScheduler().setRepeatAlarm(
                    applicationContext,
                    currentTime.timeInMillis,
                    geofenceTransitionDetails,
                    mRepeatTime
                )

            }

        }
    }

    private fun getGeofenceTransitionDetails(triggeringGeofences: List<Geofence>): String {
        val triggeringGeofencesIdsList: ArrayList<String> = ArrayList()

        for (geofence in triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.requestId)
        }
        val triggeringGeofencesIdsString =
            TextUtils.join(", ", triggeringGeofencesIdsList)


        return "Entered: $triggeringGeofencesIdsString";
    }
}
