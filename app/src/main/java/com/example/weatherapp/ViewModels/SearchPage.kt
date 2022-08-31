package com.example.weatherapp.ViewModels

import android.Manifest
import android.app.AlertDialog
import android.app.SearchManager
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Models.*
import com.example.weatherapp.databinding.ActivitySearchPageBinding
import com.example.weatherapp.managers.SharedPrefrencesManager
import java.util.*

class SearchPage() : AppCompatActivity(), SearchView.OnQueryTextListener, WeatherInfoDialogInterface, LocationListener{
    private val TAG = this@SearchPage.toString()
    lateinit var binding : ActivitySearchPageBinding
    lateinit var viewModel: ViewModel
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var swipeHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, "onCreate: Search Page onCreate", )
        super.onCreate(savedInstanceState)
        this.binding = ActivitySearchPageBinding.inflate(layoutInflater)
        this.viewModel = ViewModel(this.application)
        var n : String? = null
        n.let {
            Log.e(TAG, "onCreate: $n")
        }
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
        this.binding.recyclerView.addItemDecoration(MarginItemDecoration(40))
        getLocation()
        swipeGesture()
        this.swipeHelper.attachToRecyclerView(this.binding.recyclerView)
    }
    fun swipeGesture(){
        this.swipeHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Log.e(TAG, "onMove: ${viewHolder.adapterPosition}", )
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.e(TAG, "onSwiped: ${viewHolder.layoutPosition}")
                Log.e(TAG, "onSwiped City: ${viewModel.citiesWheatherList.value?.get(viewHolder.adapterPosition)?.cityName!!}")
                try {
                    SharedPrefrencesManager.removeCity("Cities",viewModel.citiesWheatherList.value?.get(viewHolder.adapterPosition)?.cityName!!)
                    viewModel.citiesWheatherList.value!!.removeAt(viewHolder.adapterPosition)
                    recyclerViewAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                }catch (e : Exception){
                    Log.e(TAG, "onSwiped Error: ${e.message}", )
                }
            }

            override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                Log.e(TAG, "getSwipeDirs: ${viewModel.citiesWheatherList.value?.size}", )
                Log.e(TAG, "getSwipeDirs: ${viewModel.citiesWheatherList.value}", )
                if(viewModel.citiesWheatherList.value?.get(viewHolder.adapterPosition)?.cityName == SharedPrefrencesManager.readCurrentCity(viewModel.CURRENT_CITY_KEY)) return 0
                return super.getSwipeDirs(recyclerView, viewHolder)
            }
        })
    }

    override fun onBackPressed() {
        Log.e(TAG, "onBackPressed: Back Been Pressed", )
        super.onBackPressed()
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy: Search Page Destroyed", )
        super.onDestroy()
    }

    override fun onStart() {
        Log.e(TAG, "onStart: Search Page Started", )
        super.onStart()
    }

    override fun onResume() {
        Log.e(TAG, "onResume: Search Page Resumed", )
        super.onResume()
    }

    override fun onRestart() {
        Log.e(TAG, "onRestart: Search Page Restarted", )
        super.onRestart()
    }

    override fun finish() {
        Log.e(TAG, "finish: Activity Finished", )
        super.finish()
    }

    override fun onStop() {
        Log.e(TAG, "onStop: Search Page Stopped", )
        super.onStop()
    }

    override fun onPause() {
        Log.e(TAG, "onPause: Search Page onPaused", )
        super.onPause()
    }

    override fun onQueryTextChange(newText: String): Boolean {
        Log.e(TAG, "onQueryTextChange: $newText", )
        return false
    }

    override fun onSearchRequested(): Boolean {
        Log.e(TAG, "onSearchRequested: Existed Search Search")
        return super.onSearchRequested()
    }
    override fun onQueryTextSubmit(query: String): Boolean {
        // task HERE
        if(Intent.ACTION_SEARCH == intent.action){
            intent.getStringExtra(SearchManager.QUERY).also { query->
                Log.e(TAG, "onCreate: $query", )
            }
        }
        Log.e(TAG, "onQueryTextSubmit: $query")
        viewModel.getWeatherAsync(query).invokeOnCompletion {
            val myDialog = PopUpWeatherInfoDialog(this.viewModel.cityWheatherInfo,this,this.viewModel)
            myDialog.show(this.supportFragmentManager,"MyDialog")
        }
        return false
    }

    override fun onClickListener(cityWheatherInfo: CityWheatherInfo,position: Int) {
        //Log.e(TAG, "onClickListener: City Clicked")
        //Log.e(TAG, "onClickListener: $cityWheatherInfo +\nPosition: $position")
        var bundle = Bundle()
        var l = this.viewModel.citiesWheatherList.value
        //Log.e(TAG, "onClickListener Value: $l", )
        bundle.putParcelableArrayList("List",this.viewModel.citiesWheatherList.value)
        val intent = Intent(this, MainActivity::class.java)
        //intent.putExtra("CitiesList",bundle)
        intent.putParcelableArrayListExtra("CitiesList",this.viewModel.citiesWheatherList.value)
        intent.putExtra("ItemSelectedPosition",position)
        // TODO: HAVE THE GET POSITON BE SEPERATE FROM THE ONCLICKLISTENER
        startActivity(intent)
    }

    fun getLocation(){
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager// provides access to the system location services and these services
        // allow applications to obtain periodic updates of the device's geographical location
        var cirtiria = Criteria() // indicating the application criteria for selecting a location provider.
        // Providers may be ordered according to accuracy, power usage, ability to report altitude, speed, bearing, and monetary cost.
        var provider = locationManager.getBestProvider(cirtiria,false)

        val hasAccesCoarseLocationPermission = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        val hasAccesFineLocationPermission = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        val isGpsEnabled : Boolean = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        Log.e(TAG, "getLocation Provider: ${provider.toString()}")

        if(!hasAccesCoarseLocationPermission || !hasAccesFineLocationPermission || !isGpsEnabled){
            Log.e(TAG, "getLocation: Permission not granted yet ", )
            this.requestPermission()
            return
        }
        else{
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
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e(TAG, "onRequestPermissionsResult 1: $grantResults")
        Log.e(TAG, "onRequestPermissionsResult 2: $permissions")
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var cirtiria = Criteria()
        var provider = locationManager.getBestProvider(cirtiria,false)
        Log.e(TAG, "onRequestPermissionsResult: ${provider.toString()}", )
        Log.e(TAG, "onRequestPermissionsResult 3: ${grantResults.get(0)}", )
        var location =
            provider?.let {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }else{
                    locationManager.getLastKnownLocation(it)
                }
            }
        location?.let {
            var city = getCity(it)
            city?.let{
                this.viewModel.getWeatherAsync(it).invokeOnCompletion {
                    this.viewModel.addCity()
                }
                return@onRequestPermissionsResult
            }
            Toast.makeText(this.baseContext,"Couldn't Fetch Current Location",Toast.LENGTH_LONG).show()
        }
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
        SharedPrefrencesManager.writeCurrentCity("currentCity",city)
        SharedPrefrencesManager.writeCitiesList("Cities",city)
        this.viewModel.getWeatherAsync(city).invokeOnCompletion { it ->
            this.viewModel.addCity()
        }
    }
