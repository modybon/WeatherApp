package com.example.weatherapp.ViewModels

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Database.CitiesRepositoriy
import com.example.weatherapp.Models.CityWheatherInfo
import com.example.weatherapp.managers.SharedPrefrencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = this@ViewModel.toString()
    private val key: String = "fd6b5b197e705ecbec520c688fc640bb"
    private var metrics: String = "metric"
    var list: ArrayList<CityWheatherInfo> = arrayListOf<CityWheatherInfo>()
    var icon: Bitmap? = null

    //var citiesWheatherList : MutableLiveData<List<CityWheatherInfo>>
    var citiesWheatherList: MutableLiveData<ArrayList<CityWheatherInfo>>
    var cityWheatherInfo: CityWheatherInfo

    init {
        this.citiesWheatherList = MutableLiveData<ArrayList<CityWheatherInfo>>()
        cityWheatherInfo = CityWheatherInfo()
    }

    fun loadWeather(city: String) {
        var ins: InputStream? = null;
        var u: String =
            "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=${this.key}&units=$metrics"
        var result: String
        try {
            var url = URL(u)
            var connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            //Log.e(TAG, " Worked loadWeather: ${connection.responseCode}", )
            ins = connection.inputStream
            result = ins.bufferedReader().use(BufferedReader::readText)
            Log.e(TAG, "Result loadWeather: ${result}")
            var json = JSONObject(result)
            var jsonarr = json.toJSONArray(json.names())
            Log.e(TAG, "jsonarray: ${jsonarr}")
            var obj = jsonarr.getJSONArray(1).get(0) as JSONObject

            var infodesc = jsonarr[1] as JSONArray
            Log.e(TAG, "Info Desc: ${(infodesc[0] as JSONObject).get("description")}")
            var tempInfo =
                jsonarr[3] as JSONObject // temp, feels Like , temp_min, temp_max, pressure, humidity
            Log.e(TAG, "Temp Info : ${tempInfo}")
            var tempInfo2 = jsonarr[4] // Pressure
            Log.e(TAG, "Temp Info 2: ${tempInfo2}")
            var tempInfo3 = jsonarr[5] as JSONObject // Wind Speed , Wind Degree
            Log.e(TAG, "Temp Info 3: ${tempInfo3}")
            var tempInfo4 = jsonarr[11] // City
            Log.e(TAG, "Temp Info 4: ${tempInfo4}")
            var tempInfo5 = jsonarr[8] as JSONObject  // Country , Sun Rise , Sun Set
            Log.e(TAG, "Temp Info 5: ${tempInfo5.get("country")}")
            //Log.e(TAG, "TempInfo 5: $tempInfo5")

//   var feelsLike = tempInfo.getString("feels_like").toFloat()
//   var temp = tempInfo.getString("temp").toFloat()
//   var highTemp = tempInfo.getString("temp_max").toFloat()
//   var lowTemp = tempInfo.getString("temp_min").toFloat()
//   var pressure = tempInfo.getString("pressure").toInt()
//   var humidity = tempInfo.getString("humidity").toInt()
//   var visibility = tempInfo2.toString().toInt();
//   var windSpeed = tempInfo3.getString("speed").toFloat()
//   var windDegree = tempInfo3.getString("deg").toFloat()
//   var cityName = tempInfo4.toString()
//   var country = tempInfo5.get("country").toString()
            when ((infodesc[0] as JSONObject).get("icon").toString()) {
                "01n" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.onen)
                "01d" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.oned)
                "02d" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.twod)
                "02n" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.twon)
                "03d" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.threed)
                "03n" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.threen)
                "04d" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.fourd)
                "04n" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.fourn)
                "09d" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.nined)
                "09n" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.ninen)
                "10d" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.tend)
                "10n" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.tenn)
                "11d" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.elevend)
                "11n" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.elevenn)
                "13d" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.thirteend)
                "13n" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.thirteenn)
                "50d" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.fiftyd)
                "50n" -> this.cityWheatherInfo.icon = (com.example.weatherapp.R.drawable.fiftyn)
            }
            this.cityWheatherInfo.cityName = tempInfo4.toString()
            this.cityWheatherInfo.temp = tempInfo.getString("temp").toFloat()
            this.cityWheatherInfo.feelsLike = tempInfo.getString("feels_like").toFloat()
            this.cityWheatherInfo.highTemp = tempInfo.getString("temp_max").toFloat()
            this.cityWheatherInfo.lowTemp = tempInfo.getString("temp_min").toFloat()
            this.cityWheatherInfo.humidity = tempInfo.getString("humidity").toInt()
            this.cityWheatherInfo.pressure = tempInfo.getString("pressure").toInt()
            this.cityWheatherInfo.visibility = tempInfo2.toString().toInt();
            this.cityWheatherInfo.windSpeed = tempInfo3.getString("speed").toFloat()
            this.cityWheatherInfo.windDegree = tempInfo3.getString("deg").toFloat()
            this.cityWheatherInfo.countryName = tempInfo5.get("country").toString()
            this.cityWheatherInfo.desc = (infodesc[0] as JSONObject).get("description").toString()
            //this.cityWheatherInfo.icon = (infodesc[0] as JSONObject).get("icon").toString() + ".png"

            Log.e(TAG, "TEST RUN: ${cityWheatherInfo.toString()}")
            Log.e(
                TAG,
                "TEST RUN ------------------------------------------------------------------------------"
            )

            Log.e(TAG, "Obj: ${obj}")

        } catch (exec: Exception) {
            Log.e(TAG, "Error loadWeather: ${exec.message}")
            this.cityWheatherInfo.cityName = city
        }
    }

    fun getWeather(city: String) = viewModelScope.launch(Dispatchers.IO) {
        loadWeather(city)
    }

    fun setUp(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        SharedPrefrencesManager.init(context)
        Log.e(TAG, "setUp: ${SharedPrefrencesManager.readCities("Cities")}", )
        SharedPrefrencesManager.readCities("Cities").forEach { city ->
            Log.e(TAG, "TEST RUN onCreate: City Name: $city")
            //Log.e(TAG, "TEST RUN setUp: ${citiesWheatherList.value!!.size.toString()}", )
            //getWeather(city).invokeOnCompletion { addCity() }
            loadWeather(city)
            addCity()
        }
    }

    fun addCity() = viewModelScope.launch(Dispatchers.IO) {
        if (cityWheatherInfo.cityName == SharedPrefrencesManager.readCurrentCity("currentCity")) {
            list.add(0, cityWheatherInfo)
        } else {
            list.add(list.size, cityWheatherInfo)
        }
        Log.e(TAG, "addCity: ${list.size}")
        citiesWheatherList.postValue(list)
        //citiesWheatherList.value = list
        //citiesRep.allCities.postValue(list)
        cityWheatherInfo = CityWheatherInfo()
    }

    fun currentCityChanged(city: String) = viewModelScope.launch(Dispatchers.IO){
        var oldCity = SharedPrefrencesManager.readCurrentCity("currentCity")
        if(city != oldCity && oldCity != null){
            getWeather(city).invokeOnCompletion {
                Log.e(TAG, "onLocationChanged Cities Before Removal: ${citiesWheatherList.value}")
                SharedPrefrencesManager.removeCity("Cities",citiesWheatherList.value?.get(0)?.cityName!!)
                //SharedPrefrencesManager.readCities("Cities").remove(this.viewModel.citiesWheatherList.value?.get(0)?.cityName)
                Log.e(TAG, "onLocationChanged Cities After Removal: ${citiesWheatherList.value}")
                Log.e(TAG, "onLocationChanged Current City before change: ${SharedPrefrencesManager.readCurrentCity("currentCity")}")
                SharedPrefrencesManager.writeCurrentCity("currentCity",city)
                Log.e(TAG, "onLocationChanged Current City After change: ${SharedPrefrencesManager.readCurrentCity("currentCity")}")
                SharedPrefrencesManager.writeCitiesList("Cities",city)
                citiesWheatherList.value?.removeFirst()
                addCity()
                //Log.e(TAG, "onLocationChanged: ${this.viewModel.cityWheatherInfo}", )
                //Log.e(TAG, "onLocationChanged: Current City Has Changed ${it?.message.toString()}", )
                //Log.e(TAG, "onLocationChanged: Error: ${it?.message.toString()}")
            }
        }else{
            Log.e(TAG, "onLocationChanged: City Hasn't Changed", )
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.e(TAG, "onCleared: ")
    }
}