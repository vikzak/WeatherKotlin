package ru.gb.weatherkotlin.repository

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gb.weatherkotlin.BuildConfig
import ru.gb.weatherkotlin.model.WeatherDTO
import java.io.IOException

private const val REQUEST_API_KEY = "X-Yandex-API-Key"

class RemoteDataSource {

//    fun getWeatherDetails(requestLink: String, callback: Callback) {
//        val builder: Request.Builder = Request.Builder().apply {
//            header(REQUEST_API_KEY, BuildConfig.MY_WEATHER_API_KEY)
//            url(requestLink)
//        }
//        OkHttpClient().newCall(builder.build()).enqueue(callback)
//    }

    private val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.weather.yandex.ru/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(createOkHttpClient(WeatherApiInterceptor()))
        .build()
        .create(WeatherAPI::class.java)

    fun getWeatherDetails(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {
        weatherApi.getWeather(lat, lon).enqueue(callback)
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        httpClient.addInterceptor(KeyInterceptor())
        return httpClient.build()
    }

    inner class WeatherApiInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }

    inner class KeyInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
                .newBuilder()
                .addHeader("X-Yandex-API-Key", BuildConfig.MY_WEATHER_API_KEY)
                .build()

            return chain.proceed(request)
        }
    }

    inner class KeyInterceptorQuery : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            val url = request.url().newBuilder()
                .addQueryParameter("X-Yandex-API-Key", BuildConfig.MY_WEATHER_API_KEY)
                .build()

            request = request.newBuilder().url(url).build()

            return chain.proceed(request)
        }
    }

}