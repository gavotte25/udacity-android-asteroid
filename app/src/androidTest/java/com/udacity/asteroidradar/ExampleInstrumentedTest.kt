package com.udacity.asteroidradar

import android.util.Log
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException
import java.lang.Exception

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var asteroidDao: AsteroidDatabaseDao
    private lateinit var db: AsteroidDatabase
    private lateinit var repo: AsteroidRepository

    @Before
    fun createDb() {
        val context =InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AsteroidDatabase::class.java).allowMainThreadQueries().build()
        asteroidDao = db.asteroidDatabaseDao
        repo = AsteroidRepository(asteroidDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.udacity.asteroidradar", appContext.packageName)
    }

//    @Test
//    @Throws(Exception::class)
//    fun insertAndGetAsteroid() = runBlocking {
//        val asteroid1 = DatabaseAsteroid(
//                1, "name", "2021-05-29", 4.0,
//                1.0, 2.0, 3.0,false)
//        val asteroid2 = DatabaseAsteroid(
//            2, "name", "2021-05-29", 4.0,
//            1.0, 2.0, 3.0,false)
//        val arr = arrayOf(asteroid1, asteroid2)
//        asteroidDao.insert(*arr)
//        val list = asteroidDao.getDemoAsteroids()
//        assertEquals(2, list.value!!.count())
//    }

//    @Test
//    @Throws(Exception::class)
//    fun debug() = runBlocking {
//        repo.refreshAsteroids()
//        val asteroidList = asteroidDao.getAsteroidsDebug()
//        Log.i("debug here test", asteroidList.first().closeApproachDate)
//        assertEquals(2, 2)
//    }

}
