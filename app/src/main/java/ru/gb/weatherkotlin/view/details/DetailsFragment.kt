    package ru.gb.weatherkotlin.view.details

import android.os.Bundle
import android.os.Handler


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson


import kotlinx.android.synthetic.main.fragment_details.*
import okhttp3.*


import ru.gb.weatherkotlin.*
import ru.gb.weatherkotlin.databinding.FragmentDetailsBinding
import ru.gb.weatherkotlin.model.Weather
import ru.gb.weatherkotlin.model.WeatherDTO
import ru.gb.weatherkotlin.utils.showSnackBar
import ru.gb.weatherkotlin.viewmodel.AppState
import ru.gb.weatherkotlin.viewmodel.DetailsViewModel
import java.io.IOException


//const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
//const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
//const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
//const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
//const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
//const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
//const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
//const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
//const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
//const val DETAILS_TEMP_EXTRA = "TEMPERATURE"
//const val DETAILS_FEELS_LIKE_EXTRA = "FEELS LIKE"
//const val DETAILS_CONDITION_EXTRA = "CONDITION"
//private const val TEMP_INVALID = -100
//private const val FEELS_LIKE_INVALID = -100

private const val PROCESS_ERROR = "Обработка ошибки"
private const val MAIN_LINK = "https://api.weather.yandex.ru/v2/forecast?"
private const val REQUEST_API_KEY = "X-Yandex-API-Key"


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java) }


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


    private fun getWeather() {
        //binding.mainView.visibility = View.GONE
        //binding.loadingLayout.visibility = View.VISIBLE
        //viewModel.getWeatherFromRemoteSource(weatherBundle.city.lat,weatherBundle.city.lon)

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
private fun renderData(weatherDTO: WeatherDTO) {

    //binding.mainView.visibility = View.VISIBLE
    //binding.loadingLayout.visibility = View.GONE

    val fact = weatherDTO.factical
    if (fact == null || fact.temp == null || fact.feels_like == null || fact.condition.isNullOrEmpty()) {
        TODO(PROCESS_ERROR)
    } else {
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
    } }


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


    private fun onError(){
        Snackbar
            .make(binding.temperatureLabel, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.reload)) {getWeather() }
            .show()
    }


    private fun setWeather(weather: Weather) {
        val city = weatherBundle.city
        binding.cityName.text = city.city
        binding.cityCoordinates.text = "${getString(R.string.city_coordinates)} ${city.lat} ${city.lon}"
        binding.temperatureValue.text = weather.temperature.toString()
        binding.feelsLikeValue.text = weather.feelsLike.toString()
        binding.weatherCondition.text = weather.condition
        Glide.with(requireContext())
            .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
            .into(binding.ivPicture)

//        weather.icon?.let {
//            GlideToVectorYou.justLoadImage(
//                activity,
//                Uri.parse("https://yastatic.net/weather/i/icons/blueye/color/svg/${it}.svg"),
//                weatherIcon
//            )
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
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