package ru.gb.weatherkotlin.repository


import ru.gb.weatherkotlin.model.WeatherDTO
import retrofit2.Callback

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource) : DetailsRepository {

    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }

}