package ru.gb.weatherkotlin.repository

import ru.gb.weatherkotlin.model.Weather
import ru.gb.weatherkotlin.model.getRussianCities
import ru.gb.weatherkotlin.model.getWorldCities

class Repository(): RepositoryImpl {
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }
    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getRussianCities()
    }
    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getWorldCities()
    }
}
