package ru.gb.weatherkotlin.repository

import ru.gb.weatherkotlin.model.Weather
import ru.gb.weatherkotlin.room.HistoryDao
import ru.gb.weatherkotlin.room.LocalRepository
import ru.gb.weatherkotlin.utils.convertHistoryEntityToWeather
import ru.gb.weatherkotlin.utils.convertWeatherToEntity

class LocalRepositoryImpl(
    private val localDataSource: HistoryDao
) : LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }
}