package com.example.weatherapp.Models

import android.opengl.Visibility
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

public class CityWheatherInfo() : Parcelable{
    var lowTemp: Float? = null
    var highTemp: Float? = null
    var pressure: Int? = null
    var visibility: Int? = null
    var humidity: Int? = null
    var windSpeed: Float? = null
    var windDegree: Float? = null
    var feelsLike: Float? = null
    var temp : Float? = null
    var cityName : String? = null
    var countryName : String? = null
    var desc : String? = null
    var icon : Int? = 0

    constructor(parcel: Parcel) : this() {
        lowTemp = parcel.readValue(Float::class.java.classLoader) as? Float
        highTemp = parcel.readValue(Float::class.java.classLoader) as? Float
        pressure = parcel.readValue(Int::class.java.classLoader) as? Int
        visibility = parcel.readValue(Int::class.java.classLoader) as? Int
        humidity = parcel.readValue(Int::class.java.classLoader) as? Int
        windSpeed = parcel.readValue(Float::class.java.classLoader) as? Float
        windDegree = parcel.readValue(Float::class.java.classLoader) as? Float
        feelsLike = parcel.readValue(Float::class.java.classLoader) as? Float
        temp = parcel.readValue(Float::class.java.classLoader) as? Float
        cityName = parcel.readString().toString()
        countryName = parcel.readString().toString()
        desc = parcel.readString().toString()
        icon = parcel.readInt()
    }

    override fun toString(): String {
        return "$\n${this.cityName},${this.countryName}: " +
                "\n Temp: ${this.temp}" +
                "\n description: ${this.desc}"+
                "\n Low Temp: ${this.lowTemp}, High Temp: ${this.highTemp}" +
                "\n Visibility : ${this.visibility}" +
                "\n Wind Speed : ${this.windSpeed}, Wind Degree: ${this.windDegree}" +
                "\n Feels Like : ${this.feelsLike}" +
                "\n Humidity : ${this.humidity}" +
                "\n Icon: ${this.icon}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(lowTemp)
        parcel.writeValue(highTemp)
        parcel.writeValue(pressure)
        parcel.writeValue(visibility)
        parcel.writeValue(humidity)
        parcel.writeValue(windSpeed)
        parcel.writeValue(windDegree)
        parcel.writeValue(feelsLike)
        parcel.writeValue(temp)
        parcel.writeString(cityName)
        parcel.writeString(countryName)
        parcel.writeString(desc)
        parcel.writeInt(icon!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CityWheatherInfo> {
        override fun createFromParcel(parcel: Parcel): CityWheatherInfo {
            return CityWheatherInfo(parcel)
        }

        override fun newArray(size: Int): Array<CityWheatherInfo?> {
            return arrayOfNulls(size)
        }
    }
}
