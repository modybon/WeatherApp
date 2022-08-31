package com.example.weatherapp.ViewModels

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Models.CityWheatherInfo
import com.example.weatherapp.Models.WeatherType
import com.example.weatherapp.managers.SharedPrefrencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

class ViewModel (application: Application) : AndroidViewModel(application) {
    private val TAG = this@ViewModel.toString()
    private val key: String = "fd6b5b197e705ecbec520c688fc640bb"
    private var metrics: String = "metric"
    var list: ArrayList<CityWheatherInfo> = arrayListOf<CityWheatherInfo>()
    var icon: Bitmap? = null
    var CITIES_LIST_KEY = "Cities"
    var CURRENT_CITY_KEY = "currentCity"
    var currentCity = SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)
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
            var tempInfo = jsonarr[3] as JSONObject // temp, feels Like , temp_min, temp_max, pressure, humidity
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
                "01n" ->{
                    this.cityWheatherInfo.backgroundColor = WeatherType.clearSkyDay.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.clearSkyDay.iconRes
                }
                "01d" -> {
                    this.cityWheatherInfo.backgroundColor = WeatherType.clearSkyNight.backgroundColor
                    this.cityWheatherInfo.icon = WeatherType.clearSkyNight.iconRes
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
            this.cityWheatherInfo.countryName = tempInfo5.get("country").toString()
            this.cityWheatherInfo.desc = (infodesc[0] as JSONObject).get("description").toString()
//            Log.e(TAG, "TEST RUN: ${cityWheatherInfo.toString()}")
//            Log.e(
//                TAG,
//                "TEST RUN ------------------------------------------------------------------------------"
//            )
//            Log.e(TAG, "Obj: ${obj}")

        } catch (exec: Exception) {
            Log.e(TAG, "Error loadWeather: ${exec.message}")
            this.cityWheatherInfo.cityName = city
        }
    }

    fun getWeatherAsync(city: String) = viewModelScope.launch(Dispatchers.IO) {
        loadWeather(city)
    }

    fun setUp(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        SharedPrefrencesManager.init(context)
        Log.e(TAG, "setUp: ${SharedPrefrencesManager.readCities(CITIES_LIST_KEY)}", )
        SharedPrefrencesManager.readCities(CITIES_LIST_KEY).forEach { city ->
            Log.e(TAG, "TEST RUN onCreate: City Name: $city")
            //Log.e(TAG, "TEST RUN setUp: ${citiesWheatherList.value!!.size.toString()}", )
//            getWeather(city).invokeOnCompletion {
//                addCity()
//            }
            loadWeather(city)
            addCity()
        }
    }

    fun addCity(){
        var isDuplicate : Boolean = false
        citiesWheatherList.value?.forEach {
            if(it.cityName == cityWheatherInfo.cityName){
                isDuplicate = true
                return@forEach
            }
        }
        if(!isDuplicate){
            if (cityWheatherInfo.cityName == SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)) {
                list.add(0, cityWheatherInfo)
            } else {
                list.add(list.size, cityWheatherInfo)
            }
            //Log.e(TAG, "addCity: ${list.size}")
            citiesWheatherList.postValue(list) // notifies observes that data has changed
            //citiesWheatherList.value = list // Cannot use setValue because its on a background thread
            cityWheatherInfo = CityWheatherInfo()
        }
    }

    fun currentCityChanged(city: String) = viewModelScope.launch(Dispatchers.IO){
        var oldCity = SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)
        //Log.e(TAG, "currentCityChanged: ${citiesWheatherList.value.isNullOrEmpty()}", )
        // TODO : TRY TO USE LET BECAUSE The code inside the let expression is executed only when the property is not null
        // TODO: DIFFRENCE BETWEEN also and let is that also would return the object while let you would need to specify the return or change the value
        loadWeather(city)
        if((citiesWheatherList.value.isNullOrEmpty())){
            //Log.e(TAG, "onLocationChanged Cities Before Removal: ${citiesWheatherList.value}")
            //Log.e(TAG, "onLocationChanged Cities After Removal: ${citiesWheatherList.value}")
            //Log.e(TAG, "onLocationChanged Current City before change: ${SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)}")
            SharedPrefrencesManager.writeCurrentCity("currentCity",city)
            citiesWheatherList.value?.removeFirst()
            Log.e(TAG, "onLocationChanged Current City After change: ${SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)}")
            SharedPrefrencesManager.writeCitiesList(CITIES_LIST_KEY,city)
            addCity()
        }else{
            //Log.e(TAG, "onLocationChanged Cities Before Removal: ${citiesWheatherList.value}")
            if(citiesWheatherList.value?.get(0)?.cityName!! == SharedPrefrencesManager.readCurrentCity("currentCity")){
                SharedPrefrencesManager.removeCity(CITIES_LIST_KEY,citiesWheatherList.value?.get(0)?.cityName!!)
                //Log.e(TAG, "onLocationChanged Cities After Removal: ${citiesWheatherList.value}")
                //Log.e(TAG, "onLocationChanged Current City before change: ${SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)}")
                SharedPrefrencesManager.writeCurrentCity("currentCity",city)
                //Log.e(TAG, "onLocationChanged Current City After change: ${SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)}")
                SharedPrefrencesManager.writeCitiesList(CITIES_LIST_KEY,city)
                citiesWheatherList.value?.removeFirst()
                addCity()
            }else{
                //Log.e(TAG, "onLocationChanged Cities After Removal: ${citiesWheatherList.value}")
                //Log.e(TAG, "onLocationChanged Current City before change: ${SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)}")
                SharedPrefrencesManager.writeCurrentCity("currentCity",city)
                //Log.e(TAG, "onLocationChanged Current City After change: ${SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)}")
                SharedPrefrencesManager.writeCitiesList(CITIES_LIST_KEY,city)
                addCity()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.e(TAG, "onCleared: ")
    }
}




