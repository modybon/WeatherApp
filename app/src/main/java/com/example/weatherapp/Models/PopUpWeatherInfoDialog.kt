package com.example.weatherapp.Models

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.weatherapp.ViewModels.SearchPage
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
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //retainInstance = true
        return activity.let {
            var activity = getActivity() as SearchPage
            activity.searchViewModel.cityWheatherInfo = activity.cityInfo
            //Log.e(TAG, "onCreateDialogTest: ${activity.viewModel.cityWheatherInfo}")
            val builder = AlertDialog.Builder(it)
            builder.setPositiveButton("Add") { dialog, id ->
                activity.searchViewModel.addCity()
                SharedPrefrencesManager.writeCitiesList("Cities", activity.cityInfo.cityName)
                Log.e(TAG, "onCreateDialog: ${activity.searchViewModel.citiesWheatherList.value}")
                // TODO: TRY INTERFACE TO DO ADD THE CITYINFO
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") {dialog, id->
                activity.searchViewModel.cityWheatherInfo = CityWheatherInfo()
                activity.cityInfo = CityWheatherInfo()
                dialog.dismiss()
            }
            this.binding = FragmentWeatherPageBinding.inflate(layoutInflater)
            builder.setView(this.binding.root)
            this.binding.cityNameTv.setText(activity.cityInfo.cityName)
            this.binding.tempTv.setText(String.format("%.0f",activity.cityInfo.temp))
            this.binding.feelsLikeTextView.setText(String.format("%.0f",activity.cityInfo.feelsLike) + '°')
            this.binding.highTempTv.setText("H:${String.format("%.0f",activity.cityInfo.highTemp) + '°'}")
            this.binding.lowTempTv.setText("L:${String.format("%.0f",activity.cityInfo.lowTemp) + '°'}")
            this.binding.humidityTv.setText(activity.cityInfo.humidity.toString() + '%')
            this.binding.pressureTextView.setText(activity.cityInfo.pressure.toString() + "hPa")
            this.binding.visibilityTv.setText(activity.cityInfo.visibility.toString() + "Km")
            this.binding.windTextView.setText(activity.cityInfo.windSpeed.toString())
            this.binding.descTv.setText(activity.cityInfo.desc)
            this.binding.icon.setBackgroundResource(activity.cityInfo.icon!!)
            this.binding.backGroundLayout.setBackgroundColor(Color.parseColor(activity.cityInfo.backgroundColor ?: "#696969"))
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}