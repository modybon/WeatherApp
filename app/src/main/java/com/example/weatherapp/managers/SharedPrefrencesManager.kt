package com.example.weatherapp.managers

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

object SharedPrefrencesManager {
    private val TAG = this@SharedPrefrencesManager.toString()
    private var sharedPreferences : SharedPreferences? = null

    fun init(context: Context) {
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(context.packageName,Context.MODE_PRIVATE)
        }
    }

    fun addCityToCitiesList(key : String?, value: String?){
        val setOfCitiesNames : Set<String> = readCities(key)
        apply { sharedPreferences!!.edit().putStringSet(key,setOfCitiesNames + value).apply() }
    }

    fun readCities(key: String?): MutableSet<String>{
        with(sharedPreferences){
            if (this!!.contains(key)){
                val l = sharedPreferences!!.getStringSet(key,null)!!.toSet()
                return sharedPreferences!!.getStringSet(key,null)!!.toMutableSet()
            }
            return mutableSetOf<String>()
        }
    }

    fun removeCity(key: String?, deletedCity:String){
        var newSet : MutableSet<String> = mutableSetOf()
        this.readCities(key).forEach {
            city -> if(city == deletedCity){
            //this.readCities("Cities").remove(city)
        }else{
            newSet.add(city)
        }
        }
        apply { sharedPreferences?.edit()?.putStringSet("Cities",newSet)?.apply() }
    }

    fun writeCurrentCity(key: String?, value: String?){
        apply { sharedPreferences!!.edit().putString(key,value).apply()}
    }

    fun readCurrentCity(key: String?): String{
        with(sharedPreferences){
            if(this?.contains(key) == true){
                return sharedPreferences?.getString(key,null).toString()
            }
            return ""
        }
    }

}