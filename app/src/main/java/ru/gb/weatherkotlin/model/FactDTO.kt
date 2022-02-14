package ru.gb.weatherkotlin.model

import com.google.gson.annotations.SerializedName

data class FactDTO(
    val temp: Int?,
    @SerializedName("feels_like")
    val feelLike: Int?,
    val condition: String?,
    val icon: String?
)
