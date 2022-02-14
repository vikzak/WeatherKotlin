package ru.gb.weatherkotlin.view.details


import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_details.*
import okhttp3.*
import ru.gb.weatherkotlin.BuildConfig
import ru.gb.weatherkotlin.R
import ru.gb.weatherkotlin.databinding.FragmentDetailsBinding
import ru.gb.weatherkotlin.model.Weather
import ru.gb.weatherkotlin.model.WeatherDTO
import ru.gb.weatherkotlin.utils.showSnackBar
import ru.gb.weatherkotlin.viewmodel.AppState
import ru.gb.weatherkotlin.viewmodel.DetailsViewModel
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
private const val REQUEST_API_KEY = "X-Yandex-API-Key"
private const val MAIN_LINK = "https://api.weather.yandex.ru/v2/forecast/?"



class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private lateinit var mainView: ConstraintLayout
    private lateinit var city: TextView
    private lateinit var coordinates: TextView
    private lateinit var temperature: TextView
    private lateinit var feelsLike: TextView
    private val viewModel: DetailsViewModel by lazy { ViewModelProvider(this)[DetailsViewModel::class.java] }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
        //return inflater.inflate(R.layout.fragment_details, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable<Weather>(BUNDLE_EXTRA) ?: Weather()
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        viewModel.detailsLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        getWeather()
    }

    private fun getWeather() {
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        viewModel.getWeatherFromRemoteSource(weatherBundle.city.lat, weatherBundle.city.lon)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                setWeather(appState.weatherData[0])
            }
            is AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                binding.mainView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getWeatherFromRemoteSource(weatherBundle.city.lat, weatherBundle.city.lon) }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun findsViews() {
        mainView = binding.mainView
        city = binding.cityName
        coordinates = binding.cityCoordinates
        temperature = binding.temperatureValue
        feelsLike = binding.feelsLikeValue
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }







//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
//        binding.mainView.visibility = View.GONE
//        binding.loadingLayout.visibility = View.VISIBLE
//        //viewModel.detailsLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
//        getWeather()
//
////        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
////        getWeather()
//    }





    //    private fun renderData(appState: AppState) {//weatherDTO: WeatherDTO
//        when (appState) {
//            is AppState.Success -> {
//                binding.mainView.visibility = View.VISIBLE
//                binding.loadingLayout.visibility = View.GONE
//                setWeather(appState.weatherData[0])
//            }
//            is AppState.Loading -> {
//                binding.mainView.visibility = View.GONE
//                binding.loadingLayout.visibility = View.VISIBLE
//            }
//            is AppState.Error -> {
//                binding.mainView.visibility = View.VISIBLE
//                binding.loadingLayout.visibility = View.GONE
//                binding.mainView.showSnackBar(
//                    getString(R.string.error),
//                    getString(R.string.reload),
//                    { getWeather() })
//            }
//        }
//
////        binding.mainView.visibility = View.VISIBLE
////        binding.loadingLayout.visibility = View.GONE
////        val fact = weatherDTO.fact
////        if (fact == null || fact.temp == null || fact.feels_like == null || fact.condition.isNullOrEmpty()) {
////            TODO(PROCESS_ERROR) }
////        else {
////            val city = weatherBundle.city
////            binding.cityName.text = city.city
////            binding.cityCoordinates.text = String.format(
////                getString(R.string.city_coordinates),
////                city.lat.toString(),
////                city.lon.toString()
////            )
////            binding.temperatureValue.text = fact.temp.toString()
////            binding.feelsLikeValue.text = fact.feels_like.toString()
////            binding.weatherCondition.text = fact.condition.toString()
////            //binding.weatherCondition.text = tranclate(fact.condition)
////        }
//    }



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


    private fun onError() {
        Snackbar
            .make(binding.temperatureLabel, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.reload)) { getWeather() }
            .show()
    }


    private fun setWeather(weather: Weather) {
        val city = weatherBundle.city
        binding.cityName.text = city.city
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            city.lat.toString(),
            city.lon.toString()
        )
        binding.temperatureValue.text = weather.temperature.toString()
        binding.feelsLikeValue.text = weather.feelsLike.toString()
        binding.weatherCondition.text = weather.condition

        Glide.with(requireContext())
            .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
            .circleCrop()
            .into(binding.ivPicture)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        _binding = null
//    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle) = DetailsFragment().also {
                fragment -> fragment.arguments = bundle
        }
    }

}