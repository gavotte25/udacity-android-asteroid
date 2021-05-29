package com.udacity.asteroidradar.api

import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.DatabasePictureOfDay

data class NetworkAsteroid(
        val id: Long,
        val codename: String,
        val closeApproachDate: String,
        val absoluteMagnitude: Double,
        val estimatedDiameter: Double,
        val relativeVelocity: Double,
        val distanceFromEarth: Double,
        val isPotentiallyHazardous: Boolean
)

data class NetworkPictureOfDay(
        @Json(name = "media_type") val mediaType: String,
        val title: String,
        val url: String
)

fun NetworkPictureOfDay.asDatabaseModel(): DatabasePictureOfDay {
    return DatabasePictureOfDay(
            mediaType = this.mediaType,
            title = this.title,
            url = this.url
    )
}

fun ArrayList<NetworkAsteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}