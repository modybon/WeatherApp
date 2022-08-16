package com.example.weatherapp.Models

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.ViewModels.ViewModel
import com.example.weatherapp.databinding.MiniWheatherInfoLayoutBinding
import java.lang.Exception
import java.net.URL

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

class RecyclerViewAdapter(
    private val context: Context,
    private val viewModel: ViewModel,
    private val clickListener: WeatherInfoDialogInterface
    ): RecyclerView.Adapter<RecyclerViewAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(var binding: MiniWheatherInfoLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        private val TAG = this@WeatherViewHolder.toString()
        fun bind(currentWeatherInfo: CityWheatherInfo, clickListener: WeatherInfoDialogInterface){
            if(currentWeatherInfo.temp == null){
                this.binding.cityNameTv.setText(currentWeatherInfo.cityName)
            }else{
                this.binding.cityNameTv.setText(currentWeatherInfo.cityName)
                this.binding.countryNameTv.setText(currentWeatherInfo.countryName)
                this.binding.tempTv.setText( String.format("%.0f",currentWeatherInfo.temp) + "°")
                this.binding.extraInfoTv.setText("H:${String.format("%.0f",currentWeatherInfo.highTemp)}°"+" L:${String.format("%.0f",currentWeatherInfo.lowTemp)}°")
                this.binding.descTv.setText(currentWeatherInfo.desc)
                this.binding.wheatherIcon.setBackgroundResource(currentWeatherInfo.icon!!)
            }
            itemView.setOnClickListener{
                clickListener.onClickListener(currentWeatherInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(MiniWheatherInfoLayoutBinding.inflate(LayoutInflater.from(this.context),parent,false))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(this.viewModel.list?.get(position),this.clickListener)
    }

    override fun getItemCount(): Int {
        return this.viewModel.list?.size
    }

}