package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object NetworkUtils {

    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    // My customized function to convert dynamic date range
    fun parseJsonToAsteroidList(jsonResult: JSONObject): ArrayList<Asteroid> {
        val neoJSONObject = jsonResult.getJSONObject("near_earth_objects")
        val asteroidList = ArrayList<Asteroid>()
        val sortedDateList = sortDate(neoJSONObject.keys())
        for (date in sortedDateList) {
            val dateAsteroidJsonArray = neoJSONObject.getJSONArray(date)
            for (i in 0 until dateAsteroidJsonArray.length()) {
                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
                val id = asteroidJson.getLong("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                        .getJSONObject("kilometers").getDouble("estimated_diameter_max")
                val closeApproachData = asteroidJson
                        .getJSONArray("close_approach_data").getJSONObject(0)
                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                        .getDouble("kilometers_per_second")
                val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                        .getDouble("astronomical")
                val isPotentiallyHazardous = asteroidJson
                        .getBoolean("is_potentially_hazardous_asteroid")
                val asteroid = Asteroid(id, codename, date, absoluteMagnitude,
                        estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous)
                asteroidList.add(asteroid)
            }
        }
        return asteroidList
    }

    private fun sortDate(daysIterator: MutableIterator<String>): MutableList<String> {
        val dateList = mutableListOf<String>()
        daysIterator.forEach { dateList.add(it) }
        dateList.sortBy { dateFormat.parse(it) }
        return dateList
    }
}