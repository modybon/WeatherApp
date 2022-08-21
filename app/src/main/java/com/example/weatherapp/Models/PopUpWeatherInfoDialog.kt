package com.example.weatherapp.Models

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.resources.R
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.weatherapp.ViewModels.ViewModel
import com.example.weatherapp.databinding.FragmentWeatherPageBinding
import com.example.weatherapp.managers.SharedPrefrencesManager
import java.lang.Exception
import java.lang.IllegalStateException
import java.net.URL

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

class PopUpWeatherInfoDialog(
    private val cityWheatherInfo: CityWheatherInfo,
    private val activity: AppCompatActivity,
    private val viewModel : ViewModel,
): DialogFragment() {
    private val TAG = this@PopUpWeatherInfoDialog.toString()
    private lateinit var binding : FragmentWeatherPageBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            var handler = Handler(Looper.getMainLooper())
            builder.setPositiveButton("Add",DialogInterface.OnClickListener{dialog,id->
                // TODO: ADD IT TO THE LIST
//                viewModel.list.add(this.wheatherInfo)
//                viewModel.wheatherList.postValue(viewModel.list)
                viewModel.addCity()
                SharedPrefrencesManager.writeCitiesList("Cities",cityWheatherInfo.cityName)
                //var lol = viewModel.citiesWheatherList.value as List<CityWheatherInfo>;
                //Log.e(TAG, "onCreateDialog: ${viewModel.list.size}", )
                //Log.e(TAG, "onCreateDialog: ${viewModel.citiesWheatherList.value}", )
                dialog.dismiss()
                //searchPage.list.postValue(this.wheatherInfo)
            })
            builder.setNegativeButton("Cancel",DialogInterface.OnClickListener{dialog,id->
                viewModel.cityWheatherInfo = CityWheatherInfo()
                dialog.dismiss()
            })
            this.binding = FragmentWeatherPageBinding.inflate(layoutInflater)
            builder.setView(this.binding.root)
//            var url = URL("https://openweathermap.org/img/wn/${this.cityWheatherInfo.icon}@2x.png")
//            try {
//                Glide.with(this.activity.baseContext).load(url).into(this.binding.icon)
//            }catch (e: Exception){
//                Log.e(TAG, "onCreateDialog: Eror Loading Image", )
//            }
            this.binding.cityNameTv.setText(this.cityWheatherInfo.cityName)
            this.binding.tempTv.setText(String.format("%.0f",this.cityWheatherInfo.temp))
            this.binding.feelsLikeTextView.setText(String.format("%.0f",this.cityWheatherInfo.feelsLike) + '°')
            this.binding.highTempTv.setText("H:${String.format("%.0f",this.cityWheatherInfo.highTemp) + '°'}")
            this.binding.lowTempTv.setText("L:${String.format("%.0f",this.cityWheatherInfo.lowTemp) + '°'}")
            this.binding.humidityTv.setText(this.cityWheatherInfo.humidity.toString() + '%')
            this.binding.pressureTextView.setText(this.cityWheatherInfo.pressure.toString() + "hPa")
            this.binding.visibilityTv.setText(this.cityWheatherInfo.visibility.toString() + "Km")
            this.binding.windTextView.setText(this.cityWheatherInfo.windSpeed.toString())
            this.binding.descTv.setText(this.cityWheatherInfo.desc)
            this.binding.icon.setBackgroundResource(this.cityWheatherInfo.icon!!)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}