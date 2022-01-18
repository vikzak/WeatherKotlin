package ru.gb.weatherkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import ru.gb.weatherkotlin.view.details.DetailsFragment
import ru.gb.weatherkotlin.view.main.MainFragment

//private lateinit var binding: MainActivityBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = MainActivityBinding.inflate(getLayoutInflater())
//        val view = binding.getRoot()
//        setContentView(view)

            setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }
}