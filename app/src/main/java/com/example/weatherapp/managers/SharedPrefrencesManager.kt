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

    fun writeCitiesList(key : String?, value: String?){
        val setOfCitiesNames : Set<String> = readCities(key)
        apply { sharedPreferences!!.edit().putStringSet(key,setOfCitiesNames + value).apply() }
    }

    fun readCities(key: String?): MutableSet<String>{
        with(sharedPreferences){
            if (this!!.contains(key)){
                val l = sharedPreferences!!.getStringSet(key,null)!!.toSet()
                return sharedPreferences!!.getStringSet(key,null)!!
            }
            return mutableSetOf<String>()
        }
    }

    fun removeCity(key: String?,city:String){
        this.readCities(key).remove(city)
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