package ru.gb.weatherkotlin.repository

import ru.gb.weatherkotlin.model.WeatherDTO

interface DetailsRepository {

    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    )
}