package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel: ViewModel() {
    fun getForecast(latitude: Double, longitude: Double): LiveData<WeatherModel> {
        val result = MutableLiveData<WeatherModel>()

        viewModelScope.launch {
            result.value = withContext(Dispatchers.IO) {
                val forecast = WeatherApi.service.getForecast(latitude, longitude).await()
                WeatherModel(forecast.current.temp)
            }
        }

        return result
    }
}