//    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//        super.onStatusChanged(provider, status, extras) // TODO: search ip this method
//    }

    fun getCity(location: Location) : String?{
        var city : String? = null
        try{
            val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
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
    override fun onLocationChanged(location: Location) {
        Log.e(TAG, "onLocationChanged new Location: $location")
        var city = getCity(location) ?: SharedPrefrencesManager.readCurrentCity(this.viewModel.CURRENT_CITY_KEY)
        Log.e(TAG, "onLocationChanged: $city")
        if(city != SharedPrefrencesManager.readCurrentCity(viewModel.CURRENT_CITY_KEY)){
            Log.e(TAG, "onLocationChanged New City: $city")
            // TODO: CHANGE CURRENT CITY
            this.viewModel.currentCityChanged(city)
            return
        }
        Log.e(TAG, "onLocationChanged: Current City Hasn't Changed")
//        var oldCity = SharedPrefrencesManager.readCurrentCity(this.viewModel.CURRENT_CITY_KEY)
//        Log.e(TAG, "onLocationChanged Old City: $oldCity")
//        if(city != oldCity){
//            this.viewModel.currentCityChanged(city)
//        }
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
    // TODO: search ip this method
    }
}



//        if(city != oldCity && oldCity != null){
//            this.viewModel.getWeather(city).invokeOnCompletion {
//                Log.e(TAG, "onLocationChanged Cities Before Removal: ${this.viewModel.citiesWheatherList.value}")
//                SharedPrefrencesManager.removeCity("Cities",this.viewModel.citiesWheatherList.value?.get(0)?.cityName!!)
//                //SharedPrefrencesManager.readCities("Cities").remove(this.viewModel.citiesWheatherList.value?.get(0)?.cityName)
//                Log.e(TAG, "onLocationChanged Cities After Removal: ${this.viewModel.citiesWheatherList.value}")
//                Log.e(TAG, "onLocationChanged Current City before change: ${SharedPrefrencesManager.readCurrentCity("currentCity")}")
//                SharedPrefrencesManager.writeCurrentCity("currentCity",city)
//                Log.e(TAG, "onLocationChanged Current City After change: ${SharedPrefrencesManager.readCurrentCity("currentCity")}")
//                SharedPrefrencesManager.writeCitiesList("Cities",city)
//                this.viewModel.citiesWheatherList.value?.removeFirst()
//                this.viewModel.addCity()
//                //Log.e(TAG, "onLocationChanged: ${this.viewModel.cityWheatherInfo}", )
//                //Log.e(TAG, "onLocationChanged: Current City Has Changed ${it?.message.toString()}", )
//                //Log.e(TAG, "onLocationChanged: Error: ${it?.message.toString()}")
//            }
//        }else{
//            Log.e(TAG, "onLocationChanged: City Hasn't Changed", )
//        }







//        if(grantResults.get(0) == PackageManager.PERMISSION_GRANTED){
//            Log.e(TAG, "onRequestPermissionsResult: Granted", )
//
//            return
//        }
//Log.e(TAG, "onRequestPermissionsResult: Not Granted", )
//        var location = provider?.let {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            return@onRequestPermissionsResult
//        }
//            locationManager.getLastKnownLocation(it)
//        }
//        provider.let { // The code inside the let expression is executed only when the property is not null.
//            var l =  locationManager.requestLocationUpdates(it!!,400,1f,this)
//            Log.e(TAG, "Location 1 : ${location.toString()}", )
//            Log.e(TAG, "l 1 : ${l.toString()}", )
//            if(location != null){
//                getAddress(location)
//            }else{
//                Toast.makeText(this.baseContext,"Couldn't Fetch Address",Toast.LENGTH_LONG).show()
//            }
//        }

// Last know location will not go and get the location itself it will only see if the phone fetched a location before and then it will give you that location

//Log.e(TAG, "onRequestPermissionsResult: ${LocationRequest.CREATOR.createFromParcel().toString()}", )
// WHEN THE USER PRESSES ALLOW LOCATION THIS FIRES