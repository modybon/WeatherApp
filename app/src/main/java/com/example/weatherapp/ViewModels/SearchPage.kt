package com.example.weatherapp.ViewModels

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.Models.*
import com.example.weatherapp.databinding.ActivitySearchPageBinding
import com.example.weatherapp.managers.SharedPrefrencesManager
import java.text.FieldPosition
import java.util.*

class SearchPage : AppCompatActivity(), SearchView.OnQueryTextListener, WeatherInfoDialogInterface, LocationListener{
    private val TAG = this@SearchPage.toString()
    lateinit var binding : ActivitySearchPageBinding
    lateinit var viewModel: ViewModel
    lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivitySearchPageBinding.inflate(layoutInflater)
        this.viewModel = ViewModel(this.application)
        setContentView(binding.root)

        this.binding.recyclerView.layoutManager = LinearLayoutManager(this)
        this.binding.searchBar.setOnQueryTextListener(this)
        this.viewModel.setUp(applicationContext)
        this.viewModel.citiesWheatherList.observe(this){
            //Log.e(TAG, "TEST RUN onCreate: List Changed Added: ${it}")
            recyclerViewAdapter.notifyDataSetChanged()
            Log.e(TAG, "SIZE onCreate: ${this.viewModel.citiesWheatherList.value?.size}")
        }
        recyclerViewAdapter = RecyclerViewAdapter(this,this.viewModel,this)
        this.binding.recyclerView.adapter = recyclerViewAdapter
        this.binding.recyclerView.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        this.binding.recyclerView.addItemDecoration(MarginItemDecoration(50))
        getLocation()
    }

    override fun onQueryTextChange(newText: String): Boolean {
        //Log.e(TAG, "onQueryTextChange: $newText", )
        return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        // task HERE
        Log.e(TAG, "onQueryTextSubmit: $query")
        viewModel.getWeather(query).invokeOnCompletion {
            val myDialog = PopUpWeatherInfoDialog(this.viewModel.cityWheatherInfo,this,this.viewModel)
            myDialog.show(this.supportFragmentManager,"MyDialog")
        }
        return false
    }

    override fun onClickListener(cityWheatherInfo: CityWheatherInfo,position: Int) {
        Log.e(TAG, "onClickListener: City Clicked")
        Log.e(TAG, "onClickListener: $cityWheatherInfo +\nPosition: $position")
        var bundle = Bundle()
        var l = this.viewModel.citiesWheatherList.value
        //Log.e(TAG, "onClickListener Value: $l", )
        bundle.putParcelableArrayList("List",this.viewModel.citiesWheatherList.value)
        val intent = Intent(this, MainActivity::class.java)
        //intent.putExtra("CitiesList",bundle)
        intent.putParcelableArrayListExtra("CitiesList",this.viewModel.citiesWheatherList.value)
        intent.putExtra("ItemSelectedPosition",position)
        // TODO: HAVE THE GETPOSITON BE SEPERATE FROM THE ONCLICKLISTENER
        startActivity(intent)
    }

    fun getLocation(){
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager// provides access to the system location services and these services
        // allow applications to obtain periodic updates of the device's geographical location
        var cirtiria = Criteria() // indicating the application criteria for selecting a location provider.
        // Providers may be ordered according to accuracy, power usage, ability to report altitude, speed, bearing, and monetary cost.
        var provider = locationManager.getBestProvider(cirtiria,false)
        Log.e(TAG, "getLocation Provider: ${provider.toString()}")
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // When the Permission is not Granted
            Log.e(TAG, "getLocation: Permission not granted yet ", )
            this.requestPermission()
            return
        }else{
            // When permission is granted
            //var location = provider?.let { locationManager.getLastKnownLocation(it) }
            var location = provider?.let { locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5f,this)}
            var l = provider?.let { locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5f,this)}
            // Last know location will not go and get the location itself it will only see if the phone fetched a location before and then it will give you that location
            if(provider != null){
                var location =  locationManager.requestLocationUpdates(provider,400,1f,this)
                Log.e(TAG, "getLocation: ${location.toString()}", )
            }
            Log.e(TAG, "getLocation: ${location.toString()}")
        }
    }

    private fun requestPermission() {
        if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) { // if the user has denied the location access before this fires
            AlertDialog.Builder(this)
                .setMessage("Allow ${getString(com.example.weatherapp.R.string.app_name)} To use your location to show local weather")
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        100
                    )
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
        }
        //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),100)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e(TAG, "onRequestPermissionsResult: ${grantResults.toString()}", )
        Log.e(TAG, "onRequestPermissionsResult: ${permissions}", )
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var cirtiria = Criteria()
        var provider = locationManager.getBestProvider(cirtiria,false)
        Log.e(TAG, "onRequestPermissionsResult: ${provider.toString()}", )

        var location = provider?.let { if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
            //provider?.let { locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5f,this)}
//            if(android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.S){
//                locationManager.getCurrentLocation(provider,null,application.mainExecutor, {
//                Log.e(TAG, "Current Location: $it", )
//            })
//            }else{
//                locationManager.getLastKnownLocation(it)
//            }
            locationManager.getLastKnownLocation(it)
        }

        if(provider != null){
            var l =  locationManager.requestLocationUpdates(provider,400,1f,this)
            Log.e(TAG, "Location 1 : ${location.toString()}", )
            Log.e(TAG, "l 1 : ${l.toString()}", )
            if(location != null){
                getAddress(location)
            }else{
                Toast.makeText(this.baseContext,"Couldn't Fetch Address",Toast.LENGTH_LONG).show()
            }

            //Log.e(TAG, "getLocation: ${location.toString()}", )
        }
        Log.e(TAG, "onRequestPermissionsResult 2: ${location.toString()}")

        // Last know location will not go and get the location itself it will only see if the phone fetched a location before and then it will give you that location

        //Log.e(TAG, "onRequestPermissionsResult: ${LocationRequest.CREATOR.createFromParcel().toString()}", )
        // WHEN THE USER PRESSES ALLOW LOCATION THIS FIRES
    }
    private fun getAddress(location: Location){
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
        val address : List<Address>
        address = geocoder.getFromLocation(location.latitude,location.longitude,1) // 1 represents the max location returned
        Log.e(TAG, "getAddress: $address", )
        val l = address[0]
        val city = l.locality
        Log.e(TAG, "City: ${city}")
        val country = l.countryName
        Log.e(TAG, "Country: ${country}")
        //TODO: IMPLEMENT CHANGE IN CITY DEPENDING ON LOCATION.
        //SharedPrefrencesManager.writeCurrentCity("CurrentCity",city)
        SharedPrefrencesManager.writeCitiesList("Cities",city)
        this.viewModel.getWeather(city).invokeOnCompletion { it ->
            this.viewModel.addCity()
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        super.onStatusChanged(provider, status, extras) // TODO: search ip this method
    }

    override fun onLocationChanged(location: Location) {
        Log.e(TAG, "onLocationChanged: $location")
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider) // TODO: search ip this method
    }
}