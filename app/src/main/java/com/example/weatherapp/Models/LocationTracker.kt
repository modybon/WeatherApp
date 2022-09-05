package com.example.weatherapp.Models

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.DefaultLifecycleObserver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*
import java.util.jar.Manifest

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

class LocationGetter(
    private val locationClient : FusedLocationProviderClient,
    private val activity : Activity
) {
    private val requestCode = 100;
    private val TAG = "LocationTracker"
    fun askPermission(){
        ActivityCompat.requestPermissions(activity, arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION),
            requestCode
        )
    }
    fun checkPermission() : Boolean{
        return !(ActivityCompat.checkSelfPermission(activity,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }
    fun getLocation(){
        if(checkPermission()){
            locationClient.lastLocation.addOnSuccessListener(activity) { location ->
                if (location != null) {
                    Log.e(TAG, "getLocation Location Class: $location")
                    Log.e(TAG, "getLocation Location City: : ${getCity(location)}", )
                }
            }
        }
    }
    fun getCity(location: Location) : String?{
        var city : String? = null
        try{
            val geocoder: Geocoder = Geocoder(activity, Locale.getDefault())
            val address : List<Address>
            address = geocoder.getFromLocation(location.latitude,location.longitude,1) // 1 represents the max location returned
            Log.e(TAG, "getAddress: $address", )
            val l = address[0]
            city = l.locality
            Log.e(TAG, "GetCity: ${city}")
        }catch (e: Exception){
            Log.e(TAG, "getCity Error: ${e.message}", )
        }
        return city
    }
}