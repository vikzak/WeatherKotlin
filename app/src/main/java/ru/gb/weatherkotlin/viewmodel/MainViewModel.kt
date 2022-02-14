package ru.gb.weatherkotlin.viewmodel

import android.os.SystemClock.sleep
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.weatherkotlin.repository.Repository
import ru.gb.weatherkotlin.repository.RepositoryImpl


class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()) : ViewModel() {

    fun getLiveData() : LiveData<AppState> = liveDataToObserve

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)
    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)
    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)


    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            val repeatLimit = 2
            var repeatCurrent = 0
            while (repeatCurrent != repeatLimit) {
                try {
                    liveDataToObserve.postValue(
                        AppState.Success(
                            if (isRussian) {
                                repositoryImpl.getWeatherFromLocalStorageRus()
                            } else {
                                repositoryImpl.getWeatherFromLocalStorageWorld()
                            }
                        ))
                    break
                } catch (e: Exception) {
                    e.printStackTrace()
                    repeatCurrent++
                    if (repeatCurrent == repeatLimit) {
                        liveDataToObserve.postValue(AppState.Error(RuntimeException("Ошибка подключения к серверу. Попробуйте еще раз")))
                    }
                }
            }
        }.start()
    }
}