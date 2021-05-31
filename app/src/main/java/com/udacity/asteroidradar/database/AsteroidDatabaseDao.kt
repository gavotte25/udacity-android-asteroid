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
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("DELETE FROM asteroid_table WHERE DATE(close_approach_date) < DATE('now')")
    fun deleteOldAsteroids()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPic(pic: DatabasePictureOfDay)

    @Query("SELECT * FROM picture_of_the_day_table LIMIT 1")
    fun getPicOfDay(): LiveData<DatabasePictureOfDay?>

    @Query("DELETE FROM picture_of_the_day_table WHERE url NOT LIKE :exceptUrl")
    fun deletePic(exceptUrl: String)



}