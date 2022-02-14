package ru.gb.weatherkotlin.model

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("fact")
    val fact: FactDTO?
)
