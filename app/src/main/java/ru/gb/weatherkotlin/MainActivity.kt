package ru.gb.weatherkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.gb.weatherkotlin.databinding.MainActivityBinding
//import ru.gb.weatherkotlin.view.details.DetailsFragment
import ru.gb.weatherkotlin.view.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(getLayoutInflater())
        val view = binding.getRoot()
        setContentView(view)

            //setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitAllowingStateLoss()
                //.commit()
        }
    }
}