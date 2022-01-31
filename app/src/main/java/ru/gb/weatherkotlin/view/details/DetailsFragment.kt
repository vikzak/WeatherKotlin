package ru.gb.weatherkotlin.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.gb.weatherkotlin.DetailsService
import ru.gb.weatherkotlin.LATITUDE_EXTRA
import ru.gb.weatherkotlin.LONGITUDE_EXTRA
import ru.gb.weatherkotlin.R
import ru.gb.weatherkotlin.databinding.FragmentDetailsBinding
import ru.gb.weatherkotlin.model.FactDTO
import ru.gb.weatherkotlin.model.Weather
import ru.gb.weatherkotlin.model.WeatherDTO

// ключ преподаваталя
//private const val YOUR_API_KEY = "1ad5da43-670b-4f43-8117-1e40442988e4"

//private const val YOUR_API_KEY = "6d3b3b7f-7a85-48f8-877d-bcacc1d44c6d"

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


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private val loadResultsReceiver: BroadcastReceiver = object :
        BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_SUCCESS_EXTRA -> renderData(
                    WeatherDTO(
                        FactDTO(
                            intent.getIntExtra(DETAILS_TEMP_EXTRA, TEMP_INVALID),
                            intent.getIntExtra(DETAILS_FEELS_LIKE_EXTRA, FEELS_LIKE_INVALID),
                                intent.getStringExtra(DETAILS_CONDITION_EXTRA)
                            )
                        )
                    )
                    else
                -> TODO(PROCESS_ERROR)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(
                    loadResultsReceiver,
                    IntentFilter(DETAILS_INTENT_FILTER)
                )
        }
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        getWeather()
    }

    private fun getWeather() {
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        context ?. let {
            it.startService(Intent(it, DetailsService::class.java).apply {
                putExtra(LATITUDE_EXTRA, weatherBundle.city.lat)
                putExtra(LONGITUDE_EXTRA, weatherBundle.city.lon)
            })
        }
    }

    private fun renderData(weatherDTO: WeatherDTO) {
        binding.mainView.visibility = View.VISIBLE
        binding . loadingLayout . visibility = View . GONE
        val fact = weatherDTO.fact
        val temp = fact!!.temp
        val feelsLike = fact.feels_like
        val condition = fact.condition
        if (temp == TEMP_INVALID || feelsLike == FEELS_LIKE_INVALID || condition
            == null
        ) {
            TODO(PROCESS_ERROR)
        } else {
            val city = weatherBundle.city
            binding.cityName.text = city.city
            binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            binding.temperatureValue.text = temp.toString()

            binding.feelsLikeValue.text = feelsLike.toString()
            binding.weatherCondition.text = tranclate(condition)
        }
    }


    private fun tranclate(condition: String): String {
        var typeTemp = "не определено"
        when (condition){
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




    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            val city = weatherBundle.city
            cityName.text = city.city
            cityCoordinates.text =
                String.format(
                    getString(R.string.city_coordinates),
                    city.lat.toString(),
                    city.lon.toString()
                )
            when (weatherDTO.fact?.condition) {
                "clear" -> weatherCondition.text = "Ясно"
                "partly-cloudy" -> weatherCondition.text = "Переменная облачность"
                "cloudy" -> weatherCondition.text = "Пасмурно"
                "overcast" -> weatherCondition.text = "Пасмурная погода"
                "drizzle" -> weatherCondition.text = "Накрапываеть"
                "light-rain" -> weatherCondition.text = "Легкий дождь"
                "rain" -> weatherCondition.text = "Дождь"
                "moderate-rain" -> weatherCondition.text = "Умеренный дождь"
                "heavy-rain" -> weatherCondition.text = "Ливень"
                "continuous-heavy-rain" -> weatherCondition.text = "Непрерывный сильный дождь"
                "showers" -> weatherCondition.text = "Ливень"
                "wet-snow" -> weatherCondition.text = "Мокрый снег"
                "light-snow" -> weatherCondition.text = "Легкий снег"
                "snow" -> weatherCondition.text = "Снег"
                "snow-showers" -> weatherCondition.text = "Снегопад"
                "hail" -> weatherCondition.text = "Град"
                "thunderstorm" -> weatherCondition.text = "Гроза"
                "thunderstorm-with-rain" -> weatherCondition.text = "Гроза-с-дождем"
                "thunderstorm-with-hail" -> weatherCondition.text = "Гроза с градом"
            }
            temperatureValue.text = weatherDTO.fact?.temp.toString()
            feelsLikeValue.text = weatherDTO.fact?.feels_like.toString()
        }
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