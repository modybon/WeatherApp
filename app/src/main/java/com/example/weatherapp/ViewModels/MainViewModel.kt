package com.example.weatherapp.ViewModels

import androidx.lifecycle.ViewModel
import com.example.weatherapp.Models.CityWheatherInfo

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

class MainViewModel: ViewModel() {
    var citiesList : ArrayList<CityWheatherInfo> = ArrayList()
    var selectedCity : CityWheatherInfo = CityWheatherInfo()
    fun selectedCityInfo(position: Int){
        selectedCity = citiesList[position]
    }
}