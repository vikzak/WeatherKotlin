package ru.gb.weatherkotlin.view.details

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import ru.gb.weatherkotlin.R
import ru.gb.weatherkotlin.databinding.FragmentDetailsBinding
import ru.gb.weatherkotlin.model.Weather
import ru.gb.weatherkotlin.model.WeatherDTO
import ru.gb.weatherkotlin.model.WeatherLoader
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL

import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

// ключ преподаваталя
//private const val YOUR_API_KEY = "1ad5da43-670b-4f43-8117-1e40442988e4"

//private const val YOUR_API_KEY = "6d3b3b7f-7a85-48f8-877d-bcacc1d44c6d"

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private val onLoadListener: WeatherLoader.WeatherLoaderListener =
        object : WeatherLoader.WeatherLoaderListener {
            override fun onLoaded(weatherDTO: WeatherDTO) {
                displayWeather(weatherDTO)
            }

            override fun onFailed(throwable: Throwable) { //Обработка ошибки
            }
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()

        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE

        val loader = WeatherLoader(
            onLoadListener,
            weatherBundle.city.lat,
            weatherBundle.city.lon
        )
        loader.loadWeather()
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
            when(weatherDTO.fact?.condition){
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



    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}