//
//
//if(city != oldCity && oldCity != null){
//    getWeatherAsync(city).invokeOnCompletion {
//        if((citiesWheatherList.value.isNullOrEmpty())){
//            //Log.e(TAG, "onLocationChanged Cities Before Removal: ${citiesWheatherList.value}")
//            //Log.e(TAG, "onLocationChanged Cities After Removal: ${citiesWheatherList.value}")
//            //Log.e(TAG, "onLocationChanged Current City before change: ${SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)}")
//            SharedPrefrencesManager.writeCurrentCity("currentCity",city)
//            Log.e(TAG, "onLocationChanged Current City After change: ${SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)}")
//            SharedPrefrencesManager.writeCitiesList(CITIES_LIST_KEY,city)
//            citiesWheatherList.value?.removeFirst()
//            addCity()
//        }else{
//            //Log.e(TAG, "onLocationChanged Cities Before Removal: ${citiesWheatherList.value}")
//            if(citiesWheatherList.value?.get(0)?.cityName!! == SharedPrefrencesManager.readCurrentCity("currentCity")){
//                SharedPrefrencesManager.removeCity(CITIES_LIST_KEY,citiesWheatherList.value?.get(0)?.cityName!!)
//                //Log.e(TAG, "onLocationChanged Cities After Removal: ${citiesWheatherList.value}")
//                //Log.e(TAG, "onLocationChanged Current City before change: ${SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)}")
//                SharedPrefrencesManager.writeCurrentCity("currentCity",city)
//                //Log.e(TAG, "onLocationChanged Current City After change: ${SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)}")
//                SharedPrefrencesManager.writeCitiesList(CITIES_LIST_KEY,city)
//                citiesWheatherList.value?.removeFirst()
//                addCity()
//            }else{
//                //Log.e(TAG, "onLocationChanged Cities After Removal: ${citiesWheatherList.value}")
//                //Log.e(TAG, "onLocationChanged Current City before change: ${SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)}")
//                SharedPrefrencesManager.writeCurrentCity("currentCity",city)
//                //Log.e(TAG, "onLocationChanged Current City After change: ${SharedPrefrencesManager.readCurrentCity(CURRENT_CITY_KEY)}")
//                SharedPrefrencesManager.writeCitiesList(CITIES_LIST_KEY,city)
//                addCity()
//            }
//        }
//    }
//}else{
//    Log.e(TAG, "onLocationChanged: City Hasn't Changed", )
//}