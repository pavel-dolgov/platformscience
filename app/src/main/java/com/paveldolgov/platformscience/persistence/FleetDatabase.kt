package com.paveldolgov.platformscience.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.paveldolgov.platformscience.persistence.dao.DriverAssignmentDao
import com.paveldolgov.platformscience.persistence.dao.DriverDao
import com.paveldolgov.platformscience.persistence.dao.ShipmentDao
import com.paveldolgov.platformscience.persistence.model.Driver
import com.paveldolgov.platformscience.persistence.model.DriverAssignment
import com.paveldolgov.platformscience.persistence.model.Shipment
import com.paveldolgov.platformscience.persistence.model.converter.DriverIdConverter
import com.paveldolgov.platformscience.persistence.model.converter.ShipmentIdConverter


@Database(entities = [Driver::class, Shipment::class, DriverAssignment::class], version = 1)
@TypeConverters(DriverIdConverter::class, ShipmentIdConverter::class)
abstract class FleetDatabase : RoomDatabase() {

    abstract fun shipmentDao(): ShipmentDao
    abstract fun driverDao(): DriverDao
    abstract fun driverAssignmentDao(): DriverAssignmentDao

    companion object {
        private const val DATABASE_NAME = "fleet"

        @Volatile
        private var instance: FleetDatabase? = null

        fun getInstance(context: Context): FleetDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): FleetDatabase {
            return Room.databaseBuilder(context, FleetDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}