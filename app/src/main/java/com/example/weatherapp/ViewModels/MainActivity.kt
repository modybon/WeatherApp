package com.example.weatherapp.ViewModels

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.example.weatherapp.Models.CityWheatherInfo
import com.example.weatherapp.Models.ViewPagerAdapter
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(){
    private val TAG = this@MainActivity.toString()
    private lateinit var viewModel : ViewModel
    lateinit var binding : ActivityMainBinding
    private lateinit var data : ArrayList<CityWheatherInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        val intentExtras = this.intent.extras
        intentExtras as Bundle
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intentExtras.getParcelableArrayList<Parcelable>("CitiesList").toString()
        var itemPosition = intentExtras.getInt("ItemSelectedPosition")
        //list.getBundleExtra("CitiesList")
        //Log.e(TAG, "TEST 1 onCreate: ${data.getParcelableArrayList<Parcelable>("CitiesList").toString()}", )
        //var l = intentExtras as JSONObject
        //Log.e(TAG, "TEST onCreate: ${l.getString("") }", )
        //val l = intentExtras.getBundle("CitiesList")
        //var i = intentExtras.getBundle("CitiesList")?.getParcelableArrayList<Parcelable>("CitiesList")
        //Log.e(TAG, "TEST onCreate: ${intentExtras.getBundle("CitiesList")?.getParcelableArrayList<Parcelable>("CitiesList")?.toMutableList().toString()}")
        //Log.e(TAG, "TEST 2 onCreate: ${intentExtras.get("CitiesList")}")
        data = intentExtras.get("CitiesList") as ArrayList<CityWheatherInfo>
        Log.e(TAG, "onCreate MainActivity: ${data[0]}", )
        //Log.e(TAG, "TEST 3 onCreate: ${i}")
        // Bundle[{CitiesList=Bundle[{List=[$
        this.binding.searchPageBtn.setOnClickListener{
            Log.e(TAG, "onCreate: Search Page ", )
            val intent = Intent(this,SearchPage::class.java)
            startActivity(intent)
        }
        viewModel = ViewModel(this.application)
        super.onCreate(savedInstanceState)

        this.binding.viewpager.adapter = ViewPagerAdapter(this,this.data)
        TabLayoutMediator(this.binding.tabLayout,this.binding.viewpager){ tab, position ->
            // tab is the for the tabs below
            // positions is referring to the position of the view pager
            Log.e(TAG, "Main onCreate: TAB $position", )
            this.binding.viewpager.setCurrentItem(itemPosition,false)
            tab.icon = resources.getDrawable(R.drawable.tab_selector)
        }.attach()
    }

}
