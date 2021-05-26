package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.NetworkUtils
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

enum class Filter {WEEK, TODAY, SAVED}

class MainViewModel : ViewModel() {

    private var _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _asteroidList = MutableLiveData<ArrayList<Asteroid>>()
    val asteroidList: LiveData<ArrayList<Asteroid>>
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
        updateDateStrings()
        when (filter) {
            Filter.SAVED -> {_asteroidList.value = null}
            Filter.TODAY -> getAsteroids(today, today)
            else -> getAsteroids(today, defaultEndDate)
        }
    }

}