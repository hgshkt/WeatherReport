package com.hgshkt.data.api

import com.hgshkt.model.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/data/2.5/weather?appid=c483a300f7be2185b4e83feca219ecae")
    fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String): Call<Weather>
}