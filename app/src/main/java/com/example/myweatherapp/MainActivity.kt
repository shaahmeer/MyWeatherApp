package com.example.myweatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.model.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)


        fetchWeatherData("karachi" )
        SearchCity()
    }



    private fun SearchCity() {
        val searchView = _binding.searchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun fetchWeatherData(cityName :String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(apiInterface::class.java)
        val response = retrofit.getWeatherData(cityName, "2972e86e9cc5a06c88f93f712101f1bf", "metric")
        response.enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                val responseBody = response.body()
                if (responseBody != null) {
                    println("wargya")
                    val tempreture = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toLong()
                    val sunSet = responseBody.sys.sunset.toLong()
                    val seaLevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main?:"unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min
                    _binding.temperature.text = "$tempreture C"
                    _binding.humidity.text = "$humidity"
                    _binding.windspeed.text = "$windSpeed"
                    _binding.sunrise.text = "${time(sunRise)}"
                    _binding.sunset.text = "${time(sunSet)}"
                    _binding.sea.text = "$seaLevel"
                    _binding.condition.text = "$condition"
                    _binding.maxTemp.text = "$maxTemp"
                    _binding.minTemp.text = "$minTemp"
                    _binding.weather.text = "$condition"
                    _binding.dayOfWeek.text = dayName(System.currentTimeMillis())
                        _binding.dateAndTime.text =date()
                    _binding.cityname.text ="$cityName"

                    changeWaetheraccordingToCondition(condition)



                }



            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                println("$t nahe wargya")
            }
        })


    }

    private fun changeWaetheraccordingToCondition(Conditions :String) {
        when (Conditions) {
            "Haze" -> {
                _binding.root.setBackgroundResource(R.drawable.colud_background)
                _binding.lottieLayerName.setAnimation(R.raw.cloud)
            }
            "Sunny" -> {
                _binding.root.setBackgroundResource(R.drawable.sunny_background)
                _binding.lottieLayerName.setAnimation(R.raw.sun)
            }
            "Rain" -> {
                _binding.root.setBackgroundResource(R.drawable.rain_background)
                _binding.lottieLayerName.setAnimation(R.raw.rain)
            }
            "Snow" -> {
                _binding.root.setBackgroundResource(R.drawable.snow_background)
                _binding.lottieLayerName.setAnimation(R.raw.snow)
            }
        }
    }

    private fun date(): String? {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))


    }
    private fun time(timestamp: Long): String? {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))


    }

    fun dayName(timestamp: Long):String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}
















