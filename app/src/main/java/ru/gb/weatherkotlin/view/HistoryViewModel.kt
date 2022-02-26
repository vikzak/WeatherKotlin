package ru.gb.weatherkotlin.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.weatherkotlin.app.App.Companion.getHistoryDao
import ru.gb.weatherkotlin.repository.LocalRepositoryImpl
import ru.gb.weatherkotlin.room.LocalRepository
import ru.gb.weatherkotlin.viewmodel.AppState

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepository: LocalRepository =
        LocalRepositoryImpl(getHistoryDao()) ) : ViewModel() {

    fun getAllHistory() {
        historyLiveData.value = AppState.Loading
        historyLiveData.value = AppState.Success(historyRepository.getAllHistory()) }
}