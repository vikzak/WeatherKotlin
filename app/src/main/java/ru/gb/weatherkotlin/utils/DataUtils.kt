package ru.gb.weatherkotlin.utils

import ru.gb.weatherkotlin.model.FactDTO
import ru.gb.weatherkotlin.model.Weather
import ru.gb.weatherkotlin.model.WeatherDTO
import ru.gb.weatherkotlin.model.getDefaultCity

//import com.example.androidwithkotlin.model.FactDTO
//import com.example.androidwithkotlin.model.Weather
//import com.example.androidwithkotlin.model.WeatherDTO
//import com.example.androidwithkotlin.model.getDefaultCity

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact: FactDTO = weatherDTO.fact!!
    return listOf(Weather(getDefaultCity(), fact.temp!!, fact.feels_like!!,
        fact.condition!!, fact.icon)) }