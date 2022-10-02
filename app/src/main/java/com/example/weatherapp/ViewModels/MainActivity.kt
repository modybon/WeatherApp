package com.example.weatherapp.ViewModels

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.Models.CityWheatherInfo
import com.example.weatherapp.Models.ViewPagerAdapter
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(){
    private val TAG = this@MainActivity.toString()
    private val viewModel: MainViewModel by viewModels()
    lateinit var binding : ActivityMainBinding
    private lateinit var data : ArrayList<CityWheatherInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentExtras = this.intent.extras
        intentExtras as Bundle
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //intentExtras.getParcelableArrayList<Parcelable>("CitiesList").toString()
        if(savedInstanceState != null){
            data = savedInstanceState.getParcelableArrayList("citiesWeatherInfo")?: arrayListOf()
            Log.e(TAG, "onCreate onSaved: $data")
        }else{
            data = intentExtras.get("CitiesList") as ArrayList<CityWheatherInfo>
        }
        var itemPosition = intentExtras.getInt("ItemSelectedPosition")
        Log.e(TAG, "onCreate data: $data", )
        Log.e(TAG, "onCreate position: $itemPosition", )
        viewModel.citiesList = data
        Log.e(TAG, "onCreate data2: ${viewModel.citiesList}", )
        this.binding.searchPageBtn.setOnClickListener{
            // SingleTask as launch mode will retreive the previous instance
            // of this activity since it is the root and will destroy all above activities
            // the activity starts in a new task tho
            //startActivity(intent)
            onBackPressed() // works well as well and it will remain in the same task
            //onBackPressed() // finishes current activty and goes back to the previous one
            // finish() closes the open activty calls onDestroy()
            //finishAffinity() // Deletes all activities below this one

        }
        this.binding.viewpager.adapter = ViewPagerAdapter(this,viewModel)
        TabLayoutMediator(this.binding.tabLayout,this.binding.viewpager){ tab, position ->
            // tab is the for the tabs below
            // positions is referring to the position of the view pager
            this.binding.viewpager.setCurrentItem(itemPosition,false)
            tab.icon = resources.getDrawable(R.drawable.tab_selector)
        }.attach()
    }
    override fun onDestroy() {
        Log.e(TAG, "onDestroy: Main Activity Destroyed")
        this.binding.searchPageBtn.setOnClickListener(null)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("citiesWeatherInfo",data)
    }

}
