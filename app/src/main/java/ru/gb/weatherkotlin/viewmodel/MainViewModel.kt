package ru.gb.weatherkotlin.viewmodel

import android.os.SystemClock.sleep
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.weatherkotlin.repository.Repository
import ru.gb.weatherkotlin.repository.RepositoryImpl


class MainViewModel(
    private val liveDataToObserve : MutableLiveData<AppState> = MutableLiveData()) : ViewModel() {
    private val repository: RepositoryImpl = Repository()

    fun getLiveData() : LiveData<AppState> = liveDataToObserve
    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)
    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)
    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)


    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(
                AppState.Success(
                    if (isRussian)
                        repository.getWeatherFromLocalStorageRus()
                    else
                        repository.getWeatherFromLocalStorageWorld()
                )
            )
        }.start()
    }
}