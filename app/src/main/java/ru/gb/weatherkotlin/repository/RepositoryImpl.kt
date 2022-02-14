package ru.gb.weatherkotlin.repository

import ru.gb.weatherkotlin.model.Weather

//class RepositoryImpl : Repository {
//    override fun getWeatherFromServer() = Weather()
//
//    override fun getWeatherFromLocalStorageRus() = getRussianCities()
//
//    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
//
//}
interface RepositoryImpl{
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>

}