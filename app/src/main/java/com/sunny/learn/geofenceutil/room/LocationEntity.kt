package com.sunny.learn.geofenceutil.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String?,
    val latitude: String?,
    val longitude: String?
)