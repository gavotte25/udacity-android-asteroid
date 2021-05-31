package com.udacity.asteroidradar.repository

import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.NetworkParams.getRefreshParams
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
                val (startDate, endDate) = getRefreshParams()
                val jsonString = AsteroidApi.retrofitScalarService.getAsteroids(startDate, endDate)
                val asteroidList = parseAsteroidsJsonResult(JSONObject(jsonString))
                database.insert(*asteroidList.asDatabaseModel())
            }
            catch (e: UnknownHostException) {}
        }
    }

    suspend fun updateAsteroidsNext7Days() {
        withContext(Dispatchers.IO) {
            try {
                val (startDate, endDate) = getRefreshParams()
                val jsonString = AsteroidApi.retrofitScalarService.getAsteroids(startDate, endDate)
                val asteroidList = parseAsteroidsJsonResult(JSONObject(jsonString))
                val asteroidListNext7Days = asteroidList.filter{
                    it.closeApproachDate != startDate
                } as ArrayList
                database.insert(*asteroidListNext7Days.asDatabaseModel())
                database.deleteOldAsteroids()
            }
            catch (e: UnknownHostException) {}
        }
    }

    val asteroids = Transformations.map(database.getAsteroids()) { it.asDomainModel() }

    val pictureOfDay = database.getPicOfDay().map { it?.asDomainModel() }

    suspend fun refreshPicOfDay() {
        withContext(Dispatchers.IO) {
         try {
            val pic = AsteroidApi.retrofitMoshiService.getPictureOfTheDay().asDatabaseModel()
            if (pic.mediaType == "image") {
                database.insertPic(pic)
                database.deletePic(pic.url)
            }
        }
        catch (e: UnknownHostException){}
        }
    }

}