package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*

const val LOCATION_REQUEST_CODE = 0

class MainActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED-> {
                getLocation()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                showLocationRequestUI()
            }
            else -> {
                requestLocationPermission()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    showLocationDeniedUI()
                }
            }
            else -> {}
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

    private fun getLocation() {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                val viewModel: WeatherViewModel by viewModels()
                viewModel.getForecast(location.latitude, location.longitude)
                    .observe(this, Observer { forecast ->
                        temperatureText.text = "${forecast.temperature}° F"
                    })
            }
            .addOnFailureListener {
                Toast.makeText(this, "Unable to retrieve users location", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showLocationRequestUI() {
        AlertDialog.Builder(this)
            .setTitle("Please enable location")
            .setMessage("We need location data to get the weather in your area")
            .setOnCancelListener {
                showLocationDeniedUI()
            }
            .setNegativeButton("Cancel") { _, _ ->
                showLocationDeniedUI()
            }
            .setPositiveButton("Ok") { _, _ ->
                requestLocationPermission()
            }
            .create()
            .show()
    }

    private fun showLocationDeniedUI() {

    }
}
