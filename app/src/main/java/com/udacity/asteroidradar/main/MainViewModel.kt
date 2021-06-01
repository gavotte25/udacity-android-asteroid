package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NetworkParams.dateFormat
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.util.*

enum class FilterStatus {WEEK, TODAY, SAVED}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDatabase.getInstance(application).asteroidDatabaseDao
    private val asteroidRepository = AsteroidRepository(database)

    private val filter = Filter(asteroidRepository.asteroids)

    private val _filteredAsteroidList = MutableLiveData<List<Asteroid>>()
    val filteredAsteroidList: LiveData<List<Asteroid>>
        get() = _filteredAsteroidList

    val pictureOfTheDay = asteroidRepository.pictureOfDay

    init {
        filter.asteroids.observeForever{
            _filteredAsteroidList.value = filter.filteredAsteroids
        }

        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
            asteroidRepository.refreshPicOfDay()
        }
    }

    fun updateFilter(status: FilterStatus){
        filter.mode = status
        _filteredAsteroidList.value = filter.filteredAsteroids
    }

    private class Filter(val asteroids: LiveData<List<Asteroid>>) {
        var mode = FilterStatus.WEEK

        val filteredAsteroids: List<Asteroid>
            get() =
                if (asteroids.value != null) {
                    when(mode) {
                        FilterStatus.WEEK -> asteroids.value!!.filterWeek()
                        FilterStatus.TODAY -> asteroids.value!!.filterToday()
                        else -> asteroids.value!!
                    }
                }
                else{listOf<Asteroid>()}

        private fun List<Asteroid>.filterToday(): List<Asteroid> {
            return  this.filter{
                it.closeApproachDate == dateFormat.format(Date())
            }
        }

        private fun List<Asteroid>.filterWeek(): List<Asteroid>  {
            val calendar = Calendar.getInstance()
            val todayString = dateFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, 6)
            val endOfWeekString = dateFormat.format(calendar.time)

            val today = requireNotNull(dateFormat.parse(todayString))
            val endOfWeek = requireNotNull(dateFormat.parse(endOfWeekString))

            return this.filter{
                val approachDate = requireNotNull(dateFormat.parse(it.closeApproachDate))
                (approachDate >= today) && (approachDate <= endOfWeek)
            }
        }
    }

}

