package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

class MainViewModel : ViewModel() {

    // Dummy data, come back later
    val pictureOfDay = PictureOfDay(
        "image",
        "Lightning Eclipse from the Planet of the Goats",
        "https://apod.nasa.gov/apod/image/2105/LightningLunarEclipse_Kotsiopoulos_1024.jpg"
    )

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    init {
        // Dummy data, come back later
        _asteroidList.value = listOf(
            Asteroid(1, "ast1","2021-05-24", 4.2, 6.9, 100.0, 1000.0, true),
            Asteroid(2, "ast2","2021-05-24", 4.2, 6.9, 100.0, 1000.0, false),
            Asteroid(3, "ast1","2021-05-24", 4.2, 6.9, 100.0, 1000.0, true)
        )
    }

}