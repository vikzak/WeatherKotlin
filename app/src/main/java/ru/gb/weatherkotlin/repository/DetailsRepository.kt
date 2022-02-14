package ru.gb.weatherkotlin.repository



//--import retrofit2.Callback
import okhttp3.Callback
import ru.gb.weatherkotlin.model.WeatherDTO

//class DetailsRepository(private val remoteDataSource: RemoteDataSource): DetailsRepositoryImpl {
//    override fun getWeatherDetailsFromServer(
//        lat: Double,
//        lon: Double,
//        callback: Callback<WeatherDTO>
//    ) {
//        remoteDataSource.getWeatherDetails(lat, lon, callback)
//    }
//
//}
interface DetailsRepository {
    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    ) }