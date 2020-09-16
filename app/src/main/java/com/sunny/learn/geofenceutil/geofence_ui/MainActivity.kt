package com.sunny.learn.geofenceutil.geofence_ui

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.sunny.learn.geofenceutil.MapsActivity
import com.sunny.learn.geofenceutil.R
import com.sunny.learn.geofenceutil.room.LocationDatabase
import com.sunny.learn.geofenceutil.room.LocationEntity
import com.sunny.learn.geofenceutil.services.GeofenceTransitionsIntentService
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnCompleteListener<Void> {

    private lateinit var locList: List<LocationEntity>
    private lateinit var mGeofencingClient: GeofencingClient
    private lateinit var mGeofenceList: ArrayList<Geofence?>
    private lateinit var locationDatabase: LocationDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }


    private fun initViews() {
//        startActivity(Intent(this, MapsActivity::class.java));

        locationDatabase =
            Room.databaseBuilder(applicationContext, LocationDatabase::class.java, "LocationsDB.db")
                .allowMainThreadQueries().build()
        val locations = locationDatabase.getLocationDao().getLocations()
        if (locations.isNullOrEmpty()){
            tvNoSavedLoc.visibility = View.VISIBLE
            rvCords.visibility = View.GONE
        } else {
            locList = locations
            rvCords.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = LocationAdapter(this@MainActivity, locList)
            }

            initGeofence()
            addGeofences()
        }

        fab.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddGeofenceActivity::class.java)
            startActivityForResult(intent, 101)
        })


    }

    private fun initGeofence() {

        // Empty list for storing geofences.
        mGeofenceList = ArrayList<Geofence?>()

        populateGeofenceList()

        // Provides access to the Geofencing API.
        mGeofencingClient = LocationServices.getGeofencingClient(this)
    }


    private fun populateGeofenceList() {
        if (locList.isNullOrEmpty())
            return
        for (loc in locList) {
            mGeofenceList.add(
                loc.latitude?.let {latitude ->
                    loc.longitude?.let { longitude ->
                        Geofence.Builder()
                            .setRequestId(loc.name)
                            .setCircularRegion(
                                latitude.toDouble(),
                                longitude.toDouble(),
                                500f
                            )
                            .setExpirationDuration(24 * 60 * 60 * 1000)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                            .build()
                    }
                }
            )
        }
    }


    private fun addGeofences() {
        if (mGeofenceList.isEmpty())
            return
        mGeofencingClient
            .addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
            .addOnCompleteListener(this)
    }

    private fun getGeofencingRequest(): GeofencingRequest? {
        val builder = GeofencingRequest.Builder()
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        builder.addGeofences(mGeofenceList)
        return builder.build()
    }

    private fun getGeofencePendingIntent(): PendingIntent? {
        val intent = Intent(this, GeofenceTransitionsIntentService::class.java)
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            101 -> {
                if (resultCode == 1) {
                    initViews()
                }
            }
        }
    }

    override fun onComplete(task: Task<Void>) {
        if (task.isSuccessful) {
            Toast.makeText(this, "Geofences added", Toast.LENGTH_SHORT).show();
        }
    }
}
