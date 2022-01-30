package ru.gb.weatherkotlin.model

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import ru.gb.weatherkotlin.BuildConfig
//import ru.gb.weatherkotlin.view.details.YOUR_API_KEY
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

private const val YOUR_API_KEY = "6d3b3b7f-7a85-48f8-877d-bcacc1d44c6d"

@RequiresApi(Build.VERSION_CODES.N)
class WeatherLoader (
    private val listener: WeatherLoaderListener,
    private val lat: Double, private val lon: Double){

    interface WeatherLoaderListener {
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFailed(throwable: Throwable)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather() {
        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
            val handler = Handler()
            Thread(Runnable {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.addRequestProperty(
                        "X-Yandex-API-Key",
                        BuildConfig.MY_WEATHER_API_KEY
                    )
                    urlConnection.readTimeout = 10000
                    val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    // преобразование
                    // ответа от сервера (JSON) в модель данных (WeatherDTO)
                    val weatherDTO: WeatherDTO = Gson().fromJson(
                        getLines(bufferedReader),
                        WeatherDTO::class.java
                    )
                    handler.post {listener.onLoaded(weatherDTO)}
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace() //Обработка ошибки
                } finally {
                    urlConnection.disconnect()
                }
            }).start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace() //Обработка ошибки
            listener.onFailed(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }
}

