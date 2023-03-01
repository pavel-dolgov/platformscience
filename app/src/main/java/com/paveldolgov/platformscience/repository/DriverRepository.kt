package com.paveldolgov.platformscience.repository

import arrow.core.Either
import com.paveldolgov.platformscience.model.Driver
import com.paveldolgov.platformscience.model.DriversAndShipments
import com.paveldolgov.platformscience.model.DriversAndShipmentsError
import com.paveldolgov.platformscience.persistence.adapter.toAppModel
import com.paveldolgov.platformscience.persistence.dao.DriverDao
import com.paveldolgov.platformscience.servise.DriversAndShipmentsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriverRepository @Inject constructor(
    private val driverDao: DriverDao,
    private val driversAndShipmentsService: DriversAndShipmentsService
) {

    val drivers: Flow<List<Driver>> = driverDao.getDrivers().map { drivers ->
        drivers.map { it.toAppModel() }
    }

    suspend fun refreshDriversAndShipments(): Either<DriversAndShipmentsError, DriversAndShipments> {
        return driversAndShipmentsService.getDriversAndShipments()
    }
}