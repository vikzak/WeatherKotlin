package ru.gb.weatherkotlin.view.details

import android.os.Bundle
import android.os.Handler


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson


import kotlinx.android.synthetic.main.fragment_details.*
import okhttp3.*


import ru.gb.weatherkotlin.*
import ru.gb.weatherkotlin.databinding.FragmentDetailsBinding
import ru.gb.weatherkotlin.model.Weather
import ru.gb.weatherkotlin.model.WeatherDTO
import java.io.IOException


const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_TEMP_EXTRA = "TEMPERATURE"
const val DETAILS_FEELS_LIKE_EXTRA = "FEELS LIKE"
const val DETAILS_CONDITION_EXTRA = "CONDITION"
private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100

private const val PROCESS_ERROR = "Обработка ошибки"
private const val MAIN_LINK = "https://api.weather.yandex.ru/v2/forecast?"
private const val REQUEST_API_KEY = "X-Yandex-API-Key"


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

//    private val loadResultsReceiver: BroadcastReceiver = object :
//        BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
//                DETAILS_INTENT_EMPTY_EXTRA -> onErrorMessageSnackbar()
//                DETAILS_DATA_EMPTY_EXTRA -> onErrorMessageSnackbar()
//                DETAILS_RESPONSE_EMPTY_EXTRA -> onErrorMessageSnackbar()
//                DETAILS_REQUEST_ERROR_EXTRA -> onErrorMessageSnackbar()
//                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> onErrorMessageSnackbar()
//                DETAILS_URL_MALFORMED_EXTRA -> onErrorMessageSnackbar()
//                DETAILS_RESPONSE_SUCCESS_EXTRA -> renderData(
//                    WeatherDTO(
//                        FactDTO(
//                            intent.getIntExtra(DETAILS_TEMP_EXTRA, TEMP_INVALID),
//                            intent.getIntExtra(DETAILS_FEELS_LIKE_EXTRA, FEELS_LIKE_INVALID),
//                            intent.getStringExtra(DETAILS_CONDITION_EXTRA)
//                        )
//                    )
//                )
//                else
//                -> onErrorMessageSnackbar()
//            }
//        }
//    }



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
////        context?.let {
////            LocalBroadcastManager.getInstance(it)
////                .registerReceiver(
////                    loadResultsReceiver,
////                    IntentFilter(DETAILS_INTENT_FILTER)
////                )
////        }
//    }

    override fun onDestroy() {
//        context?.let {
//            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
//        }
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        getWeather()
    }

//    private fun getWeather() {
//        binding.mainView.visibility = View.GONE
//        binding.loadingLayout.visibility = View.VISIBLE
//        context?.let {
//            it.startService(Intent(it, DetailsService::class.java).apply {
//                putExtra(LATITUDE_EXTRA, weatherBundle.city.lat)
//                putExtra(LONGITUDE_EXTRA, weatherBundle.city.lon)
//            })
//        }
//    }

    private fun getWeather() {
        mainView.visibility = View.GONE
        loadingLayout.visibility = View.VISIBLE

        val client = OkHttpClient() // Клиент
        val builder: Request.Builder = Request.Builder() // Создаём строителя запроса

        builder.header(REQUEST_API_KEY, BuildConfig.MY_WEATHER_API_KEY) // Создаём заголовок запроса
        builder.url(MAIN_LINK + "lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}") // Формируем URL

        val request: Request = builder.build() // Создаём запрос
        val call: Call = client.newCall(request) // Ставим запрос в очередь и отправляем

        call.enqueue(object : Callback {
            val handler: Handler = Handler()
            // Вызывается, если ответ от сервера пришёл
            @Throws(IOException::class)
            override fun onResponse(call: Call?, response: Response) {
                val serverResponce: String? = response.body()?.string() // Синхронизируем поток с потоком UI
                if (response.isSuccessful && serverResponce != null) {
                    handler.post {
                        renderData(Gson().fromJson(serverResponce, WeatherDTO::class.java))
                    }
                } else {
                    response.code()
                }
            }// Вызывается при сбое в процессе запроса на сервер
            override fun onFailure(call: Call?, e: IOException?) {
                TODO(PROCESS_ERROR)
            }
        })
    }




//    private fun renderData(weatherDTO: WeatherDTO) {
//        binding.mainView.visibility = View.VISIBLE
//        binding.loadingLayout.visibility = View.GONE
//        val fact = weatherDTO.fact
//        val temp = fact!!.temp
//        val feelsLike = fact.feels_like
//        val condition = fact.condition
//        if (temp == TEMP_INVALID || feelsLike == FEELS_LIKE_INVALID || condition
//            == null
//        ) {
//            onErrorMessageSnackbar()
//        } else {
//            val city = weatherBundle.city
//            binding.cityName.text = city.city
//            binding.cityCoordinates.text = String.format(
//                getString(R.string.city_coordinates),
//                city.lat.toString(),
//                city.lon.toString()
//            )
//            binding.temperatureValue.text = temp.toString()
//
//            binding.feelsLikeValue.text = feelsLike.toString()
//            binding.weatherCondition.text = tranclate(condition)
//            //binding.weatherCondition.text = condition.toString()
//        }
//    }

    private fun renderData(weatherDTO: WeatherDTO) {
        binding.mainView.visibility = View.VISIBLE
        binding.loadingLayout.visibility = View.GONE
        val fact = weatherDTO.fact
        if (fact == null || fact.temp == null || fact.feels_like == null || fact.condition.isNullOrEmpty()) {
            TODO(PROCESS_ERROR) }
        else {
            val city = weatherBundle.city
            binding.cityName.text = city.city
            binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            binding.temperatureValue.text = fact.temp.toString()
            binding.feelsLikeValue.text = fact.feels_like.toString()
            binding.weatherCondition.text = fact.condition.toString()
            //binding.weatherCondition.text = tranclate(fact.condition)
        }
    }


    private fun tranclate(condition: String): String {
        var typeTemp = "не определено"
        when (condition) {
            "clear" -> typeTemp = "Ясно"
            "partly-cloudy" -> typeTemp = "Переменная облачность"
            "cloudy" -> typeTemp = "Пасмурно"
            "overcast" -> typeTemp = "Пасмурная погода"
            "drizzle" -> typeTemp = "Накрапываеть"
            "light-rain" -> typeTemp = "Легкий дождь"
            "rain" -> typeTemp = "Дождь"
            "moderate-rain" -> typeTemp = "Умеренный дождь"
            "heavy-rain" -> typeTemp = "Ливень"
            "continuous-heavy-rain" -> typeTemp = "Непрерывный сильный дождь"
            "showers" -> typeTemp = "Ливень"
            "wet-snow" -> typeTemp = "Мокрый снег"
            "light-snow" -> typeTemp = "Легкий снег"
            "snow" -> typeTemp = "Снег"
            "snow-showers" -> typeTemp = "Снегопад"
            "hail" -> typeTemp = "Град"
            "thunderstorm" -> typeTemp = "Гроза"
            "thunderstorm-with-rain" -> typeTemp = "Гроза-с-дождем"
            "thunderstorm-with-hail" -> typeTemp = "Гроза с градом"
        }
        return typeTemp
    }


    private fun onErrorMessageSnackbar() {
        Snackbar.make(
            binding.temperatureLabel, getString(R.string.error),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.reload)) {
                getWeather()
            }.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}