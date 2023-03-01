package com.paveldolgov.platformscience.persistence.dao

import androidx.room.*
import com.paveldolgov.platformscience.persistence.model.Shipment
import kotlinx.coroutines.flow.Flow

@Dao
interface ShipmentDao {

    @Query("SELECT * FROM shipment")
    fun getShipments(): Flow<List<Shipment>>

    @Query("SELECT * FROM shipment")
    fun getShipmentsList(): List<Shipment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShipments(drivers: List<Shipment>)

    @Delete
    fun delete(shipments: List<Shipment>)

    @Query("DELETE FROM shipment")
    fun deleteAll()
}