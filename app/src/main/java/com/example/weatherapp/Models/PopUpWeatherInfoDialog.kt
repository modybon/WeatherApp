package com.example.weatherapp.Models

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.ViewModels.SearchPage
import com.example.weatherapp.ViewModels.SearchViewModel
import com.example.weatherapp.databinding.FragmentWeatherPageBinding
import com.example.weatherapp.managers.SharedPrefrencesManager
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
        //retainInstance = true
        return activity.let {
            //Log.e(TAG, "onCreateDialogTest: ${activity.viewModel.cityWheatherInfo}")
            val builder = AlertDialog.Builder(it)
            builder.setPositiveButton("Add") { dialog, id ->
                viewModel.addCity()
                SharedPrefrencesManager.writeCitiesList("Cities", viewModel.cityWheatherInfo.cityName)
                Log.e(TAG, "onCreateDialog: ${ viewModel.citiesWheatherList.value}")
                // TODO: TRY INTERFACE TO DO ADD THE CITYINFO
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") {dialog, id->
                viewModel.cityWheatherInfo = CityWheatherInfo()
                dialog.dismiss()
            }
            this.binding = FragmentWeatherPageBinding.inflate(layoutInflater)
            builder.setView(this.binding.root)
            this.binding.cityNameTv.setText(viewModel.cityWheatherInfo.cityName)
            this.binding.tempTv.setText(String.format("%.0f",viewModel.cityWheatherInfo.temp))
            this.binding.feelsLikeTextView.setText(String.format("%.0f",viewModel.cityWheatherInfo.feelsLike) + '°')
            this.binding.highTempTv.setText("H:${String.format("%.0f",viewModel.cityWheatherInfo.highTemp) + '°'}")
            this.binding.lowTempTv.setText("L:${String.format("%.0f",viewModel.cityWheatherInfo.lowTemp) + '°'}")
            this.binding.humidityTv.setText(viewModel.cityWheatherInfo.humidity.toString() + '%')
            this.binding.pressureTextView.setText(viewModel.cityWheatherInfo.pressure.toString() + "hPa")
            this.binding.visibilityTv.setText(viewModel.cityWheatherInfo.visibility.toString() + "Km")
            this.binding.windTextView.setText(viewModel.cityWheatherInfo.windSpeed.toString())
            this.binding.descTv.setText(viewModel.cityWheatherInfo.desc)
            this.binding.icon.setBackgroundResource(viewModel.cityWheatherInfo.icon!!)
            this.binding.backGroundLayout.setBackgroundColor(Color.parseColor(viewModel.cityWheatherInfo.backgroundColor ?: "#696969"))
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}