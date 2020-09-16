package com.sunny.learn.geofenceutil.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {

    @Insert
    fun saveLocation(locationEntity: LocationEntity)

    @Query("select * from locations")
    fun getLocations(): List<LocationEntity>

}