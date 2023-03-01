package com.paveldolgov.platformscience.servise

import arrow.core.Either
import com.paveldolgov.platformscience.client.Client
import com.paveldolgov.platformscience.client.model.error.ShipmentRequestError
import com.paveldolgov.platformscience.model.DriversAndShipments
import com.paveldolgov.platformscience.model.DriversAndShipmentsError
import com.paveldolgov.platformscience.model.adapter.toAppModel
import com.paveldolgov.platformscience.persistence.adapter.toDbModel
import com.paveldolgov.platformscience.persistence.dao.DriverDao
import com.paveldolgov.platformscience.persistence.dao.ShipmentDao
import com.paveldolgov.platformscience.util.CoroutinesDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class requests drivers and shipments from network via [Client] and saves the result to the database
 */
@Singleton
class DriversAndShipmentsService @Inject constructor(
    private val client: Client,
    private val driverDao: DriverDao,
    private val shipmentDao: ShipmentDao,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
) {

    companion object {
        const val MIN_ENTRIES_COUNT = 5
    }

    suspend fun getDriversAndShipments(): Either<DriversAndShipmentsError, DriversAndShipments> {
        return withContext(dispatcherProvider.io) {
            client.getShipmentsAndDrivers().fold(
                { error ->
                    // exceptions coming from client can not be resolved by an user action
                    // they all are NonRetryable, but we pretend here that some exceptions
                    // can be resolved by a user, and returning Retryable error for the IOExceptions
                    when (error) {
                        is ShipmentRequestError.IOException -> Either.Left(DriversAndShipmentsError.Retryable)
                        is ShipmentRequestError.JsonException -> Either.Left(
                            DriversAndShipmentsError.NonRetryable
                        )
                    }
                }, { response ->

                    // take random number of drivers, no smaller than 5
                    val driversNeeded = (MIN_ENTRIES_COUNT..response.drivers.size).random()
                    val newDrivers =
                        response.drivers.shuffled().take(driversNeeded).map { it.toAppModel() }
                    val newDbDrivers = newDrivers.map { it.toDbModel() }

                    // not the most optimal solution, but with our limited drivers will work fine
                    // alternative to consider "isActive" flag and create some cleanup job
                    val currentDbDrivers = driverDao.getDriversList()
                    val driversToDelete = currentDbDrivers.filter {
                        !newDbDrivers.map { driver -> driver.id }.toSet().contains(it.id)
                    }

                    // take random number of shipments, no smaller than 5
                    val shipmentsNeeded = (MIN_ENTRIES_COUNT..response.shipments.size).random()
                    val newShipments =
                        response.shipments.shuffled().take(shipmentsNeeded).map { it.toAppModel() }
                    val newDbShipments = newShipments.map { it.toDbModel() }
                    val currentDbShipments = shipmentDao.getShipmentsList()
                    val shipmentsToDelete = currentDbShipments.filter {
                        !newDbShipments.map { shipment -> shipment.id }.toSet().contains(it.id)
                    }

                    driverDao.delete(driversToDelete)
                    shipmentDao.delete(shipmentsToDelete)

                    driverDao.insertDrivers(newDbDrivers)
                    shipmentDao.insertShipments(newDbShipments)

                    Either.Right(DriversAndShipments(newDrivers, newShipments))
                })
        }
    }
}