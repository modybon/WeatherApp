package com.example.weatherapp.Database

import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.Models.CityWheatherInfo

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

class CitiesRepositoriy {
 var allCities : MutableLiveData<List<CityWheatherInfo>> = MutableLiveData<List<CityWheatherInfo>>()
 init {
  var w1 = CityWheatherInfo()
  var w2 = CityWheatherInfo()
  var w3 = CityWheatherInfo()
  w1.cityName = "Cairo"
  w2.cityName = "Jaddah"
  w3.cityName = "Alexanderia"
  var list = arrayListOf<CityWheatherInfo>()
  list.add(w1)
  list.add(w2)
  list.add(w3)
  allCities.value = list
 }
}