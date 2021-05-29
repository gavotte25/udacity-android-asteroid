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

enum class FilterStatus {WEEK, TODAY, SAVED}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDatabase.getInstance(application).asteroidDatabaseDao
    private val asteroidRepository = AsteroidRepository(database)

    private val filter = Filter(asteroidRepository.asteroids)

    private val _filteredAsteroidList = MutableLiveData<List<Asteroid>>()
    val filteredAsteroidList: LiveData<List<Asteroid>>
        get() = _filteredAsteroidList

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay


    init {
        filter.asteroids.observeForever{
            _filteredAsteroidList.value = filter.filteredAsteroids
        }

        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
            _pictureOfTheDay.value = asteroidRepository.getPictureOfTheDay()
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
            calendar.add(Calendar.DAY_OF_YEAR, 6)
            val endOfWeek = calendar.time
            val today = Date()
            return this.filter{
                val approachDate = requireNotNull(dateFormat.parse(it.closeApproachDate))
                (approachDate >= today) && (approachDate <= endOfWeek)
            }
        }
    }

}

