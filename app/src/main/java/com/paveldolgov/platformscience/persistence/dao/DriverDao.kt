package com.paveldolgov.platformscience.persistence.dao

import androidx.room.*
import com.paveldolgov.platformscience.persistence.model.Driver
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {

    @Query("SELECT * FROM driver")
    fun getDrivers(): Flow<List<Driver>>

    @Query("SELECT * FROM driver")
    fun getDriversList(): List<Driver>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDrivers(drivers: List<Driver>)

    @Delete
    fun delete(drivers: List<Driver>)

    @Query("DELETE FROM driver")
    fun deleteAll()
}