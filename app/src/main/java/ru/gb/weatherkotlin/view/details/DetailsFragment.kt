package ru.gb.weatherkotlin.view.details

//import ru.gb.weatherkotlin.databinding.DetailsFragmentBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.gb.weatherkotlin.R
import ru.gb.weatherkotlin.databinding.FragmentDetailsBinding
import ru.gb.weatherkotlin.model.Weather
import ru.gb.weatherkotlin.viewmodel.MainViewModel

class DetailsFragment : Fragment() {

//    companion object {
//        fun newInstance() = DetailsFragment()
//    }

    private lateinit var viewModel: MainViewModel

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)
//        if (weather != null) {
//            val city = weather.city
//            binding.cityName.text = city.city
//            binding.cityCoordinates.text = String.format(
//                getString(R.string.city_coordinates),
//                city.lat.toString(),
//                city.lon.toString()
//            )
//            binding.temperatureValue.text = weather.temperature.toString()
//            binding.feelsLikeValue.text = weather.feelsLike.toString()
//        }
//    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let { weather ->
            weather.city.also { city ->
                binding.cityName.text = city.city
                binding.cityCoordinates.text = String.format(
                    getString(R.string.city_coordinates),
                    city.lat.toString(),
                    city.lon.toString()
                )
                binding.temperatureValue.text = weather.temperature.toString()
                binding.feelsLikeValue.text = weather.feelsLike.toString()
            }
        }
    }


//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
//        viewModel.getWeather()
//    }

    //    private fun renderData(appState: AppState) {
//        when(appState){
//            is AppState.Success ->{
//                val weatherData = appState.weatherData
//                binding.loadingLayout.visibility = View.GONE
//                setData(weatherData)
//                //Snackbar.make(binding.mainView, "Success", Snackbar.LENGTH_LONG).show()
//            }
//            is AppState.Loading ->{
//                binding.loadingLayout.visibility = View.VISIBLE
//            }
//            is AppState.Error -> {
//                binding.loadingLayout.visibility = View.GONE
//                Snackbar.make(binding.mainView, "Error", Snackbar.LENGTH_INDEFINITE)
//                    .setAction("Reload"){ viewModel.getWeather() }
//                    .show()
//            }
//        }
//    }

//    private fun setData(weatherData: Weather) {
//        binding.cityName.text = weatherData.city.city
//        binding.cityCoordinates.text = String.format(
//            getString(R.string.city_coordinates),
//            weatherData.city.lat.toString(),
//            weatherData.city.lon.toString()
//        )
//        binding.temperatureValue.text = weatherData.temperature.toString()
//        binding.feelsLikeValue.text = weatherData.feelsLike.toString()
//    }

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