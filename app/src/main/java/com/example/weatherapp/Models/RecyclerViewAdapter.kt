package com.example.weatherapp.Models

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.ViewModels.SearchViewModel
import com.example.weatherapp.databinding.MiniWheatherInfoLayoutBinding
import com.example.weatherapp.managers.SharedPrefrencesManager

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

class RecyclerViewAdapter(
    private val context: Context,
    private val searchViewModel: SearchViewModel,
    private val clickListener: WeatherInfoDialogInterface
    ): RecyclerView.Adapter<RecyclerViewAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(var binding: MiniWheatherInfoLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        private val TAG = this@WeatherViewHolder.toString()
        fun bind(currentWeatherInfo: CityWheatherInfo, clickListener: WeatherInfoDialogInterface, position: Int){
            var c : String = ""
            c.apply {
                if((currentWeatherInfo.cityName == SharedPrefrencesManager.readCurrentCity("currentCity"))){
                    c = "My Location"
                }else{
                    c = currentWeatherInfo.cityName.toString()
                }
            }
            if(currentWeatherInfo.temp == null){
                this.binding.cityNameTv.setText(c)
                this.binding.backGroundLayout.setBackgroundColor(R.color.blue!!)
                return
            }else{
                this.binding.cityNameTv.setText(c)
                this.binding.countryNameTv.setText(currentWeatherInfo.countryName)
                this.binding.tempTv.setText( String.format("%.0f",currentWeatherInfo.temp) + "°")
                this.binding.extraInfoTv.setText("H:${String.format("%.0f",currentWeatherInfo.highTemp)}°"+" L:${String.format("%.0f",currentWeatherInfo.lowTemp)}°")
                this.binding.descTv.setText(currentWeatherInfo.desc)
                this.binding.wheatherIcon.setBackgroundResource(currentWeatherInfo.icon!!)
                this.binding.backGroundLayout.setBackgroundColor(Color.parseColor(currentWeatherInfo.backgroundColor?:"#696969"))
            }
            itemView.setOnClickListener{
                clickListener.onClickListener(currentWeatherInfo,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(MiniWheatherInfoLayoutBinding.inflate(LayoutInflater.from(this.context),parent,false))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        this.searchViewModel.citiesWheatherList.value?.let { holder.bind(it.get(position),this.clickListener,position) }
    }

    override fun getItemCount(): Int {
        return this.searchViewModel.citiesWheatherList.value?.size ?: 0
    }

}