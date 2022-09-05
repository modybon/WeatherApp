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
import java.util.*

class SearchPage() : AppCompatActivity(), SearchView.OnQueryTextListener, WeatherInfoDialogInterface, LocationListener{
    private val TAG = this@SearchPage.toString()
    lateinit var binding : ActivitySearchPageBinding
    lateinit var viewModel: ViewModel
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var swipeHelper: ItemTouchHelper
    lateinit var locationManager: LocationManager
    private val saved = "WeatherList"
    private lateinit var dialog : PopUpWeatherInfoDialog
    var cityInfo : CityWheatherInfo = CityWheatherInfo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Always call the super class first
        Log.e(TAG, "onCreate: Search Page onCreate", )
        this.viewModel = ViewModel(this.application)
        this.dialog = PopUpWeatherInfoDialog()
        if(savedInstanceState != null){
            with(savedInstanceState){
                Log.e(TAG, "onCreate dialog state1: ${getBoolean("showDialog")}")
                Log.e(TAG, "onCreate dialog state2: ${getParcelable<CityWheatherInfo>("cityDialog")}")
                Log.e(TAG, "onCreate dialog state3: ${getParcelableArrayList<CityWheatherInfo>(saved)}")
                viewModel.list = getParcelableArrayList("WeatherList")?: arrayListOf()
                viewModel.citiesWheatherList.value = getParcelableArrayList("WeatherList")
                cityInfo = getParcelable("cityDialog")?: CityWheatherInfo()
            }
        }else{
            this.viewModel.setUp(applicationContext)
        }
        this.binding = ActivitySearchPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.binding.recyclerView.layoutManager = LinearLayoutManager(this)
        this.binding.searchBar.setOnQueryTextListener(this)

        recyclerViewAdapter = RecyclerViewAdapter(this,this.viewModel,this)
        this.binding.recyclerView.adapter = recyclerViewAdapter
        this.viewModel.citiesWheatherList.observe(this){
            recyclerViewAdapter.notifyDataSetChanged()
            Log.e(TAG, "SIZE onCreate: ${this.viewModel.citiesWheatherList.value?.size}")
            Log.e(TAG, "VALUE onCreate: ${this.viewModel.citiesWheatherList.value}")
        }
        this.binding.recyclerView.addItemDecoration(MarginItemDecoration(40))
        getLocation()
        swipeGesture()
        this.swipeHelper.attachToRecyclerView(this.binding.recyclerView)

    }
    override fun onSaveInstanceState(outState: Bundle) {
        Log.e(TAG, "onSaveInstanceState: saved instance been called")
        outState.putParcelableArrayList(saved,this.viewModel.citiesWheatherList.value)
        outState.putParcelable("cityDialog",cityInfo)
        super.onSaveInstanceState(outState)
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
                //Log.e(TAG, "onSwiped: ${viewHolder.layoutPosition}")
                //Log.e(TAG, "onSwiped City: ${viewModel.citiesWheatherList.value?.get(viewHolder.adapterPosition)?.cityName!!}")
                try {
                    viewModel.removeCity(viewModel.citiesWheatherList.value?.get(viewHolder.adapterPosition)?.cityName!!)
                    viewModel.citiesWheatherList.value!!.removeAt(viewHolder.adapterPosition)
                    recyclerViewAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                }catch (e : Exception){
                    Log.e(TAG, "onSwiped Error: ${e.message}", )
                }
            }

            override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                Log.e(TAG, "getSwipeDirs: ${viewModel.citiesWheatherList.value?.size}", )
                Log.e(TAG, "getSwipeDirs: ${viewModel.citiesWheatherList.value}", )
                if(viewModel.citiesWheatherList.value?.get(viewHolder.adapterPosition)?.cityName == viewModel.getCurrentCity()) return 0
                return super.getSwipeDirs(recyclerView, viewHolder)
            }
        })
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
        //Log.e(TAG, "onQueryTextSubmit: $query")
        viewModel.getWeatherAsync(query).invokeOnCompletion {
            cityInfo = this.viewModel.cityWheatherInfo
            //val dialog = PopUpWeatherInfoDialog()
            dialog.show(this.supportFragmentManager,"MyDialog")
            Log.e(TAG, "onQueryTextSubmit: ${this.viewModel.citiesWheatherList.value}", )
        }
        return false
    }

    override fun onClickListener(cityWheatherInfo: CityWheatherInfo,position: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putParcelableArrayListExtra("CitiesList",this.viewModel.citiesWheatherList.value)
        intent.putExtra("ItemSelectedPosition",position)
        // TODO: HAVE THE GET POSITON BE SEPERATE FROM THE ONCLICKLISTENER
        startActivity(intent)
    }

    fun getLocation(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager// provides access to the system location services and these services
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
            // Requesting location updates is what triggers onLocationChanged
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5f,this)
            //var location = provider?.let { locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5f,this)}
            //var l = provider?.let { locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5f,this)}
            // Last know location will not go and get the location itself it will only see if the phone fetched a location before and then it will give you that location
            if(provider != null){
                var location =  locationManager.requestLocationUpdates(provider,400,1f,this)
                Log.e(TAG, "getLocation: ${location.toString()}", )
            }
            //Log.e(TAG, "getLocation: ${location.toString()}")
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
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5f,this)
                    locationManager.getLastKnownLocation(it)
                }
            }
        location?.let {
            var city = getCity(it)
            city?.let{
                this.viewModel.getWeatherAsync(it).invokeOnCompletion {
                    // TODO: MOVE IT TO VIEWMODEL
                    viewModel.writeCurrentCity(city)
                    viewModel.writeCitiesList(city)
                    this.viewModel.addCities()
                }
                return@onRequestPermissionsResult
            }
            Toast.makeText(this.baseContext,"Couldn't Fetch Current Location",Toast.LENGTH_LONG).show()
        }
    }

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
        var city = getCity(location) ?: viewModel.getCurrentCity()
        Log.e(TAG, "onLocationChanged: $city")
        if(city != viewModel.getCurrentCity()){
            Log.e(TAG, "onLocationChanged New City: $city")
            // TODO: CHANGE CURRENT CITY
            this.viewModel.currentCityChanged(city)
            return
        }
        Log.e(TAG, "onLocationChanged: Current City Hasn't Changed")
    }

    override fun onBackPressed() {
        Log.e(TAG, "onBackPressed: Back Been Pressed", )
        super.onBackPressed()
    }

    override fun onDestroy() {
        // Because location manager has mcontext field that reference to activity
        // it needs remove the updates when destroyed so that memeory wouldn't be leaked
        // most memory leaks are because of context reference of activities that are destroyed
        locationManager.removeUpdates(this)
        Log.e(TAG, "onDestroy: Search Page Destroyed", )
        //if (dialog.isVisible) dialog.dismiss()
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

}






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