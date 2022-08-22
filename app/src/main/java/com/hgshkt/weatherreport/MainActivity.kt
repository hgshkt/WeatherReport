package com.hgshkt.weatherreport

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.hgshkt.data.repository.Repository
import com.hgshkt.model.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var textView: TextView
    lateinit var textView2: TextView

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var repository : Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        var lon: Double = 0.0
        var lat: Double = 0.0

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                var location = it.result

                if (location != null) {
                    var geocoder = Geocoder(this, Locale.getDefault())
                    var addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                    lat = addressList[0].latitude
                    lon = addressList[0].longitude

                    textView.text = "lat = $lat\n lon = $lon" +
                            "\ncountry name: ${addressList[0].countryName}\nlocality: ${addressList[0].locality}" +
                            "\naddress: ${addressList[0].getAddressLine(0)}"

                    repository = Repository(lat, lon)
                    var call = repository.getWeather()
                    call.enqueue(object: Callback<Weather> {
                        override fun onResponse(call1: Call<Weather>, response: Response<Weather>) {
                            var weather = response.body()!!
                            textView2.text = "weather: ${weather.weather}\nwind: ${weather.wind}\n" +
                                    "clouds: ${weather.clouds}\ncoord: ${weather.coord}\nname: ${weather.name}"
                        }

                        override fun onFailure(call: Call<Weather>, t: Throwable) {
                        }

                    })
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 44)
        }
    }
}