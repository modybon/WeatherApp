package com.example.weatherapp.Models

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherapp.ViewModels.WeatherPage

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

class ViewPagerAdapter(activity: AppCompatActivity, var data: ArrayList<CityWheatherInfo>): FragmentStateAdapter(activity) {
    var TAG = this@ViewPagerAdapter.toString()

    override fun getItemCount(): Int {
        //return viewModel.citiesWheatherList.value!!.size
        //Log.e(TAG, "View Pager getItemCount: ${viewModel.citiesWheatherList?.value?.size}", )
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        //Log.e(TAG, "View Pager createFragment: ${viewModel.citiesWheatherList.value!!.get(position)}", )
        return WeatherPage(this.data.get(position))
    }

}