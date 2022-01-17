package ru.gb.weatherkotlin.ui.main

sealed class Months {
    data class January(val monthIndex: Int, val shortForm: String) : Months()
    data class February(val monthIndex: Int, val shortForm: String, val noOfDays: Int) : Months()
    data class March(val monthIndex: Int, val shortForm: String) : Months()
    data class Arril(val monthIndex: Int, val shortForm: String) : Months()
    data class May(val monthIndex: Int, val shortForm: String) : Months()
    data class June(val monthIndex: Int, val shortForm: String) : Months()
    data class Jule(val monthIndex: Int, val shortForm: String) : Months()
    data class August(val monthIndex: Int, val shortForm: String) : Months()
    data class September(val monthIndex: Int, val shortForm: String) : Months()
    data class Oktober(val monthIndex: Int, val shortForm: String) : Months()
    data class November(val monthIndex: Int, val shortForm: String) : Months()
    data class December(val monthIndex: Int, val shortForm: String) : Months()
}
