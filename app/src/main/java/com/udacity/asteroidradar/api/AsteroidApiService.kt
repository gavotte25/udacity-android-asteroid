package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofitScalar = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(Constants.BASE_URL).build()

private val retrofitMoshi = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(Constants.BASE_URL).build()

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
            @Query("start_date") startDate: String,
            @Query("end_date") endDate: String,
            @Query("api_key") apiKey: String = Constants.API_KEY
    ): String

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(@Query("api_key") apiKey: String = Constants.API_KEY): PictureOfDay
}

object AsteroidApi {
    val retrofitScalarService: AsteroidApiService by lazy { retrofitScalar.create(AsteroidApiService::class.java) }
    val retrofitMoshiService: AsteroidApiService by lazy { retrofitMoshi.create(AsteroidApiService::class.java) }
}