package com.example.weatherapp.ViewModels

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.weatherapp.Models.CityWheatherInfo
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherPageBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WeatherPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherPage(private val cityWheatherInfo: CityWheatherInfo) : Fragment() {
    private val TAG = this@WeatherPage.toString()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentWeatherPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // the same Fragment instance will be used after the orientation change
        // IN GENERAL RETAINING INSTANCE IS BAD BECAUSE YOU WONT BE ABLE TO RELOAD RESOURCES
        retainInstance = true
        // Inflate the layout for this fragment
        binding = FragmentWeatherPageBinding.inflate(layoutInflater,container,false)
        //return inflater.inflate(R.layout.fragment_weather_page, container, false)
        this.binding.cityNameTv.setText(this.cityWheatherInfo.cityName)
        this.binding.tempTv.setText(String.format("%.0f",this.cityWheatherInfo.temp))
        this.binding.feelsLikeTextView.setText(String.format("%.0f",this.cityWheatherInfo.feelsLike) + '°')
        this.binding.highTempTv.setText("H:${String.format("%.0f",this.cityWheatherInfo.highTemp) +'°'}")
        this.binding.lowTempTv.setText("L:${String.format("%.0f",this.cityWheatherInfo.lowTemp) + '°'}")
        this.binding.humidityTv.setText(this.cityWheatherInfo.humidity.toString() + '%')
        this.binding.pressureTextView.setText(this.cityWheatherInfo.pressure.toString() + " hPa")
        this.binding.visibilityTv.setText(this.cityWheatherInfo.visibility.toString() + " Km")
        this.binding.windTextView.setText(this.cityWheatherInfo.windSpeed.toString())
        this.binding.descTv.setText(this.cityWheatherInfo.desc)
        this.binding.icon.setBackgroundResource(this.cityWheatherInfo.icon!!)
        //this.binding.backGroundLayout.setBackgroundColor(Color.parseColor("#FFA500"))
        this.binding.backGroundLayout.setBackgroundColor(Color.parseColor(this.cityWheatherInfo.backgroundColor ?: "#696969"))
        //cardView.setCardBackgroundColor(ContextCompat.getColor(requireActivity().baseContext, R.color.your_color_name));
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // To save state of the fragment where you can place data so that when you change orientaion
        // You wouldn't lose data.
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: ${cityWheatherInfo.cityName}", )
    }


//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment WeatherPage.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            WeatherPage().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}

//        var url = URL("https://openweathermap.org/img/wn/${this.cityWheatherInfo.icon}@2x.png")
//        try {
//            Glide.with(this.requireActivity().baseContext).load(url).into(this.binding.icon)
//        }catch (e: Exception){
//            Log.e(TAG, "onCreateDialog: Eror Loading Image", )
//        }