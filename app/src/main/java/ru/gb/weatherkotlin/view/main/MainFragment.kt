package ru.gb.weatherkotlin.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import ru.gb.weatherkotlin.R
import ru.gb.weatherkotlin.databinding.FragmentMainBinding
import ru.gb.weatherkotlin.model.Weather
import ru.gb.weatherkotlin.utils.showSnackBarWithResources
import ru.gb.weatherkotlin.view.OnItemViewClickListener
import ru.gb.weatherkotlin.view.details.DetailsFragment
import ru.gb.weatherkotlin.viewmodel.AppState
import ru.gb.weatherkotlin.viewmodel.MainViewModel

//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.inputmethod.InputMethodManager
//import androidx.core.view.isVisible
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import com.google.android.material.snackbar.Snackbar
//import kotlinx.android.synthetic.main.fragment_main.*
//import ru.gb.weatherkotlin.R
//import ru.gb.weatherkotlin.databinding.FragmentMainBinding
//import ru.gb.weatherkotlin.model.Weather
//import ru.gb.weatherkotlin.view.details.DetailsFragment
//import ru.gb.weatherkotlin.viewmodel.AppState
//import ru.gb.weatherkotlin.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
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
                    .commit()
            }
        }
    })

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            mainFragmentRecyclerView.adapter = adapter
            mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        }
        with(viewModel) {
            getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
            getWeatherFromLocalSourceRus()
        }
    }

    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
        } else {
            viewModel.getWeatherFromLocalSourceRus()
        }.also { isDataSetRus = !isDataSetRus }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                binding.mainFragmentRecyclerView.isVisible = true
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
                binding.mainFragmentRecyclerView.isVisible = false
            }
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                binding.mainFragmentRecyclerView.isVisible = false
                binding.mainFragmentFAB.showSnackBarWithResources(
                    fragment = this,
                    text = R.string.error,
                    actionText = R.string.reload,
                    { viewModel.getWeatherFromLocalSourceRus() }
                )
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        adapter.removeListener()
        super.onDestroy()
    }
}