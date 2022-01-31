package ru.gb.weatherkotlin.view

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.gb.weatherkotlin.MainBroadcastReceiver
import ru.gb.weatherkotlin.R
import ru.gb.weatherkotlin.databinding.MainActivityBinding
//import ru.gb.weatherkotlin.view.details.DetailsFragment
import ru.gb.weatherkotlin.view.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private val receiver = MainBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.ok.setOnClickListener(clickListener)
        //registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(receiver, filter)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }
    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}