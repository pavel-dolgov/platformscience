package com.paveldolgov.platformscience.di

import android.content.Context
import com.paveldolgov.platformscience.persistence.FleetDatabase
import com.paveldolgov.platformscience.persistence.dao.DriverAssignmentDao
import com.paveldolgov.platformscience.persistence.dao.DriverDao
import com.paveldolgov.platformscience.persistence.dao.ShipmentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideFleetDatabase(@ApplicationContext context: Context): FleetDatabase {
        return FleetDatabase.getInstance(context)
    }

    @Provides
    fun provideDriverDao(database: FleetDatabase): DriverDao {
        return database.driverDao()
    }

    @Provides
    fun provideShipmentDao(database: FleetDatabase): ShipmentDao {
        return database.shipmentDao()
    }

    @Provides
    fun provideDriverAssignmentDao(database: FleetDatabase): DriverAssignmentDao {
        return database.driverAssignmentDao()
    }
}