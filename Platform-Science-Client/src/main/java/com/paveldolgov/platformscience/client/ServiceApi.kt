package com.paveldolgov.platformscience.client

import arrow.core.Either
import com.paveldolgov.platformscience.client.model.DriversAndShipmentsResponse
import com.paveldolgov.platformscience.client.model.error.ShipmentRequestError

internal interface ServiceApi {

    suspend fun getShipmentsAndDrivers(): Either<ShipmentRequestError, DriversAndShipmentsResponse>
}