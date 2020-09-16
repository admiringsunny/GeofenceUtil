package com.sunny.learn.geofenceutil.geofence_ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunny.learn.geofenceutil.R
import com.sunny.learn.geofenceutil.room.LocationEntity

class LocationAdapter(val context: Context, val locEntityList: List<LocationEntity>?)
    : RecyclerView.Adapter<LocationAdapter.MyHolder>(){

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvLocName : TextView = itemView.findViewById(R.id.tvLocName)
        val tvLocCord : TextView = itemView.findViewById(R.id.tvLocCord)

        fun bind(locName: String?, locCord : String?){
            tvLocName.text = locName
            tvLocCord.text = locCord
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_location, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return locEntityList?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val locName: String? = locEntityList?.get(position)?.name
        val locCord: String? = "${locEntityList?.get(position)?.latitude}, ${locEntityList?.get(position)?.longitude}"
        holder.bind(locName, locCord)
    }
}