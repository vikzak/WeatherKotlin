package ru.gb.weatherkotlin.repository

import ru.gb.weatherkotlin.model.Weather
import ru.gb.weatherkotlin.model.getRussianCities
import ru.gb.weatherkotlin.model.getWorldCities

interface Repository {

    fun getWeatherFromServer(): Weather

    fun getWeatherFromLocalStorageRus(): List<Weather>

    fun getWeatherFromLocalStorageWorld(): List<Weather>
}
