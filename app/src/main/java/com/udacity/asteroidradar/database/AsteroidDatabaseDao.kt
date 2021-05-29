package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg asteroid: DatabaseAsteroid)

    @Query("SELECT * FROM asteroid_table WHERE DATE(close_approach_date) >= DATE('now') ORDER BY DATE(close_approach_date)")
    suspend fun getAsteroids(): List<DatabaseAsteroid>

}