package com.example.weatherapp

import WeatherApp
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        val retrofit= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response= retrofit.getWeatherData(city = "Burla", appid = "fa434a6aafd718d12c00578c0b25f3d4", units = "metric")
        response.enqueue(object : Callback<WeatherApp>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.main.temp.toString()
                    val humidity= responseBody.main.humidity
                    val windspeed= responseBody.wind.speed
                    val sunRise= responseBody.sys.sunrise
                    val sunSet= responseBody.sys.sunset
                    val seaLevel= responseBody.main.pressure
                    val condition= responseBody.weather.firstOrNull()?.main?: "unknown"
                    val maxTemp= responseBody.main.temp_max
                    val minTemp= responseBody.main.temp_min

                    binding.temp.text= "$temperature C"
                    binding.weather.text= condition
                    binding.maxTemp.text= "Max: $maxTemp C"
                    binding.minTemp.text= "Min: $minTemp C"
                    binding.Humidity.text= "$humidity %"
                    binding.windspeed.text= "$windspeed m/s"
                    binding.sunrise.text= "$sunRise"
                    binding.sunset.text= "$sunSet"
                    binding.sea.text= "$seaLevel hpa"
                    binding.Condition.text= condition

                }
            }
            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {

            }

        })
    }
}