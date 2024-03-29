package com.example.weatherapp.ViewModels

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Models.CityWheatherInfo
import com.example.weatherapp.Models.WeatherType
import com.example.weatherapp.managers.SharedPrefrencesManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

class SearchViewModel (application: Application) : AndroidViewModel(application) {
    private val TAG = this@SearchViewModel.toString()
    private val key: String = "fd6b5b197e705ecbec520c688fc640bb"
    private var metrics: String = "metric"
    var list: ArrayList<CityWheatherInfo> = arrayListOf<CityWheatherInfo>()
    var icon: Bitmap? = null
    var CITIES_LIST_KEY = "Cities"
    var CURRENT_CITY_KEY = "currentCity"
    private var currentCity : String
    var citiesWheatherList: MutableLiveData<ArrayList<CityWheatherInfo>>
    var cityWheatherInfo: CityWheatherInfo
    init {
        SharedPrefrencesManager.init(application)
        currentCity = SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)
        Log.e(TAG, "ViewModel Start")
        this.citiesWheatherList = MutableLiveData<ArrayList<CityWheatherInfo>>()
        cityWheatherInfo = CityWheatherInfo()
    }

    fun loadWeather(city: String) {
        val ins: InputStream?
        val u = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=${this.key}&units=$metrics"
        var result: String
        try {
            val url = URL(u)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            //Log.e(TAG, " Worked loadWeather: ${connection.responseCode}", )
            ins = connection.inputStream
            result = ins.bufferedReader().use(BufferedReader::readText)
            Log.e(TAG, "Result loadWeather: ${result}")
        } catch (exec: Exception) {
            result = ""
            Log.e(TAG, "loadWeather: Couldn't fetch ${city} Info", )
            this.cityWheatherInfo.cityName = city
        }
        if(!result.isEmpty()){
            val json = JSONObject(result)
            val jsonarr = json.toJSONArray(json.names())
            Log.e(TAG, "jsonarray: ${jsonarr}")
            var obj = jsonarr.getJSONArray(1).get(0) as JSONObject
            var infodesc = jsonarr[1] as JSONArray
            Log.e(TAG, "Info Desc: ${(infodesc[0] as JSONObject).get("description")}")
            var tempInfo = jsonarr[3] as JSONObject // temp, feels Like , temp_min, temp_max, pressure, humidity
            Log.e(TAG, "Temp Info : ${tempInfo}")
            var tempInfo2 = jsonarr[4] // Pressure
            Log.e(TAG, "Temp Info 2: ${tempInfo2}")
            var tempInfo3 = jsonarr[5] as JSONObject // Wind Speed , Wind Degree
            Log.e(TAG, "Temp Info 3: ${tempInfo3}")
            var tempInfo4 : String // City
            if(jsonarr[11].toString().toDoubleOrNull() != null){
                tempInfo4 = jsonarr[12].toString()
            }else{
                tempInfo4 = jsonarr[11].toString()
            }
            Log.e(TAG, "Temp Info 4: ${tempInfo4}")
            var tempInfo5 : JSONObject? = null
            try {
                tempInfo5 = jsonarr[8] as JSONObject  // Country , Sun Rise , Sun Set
                Log.e(TAG, "Temp Info 5: ${tempInfo5.get("country")}")
            }catch (ex: Exception){
                Log.e(TAG, "loadWeather: Couldn't fetch Country", )
            }
            when ((infodesc[0] as JSONObject).get("icon").toString()) {
                "01n" ->{
                    this.cityWheatherInfo.backgroundColor = WeatherType.clearSkyNight.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.clearSkyNight.iconRes
                }
                "01d" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.clearSkyDay.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.clearSkyDay.iconRes
                }
                "02d" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.fewCloudsDay.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.fewCloudsNight.iconRes
                }
                "02n" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.fewCloudsNight.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.fewCloudsNight.iconRes
                }
                "03d" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.scatteredCloudsDay.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.scatteredCloudsDay.iconRes
                }
                "03n" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.scatteredCloudsNight.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.scatteredCloudsNight.iconRes
                }
                "04d" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.brokenCloudsDay.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.brokenCloudsDay.iconRes
                }
                "04n" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.brokenCloudsNight.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.brokenCloudsNight.iconRes
                }
                "09d" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.showerRainDay.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.showerRainDay.iconRes
                }
                "09n" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.showerRainNight.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.showerRainNight.iconRes
                }
                "10d" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.rainDay.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.rainDay.iconRes
                }
                "10n" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.rainNight.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.rainNight.iconRes
                }
                "11d" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.thunderstormnDay.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.thunderstormnDay.iconRes
                }
                "11n" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.thunderstormNight.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.thunderstormNight.iconRes
                }
                "13d" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.snownDay.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.thunderstormnDay.iconRes
                }
                "13n" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.snowNight.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.snowNight.iconRes
                }
                "50d" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.mistDay.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.mistDay.iconRes
                }
                "50n" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.mistNight.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.mistNight.iconRes
                }
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
            if (tempInfo5 != null) {
                this.cityWheatherInfo.countryName = tempInfo5.get("country").toString()
            }
            this.cityWheatherInfo.desc = (infodesc[0] as JSONObject).get("description").toString()
        }

    }

    fun getWeatherAsync(city: String) = viewModelScope.launch(Dispatchers.Default) {
        loadWeather(city)
    }

    // IO for is used for databse or remote servers
    // DEFAULT is used for tasks with high CPU usage
    // MAIN is used for UI updates


    fun setUp() = viewModelScope.launch(Dispatchers.IO) {
        Log.e(TAG, "setUp1: ${getSavedCities()}")
        getSavedCities().forEach { city ->
                val threadName = Thread.currentThread().name
                Log.e(TAG, "TEST RUN onCreate: City Name: $city")
                Log.e(TAG, "Thread Name: $threadName")
                loadWeather(city)
                addCity()
            }
    }

    fun addCity(){
        if (cityWheatherInfo.cityName == getCurrentCity()) {
            Log.e(TAG, "addCity: Add Current city to top of list", )
            list.add(0, cityWheatherInfo)
        } else {
            Log.e(TAG, "addCity City: ${cityWheatherInfo.cityName}", )
            Log.e(TAG, "addCity: Add city to list", )
            list.add(list.size, cityWheatherInfo)
        }
        //Log.e(TAG, "addCity: ${list.size}")
        citiesWheatherList.postValue(list) // notifies observes that data has changed
        //citiesWheatherList.value = list // Cannot use setValue because its on a background thread
        cityWheatherInfo = CityWheatherInfo()
    }

    fun cityAlreadyAdded(city: String) : Boolean{
        Log.e(TAG, "addCities: ${citiesWheatherList.value}", )
        citiesWheatherList.value?.forEach {
            if(it.cityName.toString().lowercase() == city.lowercase()){
                return true
            }
        }
        return false
    }
    
    fun currentCityChanged(city: String) = viewModelScope.launch(Dispatchers.Default){
        // TODO : TRY TO USE LET BECAUSE The code inside the let expression is executed only when the property is not null
        // TODO: DIFFRENCE BETWEEN also and let is that also would return the object while let you would need to specify the return or change the value
        // TODO: REFACTOR THIS METHOD
        Log.e(TAG, "currentCityChanged city: $city", )
        loadWeather(city)
        removeCity(getCurrentCity())
        citiesWheatherList.value?.removeFirst()
        cityWheatherInfo.cityName?.let {
            Log.e(TAG, "currentCityChanged 2: $it")
            writeCurrentCity(it)
            addCityToCitiesList(city)
            addCity()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.e(TAG, "onCleared Search Page: View Model Cleared")
    }

    // Shared Preference Methods
    fun removeCity(city: String) = viewModelScope.launch(Dispatchers.IO) {
        SharedPrefrencesManager.removeCity(CITIES_LIST_KEY,city)
    }
    fun writeCurrentCity(city:String){
        Log.e(TAG, "writeCurrentCity: $city", )
        SharedPrefrencesManager.writeCurrentCity(CURRENT_CITY_KEY,city)
        currentCity = city
    }
    fun addCityToCitiesList(city : String){
        SharedPrefrencesManager.addCityToCitiesList(CITIES_LIST_KEY,city)
    }
    fun getCurrentCity(): String{
        Log.e(TAG, "getCurrentCity: $currentCity", )
        return currentCity
    }
    fun getSavedCities() : Set<String>{
        return SharedPrefrencesManager.readCities(CITIES_LIST_KEY)
    }
}



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