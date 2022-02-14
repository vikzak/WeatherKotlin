package ru.gb.weatherkotlin.repository

import ru.gb.weatherkotlin.model.Weather
import ru.gb.weatherkotlin.model.getRussianCities
import ru.gb.weatherkotlin.model.getWorldCities

//class RepositoryImpl : Repository {
//    override fun getWeatherFromServer() = Weather()
//
//    override fun getWeatherFromLocalStorageRus() = getRussianCities()
//
//    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
//
//}
class RepositoryImpl : Repository {
    override fun getWeatherFromServer() = Weather()
    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}