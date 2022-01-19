package ru.gb.weatherkotlin.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import ru.gb.weatherkotlin.R
import ru.gb.weatherkotlin.databinding.FragmentMainBinding
import ru.gb.weatherkotlin.model.Weather
import ru.gb.weatherkotlin.view.details.DetailsFragment
import ru.gb.weatherkotlin.viewmodel.AppState
import ru.gb.weatherkotlin.viewmodel.MainViewModel
import kotlin.random.Random

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

//    private lateinit var viewModel: MainViewModel
//    private var isDataSetRus: Boolean = true
//    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
//        override fun onItemViewClick(weather: Weather) {
//        val manager = activity?.supportFragmentManager
//            if (manager != null) {
//            val bundle = Bundle()
//                bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
//                manager.beginTransaction()
//                    .replace(R.id.container, DetailsFragment.newInstance(bundle))
//                    .addToBackStack("")
//                    .commitAllowingStateLoss()
//        }
//        }
//    })

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var isDataSetRus: Boolean = true
    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                    }))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })


    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromLocalSourceRus()
    }

    //    private fun changeWeatherDataSet() {
//        if (isDataSetRus) {
//            viewModel.getWeatherFromLocalSourceWorld()
//            binding.mainFragmentFAB.setImageResource(R.drawable.ic_baseline_outlined_flag_24)
//        } else {
//            viewModel.getWeatherFromLocalSourceRus()
//            binding.mainFragmentFAB.setImageResource(R.drawable.ic_baseline_outlined_flag_24)
//        }
//        isDataSetRus = !isDataSetRus
//    }
    private fun changeWeatherDataSet() =
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_baseline_outlined_flag_24)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_baseline_outlined_flag_24)
        }.also { isDataSetRus = !isDataSetRus }

    private fun View.showSnackBar(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }

    private fun renderData(appState: AppState) {
        val temp = Random.nextBoolean()
        when (appState) {
            is AppState.Success -> {
                if (temp){
                    binding.mainFragmentLoadingLayout.visibility = View.GONE
                    adapter.setWeather(appState.weatherData)
                } else {
                    mainFragmentLoadingLayout.visibility = View.GONE
                    mainFragmentRootView.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        { viewModel.getWeatherFromLocalSourceRus() })
                }
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }

            is AppState.Error -> {
                mainFragmentLoadingLayout.visibility = View.GONE
                mainFragmentRootView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                { viewModel.getWeatherFromLocalSourceRus() })
            }
//            is AppState.Error -> {
//                binding.mainFragmentLoadingLayout.visibility = View.GONE
//                Snackbar.make(
//                    binding.mainFragmentFAB,
//                    getString(R.string.error),
//                    Snackbar.LENGTH_INDEFINITE
//                )
//                    .setAction(getString(R.string.reload)) { viewModel.getWeatherFromLocalSourceRus() }
//                    .show()
//            }
        }
    }

    fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        this.requestFocus()
        imm.showSoftInput(this, 0)
    }
    // Расширяем функционал вью для скрытия клавиатуры
    fun View.hideKeyboard(): Boolean {
        try {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return inputMethodManager.hideSoftInputFromWindow(windowToken, 0) }
        catch (ignored: RuntimeException) { }
        return false
    }


    companion object {
        fun newInstance() =
            MainFragment()
    }
}