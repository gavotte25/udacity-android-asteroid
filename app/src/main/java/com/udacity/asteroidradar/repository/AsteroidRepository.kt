package com.udacity.asteroidradar.repository

import androidx.lifecycle.Transformations
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
import java.net.UnknownHostException

class AsteroidRepository(private val database: AsteroidDatabaseDao) {

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val (startDate, endDate) = getNetWorkParams()
                val jsonString = AsteroidApi.retrofitScalarService.getAsteroids(startDate, endDate)
                val asteroidList = parseAsteroidsJsonResult(JSONObject(jsonString))
                database.insert(*asteroidList.asDatabaseModel())
            }
            catch (e: UnknownHostException) {}
        }
    }

    val asteroids = Transformations.map(database.getAsteroids()) { it.asDomainModel() }

    suspend fun getPictureOfTheDay(): PictureOfDay? {
        return withContext(Dispatchers.IO) {
         try {
                val pic = AsteroidApi.retrofitMoshiService.getPictureOfTheDay()
                when (pic.mediaType) {
                    "image" -> pic
                    else -> null
                }
            }
        catch (e: UnknownHostException){null}
        }
    }

}