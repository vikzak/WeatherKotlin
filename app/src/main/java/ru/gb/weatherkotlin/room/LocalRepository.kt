package ru.gb.weatherkotlin.room

import ru.gb.weatherkotlin.model.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
}