package ru.gb.weatherkotlin.view

import ru.gb.weatherkotlin.model.Weather

interface OnItemViewClickListener {
    fun onItemViewClick(weather: Weather)
}