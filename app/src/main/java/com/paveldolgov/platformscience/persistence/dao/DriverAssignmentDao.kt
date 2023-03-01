package com.paveldolgov.platformscience.persistence.dao

import androidx.room.*
import com.paveldolgov.platformscience.persistence.model.DriverAssignment
import com.paveldolgov.platformscience.persistence.model.DriverAssignmentDetails
import com.paveldolgov.platformscience.persistence.model.DriverId
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverAssignmentDao {
    /**
     * We query from 3 tables in order to get assignment, driver and shipment.
     */
    @Transaction
    @Query("SELECT * FROM driver WHERE id = :driverId")
    fun getDriverAssignmentDetails(driverId: DriverId): Flow<DriverAssignmentDetails?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAssignments(assignments: List<DriverAssignment>)

    @Query("SELECT * FROM driver_assignment")
    fun getAssignmentsList(): List<DriverAssignment>

    @Query("DELETE FROM driver_assignment")
    fun deleteAll()
}