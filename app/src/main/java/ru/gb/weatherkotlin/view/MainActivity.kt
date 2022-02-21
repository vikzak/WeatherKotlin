package ru.gb.weatherkotlin.view

//import ru.gb.weatherkotlin.broadcast.MainBroadcastReceiver
//import ru.gb.weatherkotlin.broadcast.NetworkBroadcastReceiver
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.gb.weatherkotlin.R
import ru.gb.weatherkotlin.databinding.MainActivityBinding
import ru.gb.weatherkotlin.view.main.MainFragment
import ru.gb.weatherkotlin.view.main.maps.MapsFragment

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        menuInflater.inflate(R.menu.menu_content_provider, menu)
        menuInflater.inflate(R.menu.maps_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                showFragmentWithBackStack(HistoryFragment.newInstance())
                true
            }
            R.id.menu_content_provider ->{
                showFragmentWithBackStack(ContentProviderFragment.newInstance())
                true
            }
            R.id.menu_google_maps -> {
                showFragmentWithBackStack(MapsFragment.newInstance())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun showFragmentWithBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}