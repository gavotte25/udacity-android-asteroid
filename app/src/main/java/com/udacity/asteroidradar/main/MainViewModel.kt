package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NetworkParams
import com.udacity.asteroidradar.api.NetworkParams.dateFormat
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.util.*

enum class Filter {WEEK, TODAY, SAVED}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDatabase.getInstance(application).asteroidDatabaseDao
    private val asteroidRepository = AsteroidRepository(database)

    private val _filteredAsteroidList = MutableLiveData<List<Asteroid>>()
    val filteredAsteroidList: LiveData<List<Asteroid>>
        get() = _filteredAsteroidList

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    private lateinit var allAsteroids: List<Asteroid>

    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
            allAsteroids = asteroidRepository.getAsteroids()
            _filteredAsteroidList.value = allAsteroids
            _pictureOfTheDay.value = asteroidRepository.getPictureOfTheDay()
        }

    }

    fun updateFilter(filter: Filter) {
        viewModelScope.launch {
            if (_filteredAsteroidList.value == null) return@launch
            when (filter) {
                Filter.SAVED -> {
                    _filteredAsteroidList.value = allAsteroids
                }
                Filter.TODAY -> filterToday()
                else -> filterWeek()
            }
        }
    }

    private fun filterToday() {
        _filteredAsteroidList.value = allAsteroids.filter{
            it.closeApproachDate == dateFormat.format(Date())
        }
    }

    private fun filterWeek() {
        _filteredAsteroidList.value = allAsteroids.filter{
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            val endOfWeek = calendar.time
            val today = Date()
            val approachDate = requireNotNull(dateFormat.parse(it.closeApproachDate))
            (approachDate >= today) && (approachDate <= endOfWeek)
        }
    }

}