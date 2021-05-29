package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.NetworkParams.getNetWorkParams
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabaseDao) {

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val (startDate, endDate) = getNetWorkParams()
            val jsonString = AsteroidApi.retrofitScalarService.getAsteroids(startDate, endDate)
            val asteroidList = parseAsteroidsJsonResult(JSONObject(jsonString))
            database.insert(*asteroidList.asDatabaseModel())
        }
    }

    suspend fun getAsteroids(): List<Asteroid> {
        return database.getAsteroids().asDomainModel()
    }

    suspend fun getPictureOfTheDay(): PictureOfDay? {
        val result = AsteroidApi.retrofitMoshiService.getPictureOfTheDay()
        return when (result.mediaType) {
            "image" -> result
            else -> null
        }
    }

}