package com.example.weatherapp.Models

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherapp.ViewModels.MainViewModel
import com.example.weatherapp.ViewModels.WeatherPage

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

class ViewPagerAdapter(activity: AppCompatActivity, var viewModel: MainViewModel): FragmentStateAdapter(activity) {
    var TAG = this@ViewPagerAdapter.toString()
    override fun getItemCount(): Int {
        return viewModel.citiesList.size
    }

    override fun createFragment(position: Int): Fragment {
        viewModel.selectedCityInfo(position)
        return WeatherPage()
    }

}