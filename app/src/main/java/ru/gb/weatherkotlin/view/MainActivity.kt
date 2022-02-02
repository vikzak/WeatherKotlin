package ru.gb.weatherkotlin.view

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.gb.weatherkotlin.broadcast.MainBroadcastReceiver
import ru.gb.weatherkotlin.broadcast.NetworkBroadcastReceiver
import ru.gb.weatherkotlin.R
import ru.gb.weatherkotlin.databinding.MainActivityBinding
//import ru.gb.weatherkotlin.view.details.DetailsFragment
import ru.gb.weatherkotlin.view.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private val receiver = MainBroadcastReceiver()
    private val receiverNetworkBroadcastReceiver = NetworkBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(receiver, filter)
        registerReceiver(receiverNetworkBroadcastReceiver, filter)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }
    override fun onDestroy() {
        unregisterReceiver(receiver)
        unregisterReceiver(receiverNetworkBroadcastReceiver)
        super.onDestroy()
    }
}