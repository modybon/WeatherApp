package com.example.weatherapp.Models

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.ViewModels.SearchViewModel
import com.example.weatherapp.databinding.FragmentWeatherPageBinding
import java.lang.IllegalStateException

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

class PopUpWeatherInfoDialog: DialogFragment() {
    private val TAG = this@PopUpWeatherInfoDialog.toString()
    private lateinit var binding : FragmentWeatherPageBinding
    private val viewModel : SearchViewModel by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var cityinfo = viewModel.cityWheatherInfo
        return activity.let {
            val builder = AlertDialog.Builder(it)
            builder.setPositiveButton("Add") { dialog, id ->
                viewModel.addCity()
                viewModel.addCityToCitiesList(cityinfo.cityName!!)
                Log.e(TAG, "onCreateDialog: ${ viewModel.citiesWheatherList.value}")
                Log.e(TAG, "onCreateDialog cities: ${viewModel.getSavedCities()}", )
                viewModel.cityWheatherInfo = CityWheatherInfo()
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") {dialog, id->
                viewModel.cityWheatherInfo = CityWheatherInfo()
                dialog.dismiss()
            }
            this.binding = FragmentWeatherPageBinding.inflate(layoutInflater)
            builder.setView(this.binding.root)
            this.binding.cityNameTv.setText(cityinfo.cityName)
            this.binding.tempTv.setText(String.format("%.0f",cityinfo.temp))
            this.binding.feelsLikeTextView.setText(String.format("%.0f",cityinfo.feelsLike) + '°')
            this.binding.highTempTv.setText("H:${String.format("%.0f",cityinfo.highTemp) + '°'}")
            this.binding.lowTempTv.setText("L:${String.format("%.0f",cityinfo.lowTemp) + '°'}")
            this.binding.humidityTv.setText(cityinfo.humidity.toString() + '%')
            this.binding.pressureTextView.setText(cityinfo.pressure.toString() + "hPa")
            this.binding.visibilityTv.setText(cityinfo.visibility.toString() + "Km")
            this.binding.windTextView.setText(cityinfo.windSpeed.toString())
            this.binding.descTv.setText(cityinfo.desc)
            this.binding.icon.setBackgroundResource(cityinfo.icon!!)
            this.binding.backGroundLayout.setBackgroundColor(Color.parseColor(cityinfo.backgroundColor ?: "#696969"))
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}