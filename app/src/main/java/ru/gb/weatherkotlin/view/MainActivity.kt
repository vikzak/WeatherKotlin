package ru.gb.weatherkotlin.view

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
//import ru.gb.weatherkotlin.broadcast.MainBroadcastReceiver
//import ru.gb.weatherkotlin.broadcast.NetworkBroadcastReceiver
import ru.gb.weatherkotlin.R
import ru.gb.weatherkotlin.databinding.MainActivityBinding
import ru.gb.weatherkotlin.view.main.MainFragment

//import ru.gb.weatherkotlin.view.details.DetailsFragment
//import ru.gb.weatherkotlin.view.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            showMainFragment()
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun showMainFragment() {
        showFragment(MainFragment.newInstance())
    }
}