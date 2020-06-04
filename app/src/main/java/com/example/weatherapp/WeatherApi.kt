package com.example.weatherapp

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/onecall?appid=${WEATHER_API_KEY}&exclude=hourly,minutely")
    fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "imperial"
    ): Deferred<ForecastData>

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        val service: WeatherApi = retrofit.create(WeatherApi::class.java)
    }
}

data class ForecastData(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val current: ForecastDetails
)

data class ForecastDetails(
    val dt: Long,
    val temp: Double,
    val feels_like: Double,
    val weather: List<WeatherData>
)

data class WeatherData(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
