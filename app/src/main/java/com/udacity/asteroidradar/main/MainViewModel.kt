package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.NetworkUtils
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

enum class Filter {WEEK, TODAY, SAVED}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val  database = AsteroidDatabase.getInstance(application).asteroidDatabaseDao
    private var _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private lateinit var today: String
    private lateinit var defaultEndDate: String
    private lateinit var calendar: Calendar

    init {
        getPictureOfTheDate()
        updateDateStrings()
        getAsteroids(today, defaultEndDate)
    }

    private fun getAsteroids(startDate: String, endDate: String) {
        viewModelScope.launch {
            val jsonString = AsteroidApi.retrofitScalarService.getAsteroids(startDate, endDate)
            _asteroidList.value = NetworkUtils.parseJsonToAsteroidList(JSONObject(jsonString))
            updateDatabase()
        }
    }

    private fun getPictureOfTheDate() {
        viewModelScope.launch {
            val result = AsteroidApi.retrofitMoshiService.getPictureOfTheDay()
            _pictureOfDay.value = when (result.mediaType) {
                "image" -> result
                else -> null
            }
        }
    }

    private fun updateDateStrings() {
        calendar = Calendar.getInstance()
        today = NetworkUtils.dateFormat.format(calendar.time)
        calendar.add(Calendar.DATE, Constants.DEFAULT_END_DATE_DAYS)
        defaultEndDate = NetworkUtils.dateFormat.format(calendar.time)
    }

    fun updateFilter(filter: Filter) {
        viewModelScope.launch {
            updateDateStrings()
            when (filter) {
                Filter.SAVED -> getSavedAsteroid()
                Filter.TODAY -> getAsteroids(today, today)
                else -> getAsteroids(today, defaultEndDate)
            }
        }
    }

    private suspend fun updateDatabase() {
        _asteroidList.value?.apply {
            for (i in this) {
                database.insert(i)
            }
        }
    }

    private suspend fun getSavedAsteroid() {
        val result = database.getAllAsteroids()
        _asteroidList.value = result
    }

}