package com.example.weatherapp.Models

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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
    private lateinit var cityinfo : CityWheatherInfo
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("cityInfo",cityinfo)
        super.onSaveInstanceState(outState)
    }
    // There's a leak that gets cause because of TextView its a bug in the system they fixed it in android 5.1
    override fun onDestroyView() {
        super.onDestroyView()
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            if(savedInstanceState!=null){
                cityinfo = savedInstanceState.getParcelable<CityWheatherInfo>("cityInfo")?:CityWheatherInfo()
            }else {
                cityinfo = viewModel.cityWheatherInfo
            }
            builder.setPositiveButton("Add") { dialog, id ->
                viewModel.addCity()
                Log.e(TAG, "onCreateDialog: ${cityinfo.cityName}", )
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