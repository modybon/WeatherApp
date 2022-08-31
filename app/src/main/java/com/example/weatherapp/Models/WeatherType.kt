package com.example.weatherapp.Models

import androidx.annotation.DrawableRes

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

sealed class WeatherType(
    @DrawableRes val iconRes : Int,
    val backgroundColor : String
)  {
    var orange = "#FFA500"
    val grey = "#696969"
    val cyan = "#00FFFF"
    val cloudy_cyan = "#bbcdd4"
    val light_orange = "#FFD68A"
    val black = "#FF000000"
    val dark_blue = "#338BA8"
    val blue = "#60BEEB"
    val thunderStromNight = "#9098a1"
    val diamond = "#C0F6FB"
    object clearSkyDay : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.oned,
        backgroundColor = "#FFA500"
    )
    object clearSkyNight : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.onen,
        backgroundColor = "#696969"
    )
    object fewCloudsDay : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.twod,
        backgroundColor = "#00FFFF"
    )
    object fewCloudsNight : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.twon,
        backgroundColor = "#696969"
    )
    object scatteredCloudsDay : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.threed,
        backgroundColor = "#bbcdd4"
    )
    object scatteredCloudsNight : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.oned,
        backgroundColor = "#bbcdd4"
    )
    object brokenCloudsDay : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.fourd,
        backgroundColor ="#FFD68A"
    )
    object brokenCloudsNight : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.fourn,
        backgroundColor = "#FF000000"
    )
    object showerRainDay : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.nined,
        backgroundColor = "#338BA8"
    )
    object showerRainNight : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.ninen,
        backgroundColor ="#60BEEB"
    )
    object rainDay : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.tend,
        backgroundColor = "#FFD68A"
    )
    object rainNight : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.tenn,
        backgroundColor = "#bbcdd4"
    )
    object thunderstormnDay : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.elevend,
        backgroundColor = "#CCD6E2"
    )
    object thunderstormNight : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.elevenn,
        backgroundColor = "#9098a1"
    )
    object snownDay : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.thirteend,
        backgroundColor ="#C0F6FB"
    )
    object snowNight : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.thirteenn,
        backgroundColor = "#bbcdd4"
    )
    object mistDay : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.fiftyd,
        backgroundColor = "#C0F6FB"
    )
    object mistNight : WeatherType(
        iconRes = com.example.weatherapp.R.drawable.fiftyn,
        backgroundColor = "#bbcdd4"
    )
}