package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeatherViewModel: ViewModel() {
    fun getForecast(latitude: Double, longitude: Double): LiveData<WeatherModel> {
        val result = MutableLiveData<WeatherModel>()
        result.value = WeatherModel(latitude)
        return result
    }
}