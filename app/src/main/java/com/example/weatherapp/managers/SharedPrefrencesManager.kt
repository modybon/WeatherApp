package com.example.weatherapp.managers

import android.content.Context
import android.content.SharedPreferences

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

object SharedPrefrencesManager {
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
    fun readCities(key: String?): Set<String>{
        with(sharedPreferences){
            if (this!!.contains(key)){
                val l = sharedPreferences!!.getStringSet(key,null)!!.toSet()
                return sharedPreferences!!.getStringSet(key,null)!!.toSet()
            }
            return setOf()
        }
    }

    fun writeCurrentCity(key: String?, value: String?){
        apply { sharedPreferences!!.edit().putString(key,value).apply() }
    }

}