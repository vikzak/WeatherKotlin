package ru.gb.weatherkotlin.repository



import retrofit2.Callback
import ru.gb.weatherkotlin.model.WeatherDTO

class DetailsRepository(private val remoteDataSource: RemoteDataSource): DetailsRepositoryImpl {
    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }

}