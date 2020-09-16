package com.sunny.learn.geofenceutil.geofence_ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Room
import com.sunny.learn.geofenceutil.R
import com.sunny.learn.geofenceutil.room.LocationDatabase
import com.sunny.learn.geofenceutil.room.LocationEntity
import kotlinx.android.synthetic.main.activity_add_geofence.*

class AddGeofenceActivity : AppCompatActivity() {

    private lateinit var locationDatabase: LocationDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_geofence)

        initViews()
    }

    private fun initViews() {
        btnAdd.setOnClickListener { addLocation() }
    }

    private fun addLocation() {
        if (etLocName.text.toString().isEmpty()
            || etLat.text.toString().isEmpty()
            || etLong.text.toString().isEmpty()
        ) {
            Toast.makeText(this, getString(R.string.all_fields_required), Toast.LENGTH_SHORT).show()
            return
        }

        locationDatabase = Room.databaseBuilder(applicationContext, LocationDatabase::class.java, "LocationsDB.db").allowMainThreadQueries().build()
        locationDatabase.getLocationDao().saveLocation(LocationEntity(0, etLocName.text.toString(), etLat.text.toString(), etLong.text.toString()))

        Toast.makeText(this, "Location Added", Toast.LENGTH_SHORT).show()

        val intent = Intent()
        setResult(1, intent)
        finish()
    }
}
