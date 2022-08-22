package com.hgshkt.weatherreport

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hgshkt.data.repository.Repository
import com.hgshkt.model.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

//    lateinit var recyclerView: RecyclerView
    lateinit var cityTV: TextView
    lateinit var temperatureTV: TextView
    lateinit var descriptionTV: TextView
    lateinit var currentWeatherButton: Button

    private var lat = 0.0
    private var lon = 0.0

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var repository : Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        currentWeatherButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    var location = it.result

                    if (location != null) {
                        var geocoder = Geocoder(this, Locale.getDefault())
                        var addressList =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        lat = addressList[0].latitude
                        lon = addressList[0].longitude
                        repository = Repository(lat, lon)

                        var call = repository.getWeather()

                        call.enqueue(object : Callback<Weather> {
                            override fun onResponse(
                                call1: Call<Weather>,
                                response: Response<Weather>
                            ) {
                                var weather = response.body()!!
                                cityTV.text = "city: ${weather.name}"
                                temperatureTV.text = String.format("temperature: %(.1f°C", (weather.main.temp-273.15))
                                descriptionTV.text = "description: ${weather.weather[0].description}"
                            }
                            override fun onFailure(call: Call<Weather>, t: Throwable) {
                            }
                        })
                    }
                }
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    44
                )
            }
        }
    }

    private fun init() {
        cityTV = findViewById(R.id.city)
        temperatureTV = findViewById(R.id.temperature)
        descriptionTV = findViewById(R.id.description)
        currentWeatherButton = findViewById(R.id.current_weather_button)
    }
}