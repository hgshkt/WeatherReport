package com.hgshkt.data.repository

import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat.getSystemService
import com.hgshkt.data.api.RetrofitInstance


class Repository(var lat: Double, var lon: Double) {

    fun getWeather() = RetrofitInstance.api.getWeather(lat.toString(), lon.toString())
}